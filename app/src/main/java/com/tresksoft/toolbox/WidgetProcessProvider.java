package com.tresksoft.toolbox;

import android.app.ActivityManager.MemoryInfo;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mobeng.libs.LibProcessManager;
import com.tresksoft.toolbox.data.CTamanhoBytes;

public class WidgetProcessProvider extends AppWidgetProvider{

	private RemoteViews remoteViews;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals("com.tresksoft.processmanager.WIDGET_CLICK")) {
			LibProcessManager.matarProceso(context);
			//Obtenemos la memoria liberada por el proceso
			MemoryInfo memFinal;
			memFinal = LibProcessManager.getMemoryInfo(context);
			SystemClock.sleep(1000);
			Toast.makeText(context, context.getResources().getString(R.string.process_mem_free) + " " + (new CTamanhoBytes(memFinal.availMem)).toString(), Toast.LENGTH_LONG).show();
		}else{
			super.onReceive(context, intent);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_process);
		
		Intent intent = new Intent(context, WidgetProcessProvider.class);
		intent.setAction("com.tresksoft.processmanager.WIDGET_CLICK");
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_button, actionPendingIntent);
		
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}
	
}
