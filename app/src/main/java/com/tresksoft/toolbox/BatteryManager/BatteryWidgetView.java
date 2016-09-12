package com.tresksoft.toolbox.BatteryManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.tresksoft.toolbox.Home.ProcessApplication;
import com.tresksoft.toolbox.R;

public class BatteryWidgetView extends View{

	private ProcessApplication app;
	private Bitmap bitmap;
	private Paint p;
	
	public BatteryWidgetView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		app = (ProcessApplication) context.getApplicationContext();
		init();
	}

	public BatteryWidgetView(Context context, AttributeSet attrs) {
		super(context, attrs);
		app = (ProcessApplication) context.getApplicationContext();
		init();
	}
	
	public void init() {
		int battery = 0;
		if (app.batInfo == null) {
			battery = R.drawable.ic_battery_empty;
		} else if (app.batInfo.level >= 96 && app.batInfo.level <= 100) {
			battery = R.drawable.ic_battery_full;
		} else if (app.batInfo.level >= 84 && app.batInfo.level < 96) {
			battery = R.drawable.ic_battery_7;
		} else if (app.batInfo.level >= 72 && app.batInfo.level < 84) {
			battery = R.drawable.ic_battery_6;
		} else if (app.batInfo.level >= 60 && app.batInfo.level < 72) {
			battery = R.drawable.ic_battery_5;
		} else if (app.batInfo.level >= 48 && app.batInfo.level < 60) {
			battery = R.drawable.ic_battery_4;
		} else if (app.batInfo.level >= 36 && app.batInfo.level < 48) {
			battery = R.drawable.ic_battery_3;
		} else if (app.batInfo.level >= 24 && app.batInfo.level < 36) {
			battery = R.drawable.ic_battery_2;
		} else if (app.batInfo.level >= 12 && app.batInfo.level < 24) {
			battery = R.drawable.ic_battery_1;
		} else if (app.batInfo.level >= 0 && app.batInfo.level < 12){
			battery = R.drawable.ic_battery_empty;
		}
		bitmap = BitmapFactory.decodeResource(getResources(),battery);
		
		p = new Paint();
		p.setColor(Color.WHITE);
		p.setTextSize(getResources().getDimension(R.dimen.widget_capacity_textsize));
		p.setTextAlign(Paint.Align.CENTER);
		p.setShadowLayer(2, 2, 2, Color.BLACK);
		p.setAntiAlias(true);
		p.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	protected void onDraw(Canvas canvas) {
		String batInfo;
		String voltage;
		String temp;
		
		if (app.batInfo != null) {
			batInfo = String.valueOf(app.batInfo.level) + "%";
			voltage = String.valueOf(app.batInfo.voltage) + "mW";
			temp = String.valueOf(app.batInfo.temp/10) + " " + getResources().getString(R.string.battery_temp);
		}else{
			batInfo = "Loading...";
			voltage = "-- mW";
			temp = "-- " + getResources().getString(R.string.battery_temp);
		}
		
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.drawText(batInfo, getResources().getDimension(R.dimen.widget_capacity_text_x)
							   , getResources().getDimension(R.dimen.widget_capacity_text_y)
							   , p);
		p.setTextSize(getResources().getDimension(R.dimen.widget_default_textsize));
		p.setShadowLayer(1, 1, 1, Color.BLACK);
		p.setTextAlign(Paint.Align.LEFT);
		canvas.drawText(voltage, getResources().getDimension(R.dimen.widget_mw_text_x)
							   , getResources().getDimension(R.dimen.widget_mw_text_y)
							   , p);
		canvas.drawText(temp, getResources().getDimension(R.dimen.widget_temp_text_x)
							, getResources().getDimension(R.dimen.widget_temp_text_y)
							, p);		
		canvas.restore();
	}
}
