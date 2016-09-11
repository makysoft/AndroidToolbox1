package com.tresksoft.BatteryManager;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.tresksoft.toolbox.ProcessApplication;

public class BatteryWidget extends AppWidgetProvider{

	private PendingIntent service = null;
	
	private RemoteViews remoteViews;
	BatteryWidgetView view;
	Bitmap bitmap;
	private Context context;
	
	public ProcessApplication app;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		m.cancel(service);
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		/*
		this.context = context;
		Toast.makeText(context, "Battery", Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(context, BatteryService.class);
		context.startService(intent);
		*/
		final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		final Calendar t = Calendar.getInstance();
		t.set(Calendar.MINUTE, 0);
		t.set(Calendar.SECOND, 0);
		t.set(Calendar.MILLISECOND, 0);
		
		final Intent i = new Intent(context, BatteryService.class);
		if (service == null) {
			service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
		}
		am.setRepeating(AlarmManager.RTC, t.getTime().getTime(), 1000*10, service);
	}
	
}
