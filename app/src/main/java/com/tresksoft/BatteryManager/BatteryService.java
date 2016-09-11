package com.tresksoft.BatteryManager;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.BatteryManager;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.tresksoft.toolbox.ProcessApplication;
import com.tresksoft.toolbox.R;

public class BatteryService extends Service {

	BroadcastReceiver receiver;
	RemoteViews remoteViews;
	BatteryWidgetView view;
	Bitmap bitmap;
	
	ComponentName myComponentName;
	AppWidgetManager manager;
	
	ProcessApplication app;
	
	public void onStart(Intent intent, int startId) {
		app = (ProcessApplication) this.getApplication();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		receiver = new BatInfoReceiver();
		registerReceiver(receiver,filter);
		remoteViews = new RemoteViews(this.getPackageName(), R.layout.widget_battery);


		myComponentName = new ComponentName(this, BatteryWidget.class);
        manager = AppWidgetManager.getInstance(this);
        
        actualizaWidget();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public class BatInfoReceiver extends BroadcastReceiver {
		private String action = null;
		public void onReceive(Context context, Intent intent) {
			action = intent.getAction();
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				BatteryInfo batInfo = new BatteryInfo();
				batInfo.level = intent.getIntExtra("level", 0);
				batInfo.voltage = intent.getIntExtra("voltage", 0);
				batInfo.temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1);
				app.batInfo = batInfo;
				actualizaWidget();
			}
		}
	}
	
	private void actualizaWidget() {
		view = new BatteryWidgetView(this);
//		view.measure( (int)getResources().getDimension(R.dimen.widget_battery_layout_width)
//					, (int)getResources().getDimension(R.dimen.widget_battery_layout_height));
		//view.measure(36, 36);
		view.layout(0, 0, (int)getResources().getDimension(R.dimen.widget_battery_layout_width)
						, (int)getResources().getDimension(R.dimen.widget_battery_layout_height));
		view.setDrawingCacheEnabled(true);
		bitmap = view.getDrawingCache();
		remoteViews.setImageViewBitmap(R.id.widget_button, bitmap);
		
		manager.updateAppWidget(myComponentName, remoteViews);
	}

}