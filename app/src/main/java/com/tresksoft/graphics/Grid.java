package com.tresksoft.graphics;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;

import com.tresksoft.toolbox.data.Constants;

public class Grid {

	/**
	 *  Variables staticas
	 */
	
	// Chart
	private static float XCHART = 0.02f;
	private static float YCHART = 0.02f;
	private static float WIDTHCHART = 0.90f;
	private static float HEIGHTCHART = 0.50f;
	
	/**
	 * Variables
	 */
	
	// Tamanho del grid
	public float topX, topY, width, height;
	public int numLinesX;
	public int numLinesY;
	// Acceso desde fuera a la posicion del grid
	public float gridX1, gridY1, gridX2, gridY2;
	
	public ArrayList<GridAxis> list_axis = new ArrayList<GridAxis>();
	
	// Estilos del grid
	public Paint estiloGrid;
	public Paint estiloAxis;
	public Paint estiloMarco;
	private Paint estiloMarcoFondo;
	private Paint estiloAxisLeft;
	private Paint estiloAxisRight;
	
	public Grid() {
		this(XCHART, YCHART, WIDTHCHART, HEIGHTCHART);
	}
	
	public Grid(float x1, float y1, float width, float height) {
		this(x1, y1, width, height, Constants.NUMERO_LINEAS_X, Constants.NUMERO_LINEAS_Y);
	}
	
	public Grid(float x1, float y1, float width, float height, int numLinesX, int numLinesY) {
		this.topX = x1;
		this.topY = y1;
		this.width = width;
		this.height = height;
		this.numLinesX = numLinesX;
		this.numLinesY = numLinesY;
		
		// Inicializa el Grid
		prepararGrid();
		

		
	}	
	
	public void onDraw(Canvas canvas) {
		dibujarGrid(canvas);
	}
	
	public void prepararGrid() {
		estiloGrid = new Paint();
		estiloGrid.setFlags(Paint.ANTI_ALIAS_FLAG);
		estiloGrid.setStrokeWidth(0.001f);
		estiloGrid.setShader(new LinearGradient(0.40f, 0.0f, 0.60f, 1.0f,
												Color.rgb(0xf0, 0xf5, 0xf0),
												Color.rgb(0x30, 0x31, 0x30),
												Shader.TileMode.CLAMP));
		
		estiloMarcoFondo = new Paint();
		estiloMarcoFondo.setColor(Color.parseColor("#eafdd6"));
		
		estiloMarco = new Paint();
		estiloMarco.setFlags(Paint.ANTI_ALIAS_FLAG);
		estiloMarco.setColor(Color.BLACK);
		estiloMarco.setStyle(Paint.Style.STROKE);
		
		estiloAxis = new Paint();
		estiloAxis.setFlags(Paint.ANTI_ALIAS_FLAG);
		estiloAxis.setColor(Color.BLACK);
		estiloAxis.setTextSize(0.03f);
		estiloAxis.setTextAlign(Paint.Align.CENTER);
		estiloAxis.setTypeface(Typeface.DEFAULT_BOLD);
		
		estiloAxisLeft = new Paint();
		estiloAxisLeft.setFlags(Paint.ANTI_ALIAS_FLAG);
		estiloAxisLeft.setColor(Color.BLACK);
		estiloAxisLeft.setTextSize(0.03f);
		estiloAxisLeft.setTextAlign(Paint.Align.LEFT);
		estiloAxisLeft.setTypeface(Typeface.DEFAULT_BOLD);
		
		estiloAxisRight = new Paint();
		estiloAxisRight.setFlags(Paint.ANTI_ALIAS_FLAG);
		estiloAxisRight.setColor(Color.BLACK);
		estiloAxisRight.setTextSize(0.03f);
		estiloAxisRight.setTextAlign(Paint.Align.RIGHT);
		estiloAxisRight.setTypeface(Typeface.DEFAULT_BOLD);		
	}
	
	private void dibujarGrid(Canvas canvas) {
		// Dibujamos marco exterior
		//RectF marco = new RectF(topX, topY, topX + width, topY + height);
		//canvas.drawRect(marco, estiloMarco);
		
		// Dibujamos axis vertical
		float axisVX1, axisVY1, axisVX2, axisVY2;
		
		axisVX1 = topX + 0.02f;
		axisVY1 = topY + 0.02f;
		axisVX2 = topX + 0.02f;
		axisVY2 = height - 0.02f;
		
		// Espacio vertical entre lineas horizontales
		float spaceHeight = (axisVY2 - axisVY1) / numLinesY;
		
		// Dibujamos axis horizontal
		float axisHX1, axisHY1, axisHX2, axisHY2;
		
		axisHX1 = axisVX2;
		axisHY1 = axisVY2;
		axisHX2 = width;
		axisHY2 = height;
		
		float spaceWidth = (axisHX2 - axisVX2) / numLinesX;
			
		// Dibujamos grid
		gridX1 = axisVX2;
		gridY1 = axisVY1;
		gridX2 = axisHX2;
		gridY2 = axisHY1;
		
		// Marco del grid
		RectF chart = new RectF(gridX1, gridY1, gridX2, gridY2);
		canvas.drawRect(chart, estiloMarcoFondo);
		
		chart = new RectF(gridX1, gridY1, gridX2, gridY2);
		canvas.drawRect(chart, estiloMarco);
		
		float x1, y1, x2, y2;
		
		x1 = gridX1;
		y1 = gridY1;
		x2 = gridX1;
		y2 = gridY2;
		
		// Dibuja lineas verticales
		boolean swInit = false;
		for (int i = 0; i <= numLinesX; i++) {
			if(swInit) {
				x1 += spaceWidth;
				x2 += spaceWidth;
			} else {
				swInit = true;
			}
			canvas.drawLine(x1, y1, x2, y2+0.005f,estiloGrid);
		}
		
		x1 = gridX1;
		y1 = gridY2;
		y2 = gridY2;

		// Dibuja lineas horizontales
		swInit = false;
		for (int i = 0; i <= numLinesY; i++) {
			if (swInit) {
				y1 -= spaceHeight;
				y2 -= spaceHeight;
			} else {
				swInit = true;
			}
			canvas.drawLine(x1-0.01f, y1, x2, y2, estiloGrid);
		}
	}	
	
	public void prepararAxis() {
		
	}
}
