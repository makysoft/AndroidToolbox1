package com.tresksoft.toolbox.BatteryManager;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.tresksoft.toolbox.Home.ProcessApplication;

public class Battery extends View{

	private Context context;
	
	private BatteryGridLevel batteryGrid;
	
	private ProcessApplication app;
	
	public Battery(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	public Battery(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	
	private void init() {
		app = (ProcessApplication) context.getApplicationContext();
		
		//BatteryHistory bh = new BatteryHistory(app.log, 60);
		
		// Create factory
		BatteryFactory factory = new BatteryFactory("gridLevelCustomWH");
		//BatteryFactory factory = new BatteryFactory("gridGeneric");
		batteryGrid = (BatteryGridLevel) factory.createGrid();
		batteryGrid.log = app.log;
		batteryGrid.analizarLog();		
		batteryGrid.prepararAxis();
	}
	
	protected void onDraw(Canvas canvas) {

		/**
		 *  Recupera datos del LOG
		 *
		 *  app.log => ArraList<BatteryInfo>
		 */
		
		
		float scale = (float) getWidth();
		
		batteryGrid.log = app.log;
		
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(scale, scale);
		
		
		batteryGrid.onDraw(canvas);

		canvas.restore();
	}
}
