package com.tresksoft.WifiManager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.os.Handler;

import com.tresksoft.network.NetworkProfile;

public class Wifi {
	
	private String[] frecuencias = {"2412","2417", "2422", "2427", "2432", "2437", "2442", "2447", "2452", "2457", "2462", "2467", "2472", "2484"};
	private String[] canales = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"};

	public List<WifiScanItem> wifis = null;
	
	Context context = null;
	Handler handler = null;
	
	public Wifi(Context context) {
		this.context = context;
		
		wifis = new ArrayList<WifiScanItem>();
	}
	
	public String getCanal(int frec) {
		int i = 0;
		String canal = "null";
		for(String frecuencia: frecuencias) {
			if(frecuencia.equals(String.valueOf(frec))) {
				canal = canales[i];
			}
			i++;
		}
		return canal;
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	public void resume() {
	}
	
	public void pause() {
	}
	
	public void startScan() {
	}
	
	public void addWifi(WifiScanItem wifi, int security) {
	}
	
	public boolean isWifiEnabled() {
		return false;
	}
	
	public void connect(WifiItem wifi) {
	}
	
	public String getConnectionInfo() {
		return null;
	}
	
	public int getIDCurrentWifi() {
		return -1;
	}
	
	public void disconnect(WifiItem wifi) {
	}
	
	public List<WifiItem> getWifisConfiguradas() {
		return new ArrayList<WifiItem>();
	}
	
	public void lock() {
	}
	
	public void unlock() {
	}
	
	public void setIP(NetworkProfile profile) {
		
	}
	
	public String getIP() {
		return "";
	}
	
	public String getGateway() {
		return "";
	}
	
	public String getDNS1() {
		return "";
	}
	
	public String getDNS2() {
		return "";
	}	
	
	public String getNetMask() {
		return "";
	}		
	
	public short calculaSubred(String mask) {
		return 0;
	}
	
	public void reconnect() {
		
	}
	
	public String getSSID() {
		return "TERMINUS";
	}
	
	public String getBSSID() {
		return "00:11:22:33:44:55";
	}
	
	public int getLinkSpeed() {
		return -1;
	}	
	
	public WifiConfiguration getCurrentWifi() {
		return null;
	}
}
