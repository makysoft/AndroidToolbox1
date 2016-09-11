package com.tresksoft.wifi;

import java.util.ArrayList;
import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiNetworkList {

	private List<WifiScanItem> scanResultList;
	
	public List<WifiScanItem> getNetworkList() {
		return scanResultList;
	}
	
	public void initializeEmulator() {
		scanResultList = new ArrayList<WifiScanItem>();	
		
		WifiScanItem item = new WifiScanItem();
		
		item.SSID = "Prueba1";
		item.capabilities = "[WPA2-PSK-CCMP]";
		item.frequency = 2422;
		item.leveldbm = -40;
		item.level = 5;
		item.BSSID = "01:02:03:04:05:06";
		
		scanResultList.add(item);
		

		item.SSID = "Prueba2";
		item.capabilities = "[WEP]";
		item.frequency = 2422;
		item.leveldbm = -80;
		item.level = 1;
		item.BSSID = "01:02:03:04:05:06";

		scanResultList.add(item);
	}
	
	public void scanResult(WifiManager manager, List<ScanResult> wifis) {
		scanResultList = new ArrayList<WifiScanItem>();
		WifiScanItem item = null;
		
		for (ScanResult scanResult: wifis) {
			item = new WifiScanItem();
			
			item.BSSID = scanResult.BSSID;
			item.capabilities = scanResult.capabilities;
			item.frequency = scanResult.frequency;
			item.leveldbm = scanResult.level;
			item.level = manager.calculateSignalLevel(item.leveldbm, 5);
			item.SSID = scanResult.SSID;
			scanResultList.add(item);
		}
	}
	
}
