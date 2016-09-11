package com.tresksoft.toolbox;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobeng.libs.LibAppManager;
import com.treksoft.apps.LibApps;
import com.tresksoft.toolbox.data.CAplicacion;
import com.tresksoft.toolbox.data.CItemDefault;

public class ActivityMove2SD extends ListActivity{

	private LibApps libApps;
	private List<CAplicacion> aplicaciones;
	private List<CItemDefault> appsToSD;
	private List<CItemDefault> adapterSD;
	
	private AppReceiver receiver; 
	
	/**
	 * Intent.ACTION_PACKAGE_ADDED
	 */
	
	/**
	 * mode = 0 --> Solo se muestra el activity con los datos de la aplicación seleccionada
	 * mode = 1 --> Modo Batch
	 */
	private int mode = 0;
	
	private int indexApp = 0;
	
	private static final int INSTALLED_APP_DETAILS = 1;
	
	private AdapterDefault adapter;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case 0:
				
				adapterSD.clear();
				for (CItemDefault app: appsToSD) {
					adapterSD.add(app);
				}
				
				adapter.notifyDataSetChanged();
				// Actualizar status
				actualizarStatusBar();
			}
		}
	};
	private View empty;
	private TextView empty_tv;
	private TextView tvApps;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_move_sd);
		
		initialize();
		
		createHiloLoadApps();
	}
	
	protected void onResume() {
		super.onResume();
		registrar();
	}
	
	protected void onPause() {
		super.onPause();
		desregistrar();
		
	}

	private void initialize() {
		// TODO Auto-generated method stub
		libApps = new LibApps(this);
		appsToSD = new ArrayList<CItemDefault>();
		adapterSD = new ArrayList<CItemDefault>();
		
		adapter = new AdapterDefault(ActivityMove2SD.this, adapterSD);
		setListAdapter(adapter);
		
		this.empty = (LinearLayout)findViewById(R.id.empty);
		this.empty_tv = (TextView)findViewById(R.id.empty_tv);
		this.tvApps = (TextView)findViewById(R.id.tv_apps);
		
		final ListView lv = getListView();
		lv.setEmptyView(empty);
		empty_tv.setText(getResources().getString(R.string.lbl_no_apps_to_sd));
	}

	private void createHiloLoadApps() {
		(new Thread(new Runnable() {
			public void run() {
				loadApps();
			}
		})).start();
	}
	
	private void loadApps() {
			try {
				if (appsToSD.size() > 0) 
					appsToSD.clear();
				List<CItemDefault> apps = new ArrayList<CItemDefault>();
				apps = LibApps.getInstallApps(ActivityMove2SD.this, 0);
				for (CItemDefault app: apps) {
					appsToSD.add(app);
				}
				
				mHandler.sendEmptyMessage(0);
					
			} catch (Exception e) {
				e.printStackTrace();
			}				
	}

	private void actualizarStatusBar() {
		int apps = appsToSD.size();
		tvApps.setText(apps + " " + getResources().getString(R.string.lbl_apps));
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// Cambiamos modo a 0
		mode = 0;
		goApplicationInfo(position);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == INSTALLED_APP_DETAILS) {
			// Vuelve de pantalla de detalles
			if (++indexApp < appsToSD.size() && mode == 1)
				goApplicationInfo(indexApp);
			else if (mode == 1 || mode == 0)
				createHiloLoadApps();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void goApplicationInfo(int position) {
		
		ProcessApplication app = (ProcessApplication)getApplication();
		app.currentApplication = appsToSD.get(position).tv3;
		
		if (appsToSD.size() > 0) 
			LibAppManager.showPackageInfo(this, app.currentApplication, INSTALLED_APP_DETAILS);
	}	
	
	public void onHomeClick(View v) {
		mostrarActivity(ActivityMain.class);
	}
	
	public void onBatchClick(View v) {
		// Cambiamos a modo Batch
		if (appsToSD.size() > 0) {
			mode = 1;
			indexApp = 0;
			
			goApplicationInfo(indexApp);
		}
	}
	
	public void mostrarActivity(Class <?> cls) {
		Intent intent = new Intent(this, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
	public void registrar() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PACKAGE_ADDED");
		filter.addAction("android.intent.action.PACKAGE_CHANGED");
		filter.addAction("android.intent.action.PACKAGE_REPLACED");
		filter.addAction("android.intent.action.PACKAGE_REMOVED");
		filter.addAction("android.intent.action.PACKAGE_RESTARTED");
		filter.addDataScheme("package");
		
		receiver = new AppReceiver();
		registerReceiver(receiver, filter);
	}
	
	public void desregistrar() {
		if (receiver != null)
			unregisterReceiver(receiver);
	}
	
	class AppReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			createHiloLoadApps();
		}
	}
}
