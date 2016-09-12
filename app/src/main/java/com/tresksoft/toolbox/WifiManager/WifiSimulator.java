package com.tresksoft.toolbox.WifiManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.content.Context;
import android.os.Handler;

public class WifiSimulator extends Wifi{
	
	private Timer timer = null;
	
	public WifiSimulator(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void resume() {
		
	}
	
	public void pause() {
		
	}
	
	public void startScan() {
		// Inicia timer
		mHandler.removeCallbacks(mUpdateTimeTask);
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}
	
	public List<WifiItem> getWifisConfiguradas() {
		List<WifiItem> list = new ArrayList<WifiItem>();
		
		WifiItem item = new WifiItem();
		item.BSSID = "00:11:22:33:44:55";
		item.SSID = "Red Configurada 1";
		list.add(item);
		
		item = new WifiItem();
		item.BSSID = "01:02:03:04:05:06";
		item.SSID = "Prueba1";
		list.add(item);
		
		return list;
	}
	
	private Handler mHandler = new Handler();
	
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			WifiScanItem item = new WifiScanItem();
			
			item.SSID = "Prueba1";
			item.capabilities = "[WPA2-PSK-CCMP]";
			item.frequency = 2422;
			item.leveldbm = -40;
			item.level = 1;
			item.BSSID = "01:02:03:04:05:06";
			
			wifis.add(item);
			
			item = new WifiScanItem();

			item.SSID = "Prueba2";
			item.capabilities = "[WEP]";
			item.frequency = 2422;
			item.leveldbm = -80;
			item.level = 5;
			item.BSSID = "01:02:03:04:05:06";

			wifis.add(item);
			
			handler.sendEmptyMessage(0);
		}
	};
}
