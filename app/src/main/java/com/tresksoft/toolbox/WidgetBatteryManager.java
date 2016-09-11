package com.tresksoft.toolbox;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class WidgetBatteryManager extends Service {
	
	private BroadcastReceiver myReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				// Recoger información de la batería
			}
			
		}
		
	};


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {
		super.onCreate();
		
		Intent intent = registerReceiver(myReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}
	
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(myReceiver);
	}
}
