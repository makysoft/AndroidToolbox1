package com.tresksoft.toolbox.WifiManager;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.mobeng.third_party.ActionItem;
import com.mobeng.third_party.QuickAction;
import com.tresksoft.Views.Main;
import com.tresksoft.graphics.SaundProgressBar;
import com.tresksoft.toolbox.ActivityPreferences;
import com.tresksoft.toolbox.Home.ProcessApplication;
import com.tresksoft.toolbox.R;

public class ActivityWifiManager extends ListActivity{

	private FactoryWifi factory = null;
	private Wifi wifiObject = null;
	private WifiManager mainWifi;
	private AdapterWifi adapterWifis;
	
	private String typeDevice = "real";
	
	private List<WifiItem> wifisConfiguradas;
	
	private WifiScanItem currentWifi;
	
	private Handler mHandler;
	private long mStartTime = 0L;
	
	private TextView tvWifiSSID;
	private TextView tvWifiLinkSpeed;
	private TextView tvWifiBSSID;
	
	private QuickAction mQuickAction = null;
	private SaundProgressBar bar;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wifi_manager);
		// Titulamos el activity
		this.setTitle("Wifi Manager 2");
		
		initialize();
		
		// Registrar context menu
		ListView listview = (ListView) findViewById(android.R.id.list);
		registerForContextMenu(listview);
		
		// Registrar tipo de conexion Emulador o Real
		factory = new FactoryWifi(typeDevice);
		wifiObject = (Wifi)factory.createFactory(this);
		wifiObject.setHandler(handler);
		//wifiObject.lock();
		//actualizarWifisConfiguradas();
		//wifiObject.startScan();
	}
	
	private void initialize() {
		bar = (SaundProgressBar)findViewById(R.id.miniprogressbar);
		mHandler = new Handler();
		tvWifiBSSID = (TextView)findViewById(R.id.lbl_status_wifi_bssid);
		tvWifiLinkSpeed = (TextView)findViewById(R.id.lbl_status_wifi_link_speed);
		tvWifiSSID= (TextView)findViewById(R.id.lbl_status_wifi_ssid);
		
        ActionItem actShowProfiles = new ActionItem();
        actShowProfiles.setTitle(getResources().getString(R.string.lbl_show_profiles));
        
        mQuickAction = new QuickAction(this);
        mQuickAction.addActionItem(actShowProfiles);
        
        mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			
			public void onItemClick(QuickAction source, int pos, int actionId) {
				// TODO Auto-generated method stub
				onProfilesClick();
			}
		});		
        
        bar.setMax(54);
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
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		
		// Preparar menu
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_contextual_wifi_survey, menu);
		
		if (wifiObject.wifis.get(info.position).wificonfigurada == null) {
			menu.removeItem(menu.findItem(R.id.wifi_connect).getItemId());
			menu.removeItem(menu.findItem(R.id.wifi_disconnect).getItemId());
		} else {
			menu.removeItem(menu.findItem(R.id.wifi_add).getItemId());
		}
	}	
	
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int security = 0;
		
		switch (item.getItemId()) {
		case R.id.wifi_connect:
			
			wifiObject.connect(wifiObject.wifis.get(info.position).wificonfigurada);
			return true;
			
		case R.id.wifi_disconnect:
			
			wifiObject.disconnect(wifiObject.wifis.get(info.position).wificonfigurada);
			return true;
			
		case R.id.wifi_add:
			
			if (wifiObject.wifis.get(info.position).capabilities.contains("WEP")) {
				security = 1;
			} else if (wifiObject.wifis.get(info.position).capabilities.contains("WPA")) {
				security = 3;
			}
			wifiObject.addWifi(wifiObject.wifis.get(info.position), security);
			return true;
			
		case R.id.signal_survey:
			
			ProcessApplication app = (ProcessApplication) getApplication();
			app.typeDevice = this.typeDevice;
			app.wifiItem = wifiObject.wifis.get(info.position);
			mostrarSignalSurvey();
			return true;
			
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	private final Handler handler = new Handler() {
		

		public void handleMessage(final Message msg) {
			if (msg.what == 0) {
				if ((wifiObject.wifis == null) || (wifiObject.wifis.size() == 0)) {
					
				}else {
					// Detectamos las wifis configuradas
					marcarWifisConfiguradas();
					
					adapterWifis = new AdapterWifi(ActivityWifiManager.this, wifiObject.wifis);
					setListAdapter(adapterWifis);
				}
			} else if (msg.what == 1) {
				actualizarEstado();
			}
		}
	};
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (wifiObject.wifis.get(position).wificonfigurada != null ) {
			wifiObject.connect(wifiObject.wifis.get(position).wificonfigurada);
		}
		
		ProcessApplication app = (ProcessApplication) getApplication();
		app.typeDevice = this.typeDevice;
		app.wifiItem = wifiObject.wifis.get(position);
		
		mostrarSignalSurvey();
	}
	
	protected void onPause() {
		wifiObject.pause();
    	mHandler.removeCallbacks(mUpdateTimeTask);		
		//wifiObject.unlock();
		super.onPause();
	}
	
	protected void onResume() {
		//wifiObject.lock();
		wifiObject.resume();
		// Cuando la pantalla recibe el control se actualiza el listado de wifis configuradas
		actualizarWifisConfiguradas();
		
    	mHandler.removeCallbacks(mUpdateTimeTask);
    	mHandler.postDelayed(mUpdateTimeTask, 100);		
		super.onResume();

	}
	
	public void onRefreshClick(View v) {
		wifiObject.startScan();
	}
	
	public void onProfilesClick() {
		Intent intent = new Intent(this, ActivityWifiProfile.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
	public void onAddProfile(View v) {	
		WifiLib.dialogAddWifi(this, mainWifi);
	}
	
	public void onHomeClick(View v) {
		Intent intent = new Intent(this, Main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
    public void onOverflowClick(View v) {
    	mQuickAction.show(v);
    }
    
	private void mostrarSignalSurvey() {
		Intent intent = new Intent(this, ActivityWifiSignalSurvey.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
	public void actualizarEstado() {
		// Comprobar si est� conectado a alguna wifi
		tvWifiSSID.setText(wifiObject.getConnectionInfo());
		tvWifiBSSID.setText("BSSID " + wifiObject.getBSSID());
		
		actualizarInfoLink();
	}
	
	public void actualizarInfoLink() {
		int linkSpeed = wifiObject.getLinkSpeed();
		tvWifiLinkSpeed.setText(linkSpeed + "Mbps");
		bar.setProgress(linkSpeed);
	}
	
	public void actualizarWifisConfiguradas() {
		wifisConfiguradas = wifiObject.getWifisConfiguradas();
	}
	
	public void getCurrentWifi() {
		String currentSSID = wifiObject.getSSID();
		for(WifiScanItem wifi: wifiObject.wifis){
			if (wifi.equals(currentSSID))
				currentWifi = wifi;
		}
	}
	
	public void marcarWifisConfiguradas() {
		String s = "";
		for(WifiScanItem wifi: wifiObject.wifis) {
			for (WifiItem item: wifisConfiguradas) {
				s = item.SSID.replace("\"", "");
				if (wifi.SSID.equals(s)) {
					wifiObject.wifis.get(wifiObject.wifis.lastIndexOf(wifi)).wificonfigurada = item;
				}
			}
		}
	}
	
    private Runnable mUpdateTimeTask = new Runnable() {
    	public void run() {
    		final long start = mStartTime;
    		long millis = System.currentTimeMillis() - start;
    		int seconds = (int) (millis / 1000);
    		seconds = seconds % 60;
    		
			// Actualizamos la informaci�n del link
			actualizarInfoLink();
    		// Iniciamos el escaneo
    		wifiObject.startScan();
    		// Seteamos de nuevo el timer
    		mHandler.postDelayed(this, 10000);
    	}
    };
	
}
