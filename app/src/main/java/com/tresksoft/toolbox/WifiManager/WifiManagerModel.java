package com.tresksoft.toolbox.WifiManager;

import android.content.Context;

/**
 * Created by Salva on 19/9/16.
 */
public class WifiManagerModel {

    private final String typeDevice = "real";
    FactoryWifi factory;
    private Wifi wifiObject;


    public WifiManagerModel(Context context) {
        factory = new FactoryWifi(typeDevice);
        wifiObject = (Wifi)factory.createFactory(context);
    }

    public int getWifisAvailable() {

        if(wifiObject.isWifiEnabled()) {
            wifiObject.startScan();
            return 0;
        } else {
            return -1;
        }
    }
}
