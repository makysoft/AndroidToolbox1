package com.tresksoft.BatteryManager;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.format.Time;

import com.tresksoft.graphics.Grid;
import com.tresksoft.graphics.GridAxis;
import com.tresksoft.toolbox.data.Constants;

public class BatteryGridLevel extends Grid{
	
	public BatteryInfo desde;
	public BatteryInfo hasta;
	public ArrayList<BatteryInfo> log;
	
	private long fromAxisX = 0;
	private long toAxisX = 0;
	
	public BatteryGridLevel() {
		super();
	}

	public BatteryGridLevel(float x1, float y1, float x2, float y2) {
		super(x1, y1, x2, y2);
	}	
	
	public void analizarLog() {
		if (log != null && log.size() > 0) {
			desde = log.get(0);
			hasta = log.get(log.size()-1);
		}
	}
	
	public void prepararAxis() {
		analizarLog();
		
		int maxY = 100;
		int minY = 0;
		
		Time maxX = new Time();
		Time minX = new Time();
		Time current = new Time();
		
		minX.parse(desde.time);
		maxX.parse(hasta.time);
		
		long millis = maxX.toMillis(true) - minX.toMillis(true);
		long reglatres = (100*(millis-10))/millis;
		
		// Borrar los axis
		list_axis.clear();
		// Axis Y
		GridAxis axis = new GridAxis();
		axis.orientation = 1;
		
		for (int i = maxY; i >= minY; i -= 10) {
			axis.labels.add(i + "%");
		}
		axis.offsetLabel = 1;
		axis.showFirst = true;
		axis.showLast = true;
		axis.axisName = "Capacidad (%)";
		list_axis.add(axis);
		// Axis X
		axis = new GridAxis();
		axis.orientation = 2;
		for (int i = 0; i <= Constants.NUMERO_LINEAS_X; i++) {
			axis.labels.add(minX.hour + ":" + minX.minute + ":" + minX.second);
			minX.minute += 10;
			minX.normalize(true);
		}
		axis.offsetLabel = 4;
		axis.showFirst = true;
		axis.showLast = true;
		axis.axisName = "Hora";
		list_axis.add(axis);
		
		// Limites axis X
		fromAxisX = minX.toMillis(false);
		toAxisX = maxX.toMillis(false);
	}
	
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		float x1 = 0, y1 = 0;
		int level = 0;
		Time t = new Time();
		
		long t1 = 0;
		long t2 = 0;
		
		Path path = new Path();
		float spaceX = (0.80f * 1000)/60000;
		
		
		if (log != null) {
			boolean swInit = false;
			for (int i = log.size()-1; i >= 0; i--) {
				level = log.get(i).level;
				y1 = super.gridY2 - ((level * (super.gridY2-super.gridY1)) / 100);
				if (!swInit) {
					t.parse(log.get(i).time);
					t1 = t.toMillis(false);
					x1 = super.gridX2;
					path.moveTo(x1, y1);
					swInit = true;
				} else {
					t.parse(log.get(i).time);
					t1 = t.toMillis(false);
					x1 -= ((t2-t1) / 1000) * spaceX;
					path.lineTo(x1, y1);
				}
				t2 = t1;
			}
			
			x1 = super.gridX1;
			spaceX = (super.gridX2 - super.gridX1) / log.size();
			
			
			
			for (BatteryInfo info: log) {
				y1 = super.gridY2 - ((info.level * (super.gridY2-super.gridY1)) / 100);
				if (!swInit) {
					path.moveTo(super.gridX1, y1);
					swInit = true;
				}
				t = new Time();
				t.parse(info.time);
				long lt = t.toMillis(false);
				long coordX = ((toAxisX - lt) * 100) / (toAxisX - fromAxisX);
				
				path.lineTo(x1, y1);
				x1 += spaceX;
			}
			
			Paint paint = new Paint();
			paint.setColor(0xff555555);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(0.005f);
			
			canvas.drawPath(path, paint);
		}
	}
	
}
