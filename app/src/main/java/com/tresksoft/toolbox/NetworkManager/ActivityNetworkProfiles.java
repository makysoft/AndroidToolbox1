package com.tresksoft.toolbox.NetworkManager;

import java.util.List;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tresksoft.Views.ActivityMain;
import com.tresksoft.toolbox.Home.ProcessApplication;
import com.tresksoft.toolbox.WifiManager.FactoryWifi;
import com.tresksoft.toolbox.WifiManager.Wifi;
import com.tresksoft.toolbox.R;

public class ActivityNetworkProfiles extends ListActivity{

	private AdapterNetworkProfiles adapter;
	private List<NetworkProfile> list_network_profiles;
	
	private ProcessApplication app;
	private WifiReceiver receiver = new WifiReceiver();
	
	private NetworkProfileFactory factory; 
	private NetworkProfileGeneric npg;
	
	private FactoryWifi factoryWifi;
	
	private Wifi wifiObject;
	private LinearLayout empty;
	private TextView empty_tv;
	private TextView tv;
	private ListView lv;
	private TextView tvGateway;
	private TextView tvDNS1;
	private TextView tvDNS2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_network_profile_manager);
		
		// Inicializar items
		initialize();
		
		app = (ProcessApplication) getApplication();
		app.network_profile = null;
		
		// Inicializamos factorias 
		factory = new NetworkProfileFactory(ActivityNetworkProfiles.this,"database");
		factoryWifi = new FactoryWifi("real");
		
		// Creamos los objetos desde las factorias
		npg = factory.createNetworkProfile();
		wifiObject = factoryWifi.createFactory(this);
		
		// Registrar menu contextual
		ListView listview = (ListView) findViewById(android.R.id.list);
		registerForContextMenu(listview);
		
		
		lv.setEmptyView(this.empty);
		
		// Refrescamos el listView
		refrescarListView();
	}
	
	private void initialize() {
		this.lv = getListView();
		this.empty = (LinearLayout) findViewById(R.id.empty);
		this.empty_tv = (TextView) findViewById(R.id.empty_tv);
		this.tv = (TextView) findViewById(R.id.status_network);
		this.tvGateway = (TextView)findViewById(R.id.tvGateway);
		this.tvDNS1 = (TextView)findViewById(R.id.tvDNS1);
		this.tvDNS2 = (TextView)findViewById(R.id.tvDNS2);
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_contextual_network_profile, menu);
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.profile_activate:
			activarPerfil(list_network_profiles.get(info.position));
			return true;
		case R.id.profile_remove:
			borrarPerfil(list_network_profiles.get(info.position).nombreperfil);
			return true;
		case R.id.profile_modify:
			app.network_profile = list_network_profiles.get(info.position);
			mostrarActivityAdd();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	private void clean() {
		tv.setText(getResources().getString(R.string.lbl_network_profile_ip) + " 0.0.0.0/0");
		tvGateway.setText(getResources().getString(R.string.lbl_network_profile_gateway) + " 0.0.0.0");
		tvDNS1.setText(getResources().getString(R.string.lbl_network_profile_dns1) + " 0.0.0.0");
		tvDNS2.setText(getResources().getString(R.string.lbl_network_profile_dns2) + " 0.0.0.0");
	}
	
	private void activarPerfil(NetworkProfile profile) {
		// Borramos los datos anteriores
		clean();
		// Activamos el perfil
		wifiObject.setIP(profile);
		refrescarListView();
		wifiObject.reconnect();
	}
	
	private void borrarPerfil(String nameProfile) {
		npg.borrarPerfil(nameProfile);
		list_network_profiles.clear();
		if (adapter != null)
			adapter.notifyDataSetChanged();
		refrescarListView();
	}
	
	private final Handler handler = new Handler() {
		
		public void handleMessage(final Message msg) {
			if ((list_network_profiles == null) || (list_network_profiles.size() == 0)) {
				empty_tv.setText(getResources().getString(R.string.msg_no_network));
			} else {
				adapter = new AdapterNetworkProfiles(ActivityNetworkProfiles.this, list_network_profiles);
				setListAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}
	};
	
	public void refrescarListView() {
		new Thread() {
			public void run() {
				// Cargar coleccion profiles
				list_network_profiles = npg.getPerfiles();
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	public void onAddProfile(View v) {
		app.network_profile = null;
		mostrarActivityAdd();
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		NetworkProfile profile = list_network_profiles.get(position);
		// Mostrar cuadro dialogo editar
		app.network_profile = profile;
		mostrarActivityAdd();
	}
	
	private void mostrarActivityAdd() {
		Intent intent = new Intent(this, ActivityNetworkProfilesAdd.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
	protected void onPause() {
		unregisterReceiver(receiver);
		super.onPause();
	}
	
	protected void onResume() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		registerReceiver(receiver, filter);
		super.onResume();
	}
	
	protected void onDestroy() {
		npg.close();
		super.onDestroy();
	}
	
	public void onHomeClick(View v) {
		Intent intent = new Intent(this, ActivityMain.class);
    	startActivity(intent);
	}
	
	class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			String ip = wifiObject.getIP();
			String gateway = wifiObject.getGateway();
			String dns1 = wifiObject.getDNS1();
			String dns2 = wifiObject.getDNS2();
			String netmask = wifiObject.getNetMask();
			short shortNetMask = wifiObject.calculaSubred(netmask);
			
			tv.setText(getResources().getString(R.string.lbl_network_profile_ip) + " " + ip + "/" + shortNetMask);
			tvGateway.setText(getResources().getString(R.string.lbl_network_profile_gateway) + " " + gateway);
			tvDNS1.setText(getResources().getString(R.string.lbl_network_profile_dns1) + " " + dns1);
			tvDNS2.setText(getResources().getString(R.string.lbl_network_profile_dns2) + " " + dns2);
		}
	}
	
}
