package com.tresksoft.WifiManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tresksoft.Home.ActivityMain;
import com.tresksoft.graphics.LineChart;
import com.tresksoft.toolbox.ProcessApplication;
import com.tresksoft.toolbox.R;
import com.tresksoft.wifi.FactoryWifi;
import com.tresksoft.wifi.Wifi;
import com.tresksoft.wifi.WifiScanItem;

public class ActivityWifiSignalSurvey extends Activity {
	
	FactoryWifi factory = null;
	Wifi wifiObject = null;
	
	WifiManager mainWifi;
	List<ScanResult> wifis;
	String typeDevice;
	WifiScanItem wifiItem;
	ToggleButton tb;
	Timer timer;
	
	int maxdbm = -100;
	long startTimeMillis = 0;
	long elapsedTimeMillis = 0;
	int segElapsed = 0;
	
	
	private LineChart lcSurvey;
	private ArrayList<String> list_dbm;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wifi_signal_survey);
		
		ProcessApplication app = (ProcessApplication) getApplication();
		typeDevice = app.typeDevice;
		wifiItem = app.wifiItem;
		
		factory = new FactoryWifi(typeDevice);
		wifiObject = (Wifi)factory.createFactory(this);
		wifiObject.setHandler(handler);
		
		TextView tv = (TextView)findViewById(R.id.lbl_status_wifi_ssid);
		tv.setText(wifiItem.SSID);
		
		tv = (TextView)findViewById(R.id.lbl_status_wifi_bssid);
		tv.setText("BSSID: " + wifiItem.BSSID);
		
		tv = (TextView)findViewById(R.id.lbl_status_channel);
		tv.setText(getResources().getString(R.string.lbl_channel) + ": " + wifiObject.getCanal(wifiItem.frequency));		
		
		tv = (TextView)findViewById(R.id.lbl_status_security);
		if (wifiItem.capabilities.contains("WEP")) {
			tv.setText("WEP");
		} else if (wifiItem.capabilities.contains("WPA2")) {
			tv.setText("WPA2");
		} else if (wifiItem.capabilities.contains("WPA")) {
			tv.setText("WPA");
		} else {
			tv.setText("OPEN");
		}
		
		lcSurvey = (LineChart) findViewById(R.id.linechart_survey);
		list_dbm = new ArrayList<String>();
		lcSurvey.list_axis = list_dbm;

		//wifiObject.lock();
		wifiObject.startScan();
		
	}
	
	public void actualizarEstado() {
		wifis = mainWifi.getScanResults();
		handler.sendEmptyMessage(0);
	}
	
	private final Handler handler = new Handler() {
		
		public void handleMessage(final Message msg) {
			int signalStrength = -100;
			int signalLevel = -1;
			
			if ((wifiObject.wifis == null) || (wifiObject.wifis.size() == 0)) {
				
			}else {

				TextView tv = null;
				ImageView iv = (ImageView)findViewById(R.id.iv_signal_strength);
				for(WifiScanItem result: wifiObject.wifis) {
					if (result.SSID.equals(wifiItem.SSID)) {
						signalStrength = result.leveldbm;
						signalLevel = result.level;
						
						if ((100-Math.abs(maxdbm) < (100-Math.abs(signalStrength)))) {
							maxdbm = signalStrength;
							startTimeMillis = System.currentTimeMillis();
						}
						
						tv = (TextView)findViewById(R.id.tv_max_dbm);
						tv.setText(maxdbm + " dBm");
						
						elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
						segElapsed = (int)((int)elapsedTimeMillis/1000F);
						
						tv = (TextView)findViewById(R.id.tv_time_max_dbm);
						tv.setText(segElapsed + " seg");
						
						switch (signalLevel) {
						case 0:
							iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_strength_1));
							break;
						case 1:
							iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_strength_2));
							break;
						case 2:
							iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_strength_3));
							break;
						case 3:
							iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_strength_4));
							break;
						case 4:
							iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_strength_5));
							break;
						}
					}
				}
				tv = (TextView)findViewById(R.id.tv_signal_strength);
				if (signalLevel == -1) {
					tv.setText(getResources().getString(R.string.lbl_no_signal));
					iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_strength_0));
				} else {
					tv.setText(signalStrength + "dbm");
				}
				refrescarGrafico(Math.abs(signalStrength));
			}
			wifiObject.startScan();
		}
	};	
	
	protected void onResume() {
		//wifiObject.lock();
		wifiObject.resume();
		super.onResume();
	}
	
	protected void onPause() {
		wifiObject.pause();
		//wifiObject.unlock();
		super.onPause();
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

	public void refrescarGrafico(int level) {
		lcSurvey.list.add((float)level);
		if (lcSurvey.list.size() > 20)
			lcSurvey.list.remove(0);
		lcSurvey.invalidate();
	}

	/*
	class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			actualizarEstado();
			mainWifi.startScan();
		}
	}
	*/
	
}
