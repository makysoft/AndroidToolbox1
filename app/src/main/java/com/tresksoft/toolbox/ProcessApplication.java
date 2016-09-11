package com.tresksoft.toolbox;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.tresksoft.batterymanager.BatteryInfo;
import com.tresksoft.network.NetworkProfile;
import com.tresksoft.toolbox.data.CProcess;
import com.tresksoft.wifi.WifiScanItem;

public class ProcessApplication extends Application{

	// Información sobre la API
	public int apiLevel = Build.VERSION.SDK_INT;
	
	private static Context context;
	
	public ColeccionProcesos coleccionProcesosDB;
	
	public String currentApplication;
	
	public ArrayList<CProcess> procesos;
	public int appWidgetId;
	
	// Wifi Manager
	public String typeDevice;
	public WifiScanItem wifiItem;
	
	// Network Manager
	public NetworkProfile network_profile;
	
	// Battery Manager
	public ArrayList<BatteryInfo> log;
	public BatteryInfo batInfo;
	
	public ProcessApplication() {
		super();
	}
	
	public void onCreate(){
		ProcessApplication.context = getApplicationContext();
		super.onCreate();
	}
	
	public void onTerminate(){
		super.onTerminate();
	}
	
	public static Context getAppContext(){
		return ProcessApplication.context;
	}
	
	public void setColeccionProcesosDB(ColeccionProcesos coleccionProcesosDB) {
		this.coleccionProcesosDB = coleccionProcesosDB;
	}
	
	public ColeccionProcesos getColeccionProcesosDB() {
		return this.coleccionProcesosDB;
	}
	
	public void setProcesos(ArrayList<CProcess> procesos){
		this.procesos = procesos;
	}
	
	public ArrayList<CProcess> getProcesos(){
		return this.procesos;
	}
	
	public void setAppWidgetId(int appWidgetId) {
		this.appWidgetId = appWidgetId;
	}
	
	public int getAppWidgetId() {
		return this.appWidgetId;
	}
}
