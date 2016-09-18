package com.tresksoft.toolbox.Home;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager.MemoryInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobeng.libs.LibAppManager;
import com.mobeng.libs.LibBase;
import com.mobeng.libs.LibProcessManager;
import com.treksoft.apps.LibApps;
import com.tresksoft.toolbox.Funciones;
import com.tresksoft.toolbox.R;
import com.tresksoft.toolbox.data.CAplicacion;
import com.tresksoft.toolbox.data.CItemDefault;
import com.tresksoft.toolbox.data.CTamanhoBytes;
import com.tresksoft.toolbox.WifiManager.FactoryWifi;
import com.tresksoft.toolbox.WifiManager.Wifi;


public class ActivityMain extends Activity implements OnClickListener, HomeContract.View {

    private HomePresenter mHomePresenter;

	private LinearLayout layout_main;
	private LinearLayout layout_process_manager;
	private LinearLayout layout_app_manager;
	private LinearLayout layout_wifi_manager;
	private LinearLayout layout_network_manager;
	private LinearLayout layout_speed_settings;
	private LinearLayout layout_cache_manager;
	private LinearLayout layout_move2sd;
	
	private TextView tvWifi;
	private TextView tvProcesos;
	private TextView tvMemoriaLibre;
	private TextView tvMemory;
	private TextView tvSD;
	private TextView tvCache;
	private TextView tvMove2SD;
	
	// Wifi Manager
	private String typeDevice = "real";
	private FactoryWifi factory = null;
	private Wifi wifiObject = null;
	private boolean wifiEnabled = false;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        // Refactor MVP
        mHomePresenter = new HomePresenter(this, new HomeModel(this));
        //

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_main2);
		
		// Inicializar los objetos
		initialize();
		
//		factory = new FactoryWifi(typeDevice);
//		wifiObject = (Wifi)factory.createFactory(ActivityMain.this);
//		wifiObject.setHandler(mHandler);

	}
	
	private void initialize() {
		this.tvWifi = (TextView)findViewById(R.id.tv_main_wifi);
		this.tvProcesos = (TextView)findViewById(R.id.tv_procesos_ejecucion);
		this.tvMemoriaLibre = (TextView) findViewById(R.id.tv_free_mem);
		this.tvMemory = (TextView)findViewById(R.id.tv_apps_memory);
		this.tvSD = (TextView)findViewById(R.id.tv_apps_memory_sd);
		this.tvCache = (TextView)findViewById(R.id.tv_main_cache);
		this.tvMove2SD = (TextView)findViewById(R.id.tv_main_move2sd);
		
		this.layout_main = (LinearLayout)findViewById(R.id.ad_layout);
		this.layout_process_manager = (LinearLayout)findViewById(R.id.layout_process_manager);
		this.layout_app_manager = (LinearLayout)findViewById(R.id.layout_app_manager);
		this.layout_wifi_manager = (LinearLayout)findViewById(R.id.layout_wifi_manager);
		this.layout_network_manager = (LinearLayout)findViewById(R.id.layout_network_manager);
		this.layout_speed_settings = (LinearLayout)findViewById(R.id.layout_speed_settings);
		this.layout_cache_manager = (LinearLayout)findViewById(R.id.layout_cache_manager);
		this.layout_move2sd = (LinearLayout)findViewById(R.id.layout_move2sd);
		
		this.layout_process_manager.setOnClickListener(this);
		this.layout_app_manager.setOnClickListener(this);
		this.layout_wifi_manager.setOnClickListener(this);
		this.layout_network_manager.setOnClickListener(this);
		this.layout_speed_settings.setOnClickListener(this);
		this.layout_cache_manager.setOnClickListener(this);
		this.layout_move2sd.setOnClickListener(this);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_principal, menu);
		return true;
	}	
	
	public boolean onOptionsItemSelected(MenuItem item) {
        boolean retVal;

        retVal = mHomePresenter.onHomeOptionsItemSelected(item.getItemId());

        if (retVal) {
            return true;
        }
        return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

        // Refactor MVP
        mHomePresenter.onHomeButtonClick(v.getId());
        //

	}
	
//	private void iniciaTemporizadores() {
//		mHandler.postDelayed(mUpdateTime, 500);
//	}
//
//	private void cancelaTemporizadores() {
//		mHandler.removeCallbacks(mUpdateTime);
//	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {

		// Refactor MVP
		mHomePresenter.onHomeResume();
		//

//		iniciaTemporizadores();
//		wifiObject.resume();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
//		cancelaTemporizadores();
//		wifiObject.pause();
		super.onPause();
	}
	
//	private Handler mHandler = new Handler() {
//
//		 public void handleMessage(final Message msg) {
//			 switch(msg.what){
//			 case 0:
//				 if (wifiEnabled) {
//					 if ((wifiObject.wifis == null) || (wifiObject.wifis.size() == 0)) {
//
//					 }else {
//							tvWifi.setText(wifiObject.wifis.size() + " " + getResources().getString(R.string.lbl_main_wifi));
//					 }
//				 } else {
//					 tvWifi.setText(getResources().getString(R.string.lbl_wifi_disabled));
//				 }
//				 break;
//		 }
//	};
	
//	private Runnable mUpdateTime = new Runnable() {
//		public void run() {
//
//			wifiEnabled = wifiObject.isWifiEnabled();
//			if (wifiEnabled) {
//				(new Thread(new Runnable() {
//					public void run() {
//						informarWifi();
//					}
//				})).start();
//			} else {
//				mHandler.sendEmptyMessage(0);
//			}
//
//			mHandler.removeCallbacks(mUpdateTime);
//			mHandler.postDelayed(mUpdateTime, 10000);
//		}
//	};

	
	private void informarWifi() {
		wifiObject.startScan();
	}

	public void updateProcessInfo(long memoryAvailable, int numRunningProcess) {
		tvProcesos.setText(numRunningProcess  + " " + getResources().getString(R.string.button_process_list));
		tvMemoriaLibre.setText((String)getResources().getString(R.string.lbl_statusbar_free_mem) + " " + (new CTamanhoBytes(memoryAvailable)).toString());
	}

	public void updateAppInfo(long internalMemoryAvailable, long sdMemoryAvailable) {
		tvMemory.setText(internalMemoryAvailable + getResources().getString(R.string.lbl_memoria_interna_disponible));
		tvSD.setText(sdMemoryAvailable + getResources().getString(R.string.lbl_memoria_sd_disponible));
	}

	public void updateCacheInfo(long cacheTotal) {
		tvCache.setText((new CTamanhoBytes(cacheTotal)).toString() + " " + getResources().getString(R.string.lbl_cache));
	}

	public void updateMove2SDInfo(int appMoves2SD) {
		tvMove2SD.setText(appMoves2SD + " " + getResources().getString(R.string.lbl_apps));
		tvMove2SD.setText(appMoves2SD + " " + getResources().getString(R.string.lbl_apps));
	}

}
