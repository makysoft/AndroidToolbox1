package com.tresksoft.graphics;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class GridAxis {

	// Variables
	public int maxValue = 0;
	public int minValue = 0;
	public int step = 0;
	public String tag = "";
	
	public float x = 0;
	public float y = 0;
	public float offsetY = 0;
	public float width = 0;
	public float height = 0;
	
	public Paint estiloLetras;
	
	public int orientation;
	public ArrayList<String> labels = new ArrayList<String>();
	public int offsetLabel;
	public boolean showFirst;
	public boolean showLast;
	public String axisName;
	
	public GridAxis() {
		inicializar();
	}
	
	public void inicializar() {
		estiloLetras = new Paint();
		estiloLetras.setFlags(Paint.ANTI_ALIAS_FLAG);
		estiloLetras.setColor(Color.DKGRAY);
		estiloLetras.setTextSize(0.03f);
		estiloLetras.setTextAlign(Paint.Align.CENTER);
		estiloLetras.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	public void onDraw(Canvas canvas) {
		int minValue = 0;
		int maxValue = 0;

		if (this.minValue > this.maxValue) {
			minValue = this.maxValue;
			maxValue = this.minValue;
		} else {
			minValue = this.minValue;
			maxValue = this.maxValue;
		}
		
		int numLineas = (maxValue - minValue) / step;
		float espacioEntreLineas = this.height/numLineas;
		
		for (int i = 1; i < numLineas; i++) {
			canvas.drawText(String.valueOf(minValue + (step*i)), this.x, this.y - offsetY - (espacioEntreLineas * i), estiloLetras);
		}
		canvas.drawText("(" + this.tag + ")", this.x, 0.05f, estiloLetras);
	}
	
}
