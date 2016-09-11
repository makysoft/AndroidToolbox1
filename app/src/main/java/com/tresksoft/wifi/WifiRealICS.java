package com.tresksoft.wifi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.tresksoft.network.NetworkProfile;

public class WifiRealICS extends WifiReal{

	public WifiRealICS(Context context) {
		super(context);
	}
	
	public void setIP(NetworkProfile profile) {
		// Obtener wifi actual
		FactoryWifi factory = new FactoryWifi("real");
		Wifi wifiObject = (Wifi)factory.createFactory(this.context);
		WifiConfiguration wifiConf = null;
		int netid = wifiObject.getIDCurrentWifi();
		// Establecer IP
		if (netid > -1) {
			try {
				wifiConf = getCurrentWifi();
				if (profile.data.type_connection.equals("dhcp")) {
					setIpAssignment("DHCP", wifiConf);
				} else {
					setIpAssignment("STATIC", wifiConf);
					setIpAddress(InetAddress.getByName(profile.data.ip), calculaSubred(profile.data.mask), wifiConf);
					setGateway(InetAddress.getByName(profile.data.gateway), wifiConf);
					setDNS(InetAddress.getByName(profile.data.dns1), wifiConf);
				}
				updateNetwork(wifiConf);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		// Aplicar IP
	}
	
	public static void setIpAssignment(String assign, WifiConfiguration wifiConf) 
		throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		
		setEnumField(wifiConf, assign, "ipAssignment");
	}
	
	public static void setIpAddress(InetAddress addr, int prefixLength, WifiConfiguration wifiConf)
			throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException,
			NoSuchMethodException, ClassNotFoundException, InstantiationException, InvocationTargetException{
		
        Object linkProperties = getField(wifiConf, "linkProperties");
        if(linkProperties == null)return;
        Class laClass = Class.forName("android.net.LinkAddress");
        Constructor laConstructor = laClass.getConstructor(new Class[]{InetAddress.class, int.class});
        Object linkAddress = laConstructor.newInstance(addr, prefixLength);

        ArrayList mLinkAddresses = (ArrayList)getDeclaredField(linkProperties, "mLinkAddresses");
        mLinkAddresses.clear();
        mLinkAddresses.add(linkAddress);        
    }

    public static void setGateway(InetAddress gateway, WifiConfiguration wifiConf)
    		throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, 
    		ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException{
    	
        Object linkProperties = getField(wifiConf, "linkProperties");
        if(linkProperties == null)return;
        Class routeInfoClass = Class.forName("android.net.RouteInfo");
        Constructor routeInfoConstructor = routeInfoClass.getConstructor(new Class[]{InetAddress.class});
        Object routeInfo = routeInfoConstructor.newInstance(gateway);

        ArrayList mRoutes = (ArrayList)getDeclaredField(linkProperties, "mRoutes");
        mRoutes.clear();
        mRoutes.add(routeInfo);
    }

    public static void setDNS(InetAddress dns, WifiConfiguration wifiConf)
    		throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
        	Object linkProperties = getField(wifiConf, "linkProperties");
        	
        if(linkProperties == null)return;

        ArrayList<InetAddress> mDnses = (ArrayList<InetAddress>)getDeclaredField(linkProperties, "mDnses");
        mDnses.clear(); //or add a new dns address , here I just want to replace DNS1
        mDnses.add(dns); 
    }

    public static Object getField(Object obj, String name)
    		throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
    	
        Field f = obj.getClass().getField(name);
        Object out = f.get(obj);
        return out;
    }

    public static Object getDeclaredField(Object obj, String name)
    		throws SecurityException, NoSuchFieldException,
    		IllegalArgumentException, IllegalAccessException {
    	
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        Object out = f.get(obj);
        return out;
    }  

    public static void setEnumField(Object obj, String value, String name)
    		throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
    	
        Field f = obj.getClass().getField(name);
        f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
    }
	
	public void reconnect() {
		WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		manager.disconnect();
		manager.reconnect();
	}
}
