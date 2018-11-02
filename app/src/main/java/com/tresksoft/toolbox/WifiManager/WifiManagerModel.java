package com.tresksoft.toolbox.WifiManager;

import android.content.Context;
import android.os.Handler;

/**
 * Created by Salva on 19/9/16.
 */
public class WifiManagerModel {

    private final String typeDevice = "real";
    FactoryWifi factory;
    private Wifi wifiObject;


    public WifiManagerModel(Context context, Handler handler) {
        factory = new FactoryWifi(typeDevice);
        wifiObject = (Wifi)factory.createFactory(context);
        wifiObject.setHandler(handler);
    }

    public void getWifisAvailable() {

        if(wifiObject.isWifiEnabled()) {
            wifiObject.startScan();
        }
    }
}
