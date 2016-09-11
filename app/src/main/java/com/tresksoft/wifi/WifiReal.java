package com.tresksoft.wifi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.provider.Settings;
import android.util.Log;

import com.tresksoft.network.NetworkProfile;
import com.tresksoft.toolbox.R;

public class WifiReal extends Wifi{

	public List<WifiConfiguration> redesConfiguradas = null;
	
	private WifiManager mainWifi = null;
	private WifiNetworkList networkList = null;
	
	private WifiReceiver receiverWifi = null;
	private WifiStatusReceiver receiverStatusWifi = null;
	
	private WifiLock lock = null;
	
	public WifiReal(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		mainWifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		networkList = new WifiNetworkList();
		redesConfiguradas = mainWifi.getConfiguredNetworks();
		
		receiverWifi = new WifiReceiver();
		receiverStatusWifi = new WifiStatusReceiver();
		
		//lock = mainWifi.createWifiLock("tresksoft_lock");
	}

	public void resume() {
		registerReceivers();
	}
	
	public void pause() {
		unregisterReceivers();
	}
	
	public void addWifi(WifiScanItem wifi, int security) {
		WifiLib.dialogAddWifi(this.context, mainWifi, wifi.SSID,security);
	}
	
	public void connect(WifiItem wifi) {
		mainWifi.enableNetwork(wifi.networkID, true);
	}
	
	public void disconnect(WifiItem wifi) {
		mainWifi.disableNetwork(wifi.networkID);
	}
	
	private void registerReceivers() {
		IntentFilter filter = new IntentFilter();
		
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		context.registerReceiver(receiverWifi, filter);
		
		filter = new IntentFilter();
		
		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(receiverStatusWifi, filter);
	}
	
	private void unregisterReceivers() {
		context.unregisterReceiver(receiverWifi);
		context.unregisterReceiver(receiverStatusWifi);
	}
	
