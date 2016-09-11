package com.tresksoft.toolbox;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class WidgetConfigure extends Activity{
	
	Button configOkButton;
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setResult(RESULT_CANCELED);
		setContentView(R.layout.widget_layout_config);
		
		configOkButton = (Button)findViewById(R.id.widget_config_ok);
		configOkButton.setOnClickListener(configOkButtonOnClickListener);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras != null){
			
			mAppWidgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID
					); 
			ProcessApplication app = (ProcessApplication) getApplication();
			app.setAppWidgetId(mAppWidgetId);
			
		}
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
			finish();
		} 
	}
	
	private Button.OnClickListener configOkButtonOnClickListener = new OnClickListener() {
			public void onClick(View arg0){
				final Context context = WidgetConfigure.this;
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//				ProcessManagerWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId, func.getRunningProcess());
				ProcessManagerWidget.updateAppWidget(context, appWidgetManager);
				Toast.makeText(context, "WidgetConfigure.onClick " + mAppWidgetId, Toast.LENGTH_SHORT).show();
				
				
				Intent intent = new Intent(ProcessManagerWidget.MY_WIDGET_UPDATE);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
				AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.add(Calendar.SECOND, 10);
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 20*1000, pendingIntent);
				
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
				setResult(RESULT_OK, resultValue);
				finish();
			}
	};
}
