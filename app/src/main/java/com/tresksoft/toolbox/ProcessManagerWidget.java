package com.tresksoft.toolbox;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.tresksoft.toolbox.data.ColeccionRunningProcess;

public class ProcessManagerWidget extends AppWidgetProvider{
	
	public static String MY_WIDGET_UPDATE = "MY_OWN_WIDGET_UPDATE";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		//super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		//super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		//super.onEnabled(context);
	}

	public void onReceive(Context context, Intent intent){
		super.onReceive(context, intent);	
		if(MY_WIDGET_UPDATE.equals(intent.getAction())){
			   AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			   updateAppWidget(context, appWidgetManager);
		}
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
	}
	
	public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager) {
		ProcessApplication app = (ProcessApplication) context.getApplicationContext();
		Funciones func = new Funciones(context);
		ColeccionRunningProcess cp = func.getRunningProcess();
		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		updateViews.setTextViewText(R.id.widget_num_process, String.valueOf(cp.getRunningProcessToKill()));
		appWidgetManager.updateAppWidget(app.getAppWidgetId(), updateViews);
	}

	
}
