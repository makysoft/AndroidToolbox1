package com.tresksoft.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChart extends View{

	public long valorPorcion = 10;
	public int tamanho = 60;
	
	public PieChart(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public PieChart(Context context, AttributeSet attribute) {
		super(context, attribute);
		// TODO Auto-generated constructor stub
	}
	
	protected void onDraw(Canvas canvas) {
		RectF rect = new RectF();
		rect.set(1, 1, this.tamanho, this.tamanho);
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(0.5f);
		Paint paintGray = new Paint();
		paintGray.setColor(Color.LTGRAY);
		paintGray.setAntiAlias(true);
		paintGray.setStyle(Paint.Style.FILL);
		paintGray.setStrokeWidth(0.5f);		
		Paint mLinePaints = new Paint();
		mLinePaints.setAntiAlias(true);
        mLinePaints.setStyle(Paint.Style.STROKE);
        mLinePaints.setColor(0xffffffff);
        mLinePaints.setStrokeWidth(0.5f);
        Paint mLinePaints2 = new Paint();
		mLinePaints2.setAntiAlias(true);
        mLinePaints2.setStyle(Paint.Style.STROKE);
        mLinePaints2.setColor(0xffffffff);
        mLinePaints2.setStrokeWidth(2.0f);

        canvas.drawArc(rect, 0, 360, true, paintGray);
		canvas.drawArc(rect, 0, 360, true, mLinePaints);
		canvas.drawArc(rect, 270, valorPorcion, true, paint);
		canvas.drawArc(rect, 270, valorPorcion, true, mLinePaints2);
	}

	
}
