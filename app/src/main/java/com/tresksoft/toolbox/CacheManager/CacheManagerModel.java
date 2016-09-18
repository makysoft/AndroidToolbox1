package com.tresksoft.toolbox.CacheManager;

import android.content.Context;

import com.mobeng.libs.LibAppManager;
import com.tresksoft.toolbox.data.CAplicacion;

import java.util.List;

/**
 * Created by Salva on 19/9/16.
 */
public class CacheManagerModel {

    private final Context context;

    public CacheManagerModel(Context context) {
        this.context = context;
    }

    public long getCacheTotal() {
        LibAppManager libApp = new LibAppManager();
        List<CAplicacion> apps = libApp.getAppsInstall(context);
        long cache = 0;

        for(CAplicacion app: apps) {
            CAplicacion retapp = null;
            try {
                retapp = libApp.getAppCache(context.getPackageManager(), app.getPaquete());
                if (retapp != null && retapp.getCacheAplicacion().bytes > 0) {
                    cache += retapp.getCacheAplicacion().bytes;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return cache;
    }
}
