package com.tresksoft.wifi;

import android.content.Context;
import android.os.Build;
import android.os.Handler;

public class FactoryWifi {

	private String type = null;
	
	public FactoryWifi(String type) {
		this.type = type;
	}
	
	public Wifi createFactory(Context context) {
		if(type.equals("real")){
			if (Build.VERSION.SDK_INT >= 14)
				return new WifiRealICS(context);
			else
				return new WifiReal(context);
		}else if(type.equals("emulador")){
			return new WifiSimulator(context);
		}else{
			return new Wifi(context);
		}
	}
	
}