	public class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			wifis.clear();
			List<ScanResult> scanResultList = mainWifi.getScanResults();
			if (scanResultList != null) {
				networkList.scanResult(mainWifi, scanResultList);
				wifis = networkList.getNetworkList();
			}
			handler.sendEmptyMessage(0);
		}
	}
	
	public class WifiStatusReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			//actualizarEstado();
			handler.sendEmptyMessage(1);
		}
	}
	
	public List<WifiItem> getWifisConfiguradas() {
		List<WifiItem> list = new ArrayList<WifiItem>();
		WifiItem item = null;
		
		List<WifiConfiguration> listWifi = mainWifi.getConfiguredNetworks();
		for (WifiConfiguration wifiConf: listWifi) {
			item = new WifiItem();
			item.BSSID = wifiConf.BSSID;
			item.SSID = wifiConf.SSID;
			item.priority = wifiConf.priority;
			item.networkID = wifiConf.networkId;
			list.add(item);
		}
		
		return list;
	}
	
	public void startScan() {
		WifiLib.inicializarWifi(context, mainWifi);
		if (!startScanActive())
			mainWifi.startScan();
	}
	
	public boolean isWifiEnabled() {
		return mainWifi.isWifiEnabled();
	}
	
	public boolean startScanActive() {
		boolean retval = false;
		Method startScanActive;
		try {
			startScanActive = mainWifi.getClass().getMethod("startScanActive");
			try {
				startScanActive.invoke(mainWifi);
				retval = true;
				Log.v("INFO", "startScanActive");
			}catch(IllegalArgumentException e) {
				e.printStackTrace();
			}catch(IllegalAccessException e) {
				e.printStackTrace();
			}catch(InvocationTargetException e) {
				e.printStackTrace();
			}
		}catch(SecurityException e) {
			e.printStackTrace();
		}catch(NoSuchMethodException e) {
			e.printStackTrace();
		}
		return (retval);
	}
	
	public String getConnectionInfo() {
		String retval = "";
		WifiInfo wifiInfo = mainWifi.getConnectionInfo();
		if (wifiInfo != null) {
			if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
				retval = wifiInfo.getSSID();
			} else if (wifiInfo.getSupplicantState() == SupplicantState.ASSOCIATING){
				retval = context.getResources().getString(R.string.wifi_status_associating);
			} else {
				retval = wifiInfo.getSupplicantState().toString();
			}
		} else {
			retval = context.getResources().getString(R.string.wifi_status_no_associated);
		}
		return retval;
	}
	
	public int getIDCurrentWifi() {
		return mainWifi.getConnectionInfo().getNetworkId();
	}
	
	public WifiConfiguration getCurrentWifi() {
		int id = getIDCurrentWifi();
		WifiConfiguration retval = null;
		for (WifiConfiguration conf: redesConfiguradas) {
			if (conf.networkId == id) {
				retval = conf;
			}
		}
		return retval;
	}
	
	public void updateNetwork(WifiConfiguration wifiConf) {
		mainWifi.updateNetwork(wifiConf);
	}
	
	public void lock() {
		lock.acquire();
	}
	
	public void unlock() {
		lock.release();
	}
	
	public void setIP(NetworkProfile profile) {
		ContentResolver cr = context.getContentResolver();
		if (profile.data.type_connection.equals("dhcp")) {
			Settings.System.putString(cr, Settings.System.WIFI_USE_STATIC_IP, "0");
		} else {
			Settings.System.putString(cr, Settings.System.WIFI_USE_STATIC_IP, "1");
			Settings.System.putString(cr, Settings.System.WIFI_STATIC_IP, profile.data.ip);
			Settings.System.putString(cr, Settings.System.WIFI_STATIC_NETMASK, profile.data.mask);
			Settings.System.putString(cr, Settings.System.WIFI_STATIC_GATEWAY, profile.data.gateway);
			Settings.System.putString(cr, Settings.System.WIFI_STATIC_DNS1, profile.data.dns1);
			Settings.System.putString(cr, Settings.System.WIFI_STATIC_DNS2, profile.data.dns2);
		}
	}
	
	public void reconnect() {
		WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		manager.reassociate();
	}
	
	/**
	 * Devuelve la IP actual de la conexión wifi
	 */
	public String getIP() {

		String format_ip = "";
		
		WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		
		int ip = info.getIpAddress();
		
		return formatIP(ip);
	}	
	
	public String getGateway() {
		WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo info = manager.getDhcpInfo();
		return formatIP(info.gateway);
	}
	
	public String getDNS1() {
		WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo info = manager.getDhcpInfo();
		return formatIP(info.dns1);
	}
	
	public String getDNS2() {
		WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo info = manager.getDhcpInfo();
		return formatIP(info.dns2);
	}		
	
	public String getNetMask() {
		WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo info = manager.getDhcpInfo();
		return formatIP(info.netmask);
	}
	
	public String formatIP(int ip) {
		int ip3 = ip & 0xff;
		int ip2 = ip >> 8 & 0xff;
		int ip1 = ip >> 16 & 0xff;
		int ip0 = ip >> 24 & 0xff;
		
		return String.valueOf(ip3) + "." + String.valueOf(ip2) + "." + String.valueOf(ip1) + "." + String.valueOf(ip0);
	}
	
	public String getBSSID() {
		return mainWifi.getConnectionInfo().getBSSID();
	}
	
	public int getLinkSpeed() {
		return mainWifi.getConnectionInfo().getLinkSpeed();
	}
	
	public String getSSID() {
		return mainWifi.getConnectionInfo().getSSID();
	}
	
	public short calculaSubred(String mask) {
		String[] tokens = mask.split("[.]");
		short retval = 0;
		for(String token: tokens) {
			String t = Integer.toBinaryString(Integer.valueOf(token)).replace("0", "");
			retval += t.length();
		}
		return retval;
	}
	
}
