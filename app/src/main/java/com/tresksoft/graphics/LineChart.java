package com.tresksoft.graphics;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class LineChart extends View{
	
	Paint paint = null;
	Paint paintBlur = null;
	
	Grid grid = null;
	public GridAxis axisY;
	
	public int maxY = 0;
	public int minY = 0;
	public int pasoY = 0;
	
	private int width = 0;
	private int height = 0;	
	
	public float axisYposX = 0.05f;
	public float axisYposY = 0;
	
	
	public ArrayList<Float> list = new ArrayList<Float>();
	public ArrayList<String> list_axis;
	float lineSize = 0;
	
	public LineChart(Context context) {
		super(context);
		init();
	}
	
	public LineChart(Context context, AttributeSet attribute) {
		super(context, attribute);
		init();
	}
	
	private void init() {
		paint = new Paint();
		paintBlur = new Paint();
		
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(Color.argb(200, 74, 138, 255));
		paint.setStrokeWidth(0.005f);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		
		paintBlur.set(paint);
		
		paint.setShader(new LinearGradient(0.40f, 0.0f, 0.60f, 1.0f,
				Color.rgb(255, 255, 255),
				Color.rgb(74, 138, 255),
				Shader.TileMode.CLAMP));
		
		paintBlur.setColor(Color.argb(235, 74, 138, 255));
		paintBlur.setStrokeWidth(0.01f);
		paintBlur.setStyle(Paint.Style.STROKE);

		
		//if (grid == null) {
			grid = new Grid(0.08f, 0.0f, 0.98f, 0.98f, 5, 16);
			grid.prepararGrid();
			
			axisY = new GridAxis();
			axisY.x = axisYposX;
			axisY.y = 0.98f;
			axisY.width = 0.90f;
			axisY.height = 0.98f;
			axisY.minValue = -100;
			axisY.maxValue = -30;
			axisY.step = 10;
			axisY.tag = "dBm";
		//}
	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w;
		height = h;
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	protected void onDraw(Canvas canvas) {
		float scaleW = (float) getWidth();
		float scaleH = (float) getHeight();
		float value = 0.0f;
		
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(scaleW, scaleH);
		
		grid.onDraw(canvas);
		lineSize = (grid.gridX2-grid.gridX1)/19.0f;
		
		if (list.size() > 0) {
			Path p = new Path();
			Path p2 = new Path();
			
			float x = grid.gridX1;
			float y = 0;
	
			for (int i = 0; i < list.size(); i++) {
				value = list.get(i)/100.0f;
				y = value;
				if ( i == 0) {
					p.moveTo(x, grid.gridY2);
					p2.moveTo(x, grid.gridY2-calculaY(y, grid.gridY2-grid.gridY1));
				}
				p.lineTo(x, grid.gridY2-calculaY(y, grid.gridY2-grid.gridY1));
				p2.lineTo(x, grid.gridY2-calculaY(y, grid.gridY2-grid.gridY1));
				
				x += lineSize;
			}
			p.lineTo(x - lineSize, grid.gridY2);
			
			canvas.drawPath(p, paint);
			canvas.drawPath(p2, paintBlur);
		}
		
		axisY.onDraw(canvas);
	}
	
	private float calculaY(float y, float size) {
		float retval = (((1.0f-Math.abs(y))*size)/0.7f);
		return retval;
	}

}
