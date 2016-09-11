package com.tresksoft.WifiManager;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tresksoft.Home.ActivityMain;
import com.tresksoft.toolbox.ActivityPreferences;
import com.tresksoft.toolbox.R;

public class ActivityWifiProfile extends ListActivity{

	private AdapterWifiProfile adapter;
	private WifiManager mainWifi;
	private List<WifiConfiguration> redesConfiguradas;
	
	private LinearLayout empty;
	private TextView empty_tv;
	private TextView tvNumProfiles;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wifi_manager_profile);
		// Titulamos el activity
		this.setTitle("Wifi Manager - Profiles");
		
		initialize();
		
		// Registrar context menu
		ListView listview = (ListView) findViewById(android.R.id.list);
		registerForContextMenu(listview);
		listview.setEmptyView(this.empty);
		
		// Obtener redes configuradas
		mainWifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);

		loadRedesConfiguradas();
		
	}
	
	private void initialize() {
		this.empty = (LinearLayout)findViewById(R.id.empty);
		this.empty_tv = (TextView)findViewById(R.id.empty_tv);
		tvNumProfiles = (TextView)findViewById(R.id.tv_status_network_profiles);
		
		empty_tv.setText(getResources().getString(R.string.lbl_no_wifi_profile));
	}
	
	private void actualizarInfoStatus() {
		tvNumProfiles.setText(redesConfiguradas.size() + " " + getResources().getString(R.string.lbl_profiles_configured));
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_principal, menu);
		return true;
	}	
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.item_preferences:
			mostrarPreferencias();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void mostrarPreferencias() {
		Intent intent = new Intent(this, ActivityPreferences.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_contextual_wifi_profile, menu);
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.wifi_remove:
			removeWifi(redesConfiguradas.get(info.position).networkId);
			return true;
		case R.id.wifi_modify:
			crearListenerDismissDialog(WifiLib.dialogAddWifi(this, mainWifi, redesConfiguradas.get(info.position).SSID.replace("\"", ""), 
					WifiLib.getSecurity(ActivityWifiProfile.this, redesConfiguradas.get(info.position).allowedAuthAlgorithms,
										 redesConfiguradas.get(info.position).allowedGroupCiphers,
										 redesConfiguradas.get(info.position).allowedKeyManagement,
										 redesConfiguradas.get(info.position).allowedPairwiseCiphers,
										 redesConfiguradas.get(info.position).allowedProtocols).codificacionInt ));
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	public void removeWifi(int networkID) {
		mainWifi.removeNetwork(networkID);
		redesConfiguradas.clear();
		if (adapter != null)
			adapter.notifyDataSetChanged();
		loadRedesConfiguradas();
	}
	
	private void loadRedesConfiguradas() {
		redesConfiguradas = mainWifi.getConfiguredNetworks();
		actualizarInfoStatus();
		handler.sendEmptyMessage(0);
	}

	private final Handler handler = new Handler() {
		public void handleMessage(final Message msg) {
			if ((redesConfiguradas == null) || redesConfiguradas.size() == 0) {
				
			} else {
				adapter = new AdapterWifiProfile(ActivityWifiProfile.this, redesConfiguradas);
				setListAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}
	};
	
	public void crearListenerDismissDialog(AlertDialog dialog) {
		dialog.setOnDismissListener(new OnDismissListener() {

			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				loadRedesConfiguradas();
			}
			
		});
	}
	
	public void onAddProfile(View v) {
		crearListenerDismissDialog(WifiLib.dialogAddWifi(this, mainWifi));
	}
	
	public void onRefreshClick(View v) {
		loadRedesConfiguradas();
	}
	
	public void onHomeClick(View v) {
		Intent intent = new Intent(this, ActivityMain.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
	public void onSurveyClick(View v) {
		Intent intent = new Intent(this, ActivityWifiManager.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);		
	}
}
