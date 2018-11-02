package com.tresksoft.toolbox.Home;

import android.content.Context;
import android.os.Handler;

import com.tresksoft.toolbox.ApplicationManager.AppManagerModel;
import com.tresksoft.toolbox.CacheManager.CacheManagerModel;
import com.tresksoft.toolbox.Move2SDManager.Move2SDModel;
import com.tresksoft.toolbox.ProccessManager.ProcessManagerModel;
import com.tresksoft.toolbox.WifiManager.WifiManagerModel;

/**
 * Created by Salva on 13/9/16.
 */
public class HomeModel {

    private Move2SDModel move2SDModel;
    private CacheManagerModel cacheManagerModel;
    private ProcessManagerModel processManagerModel;
    private AppManagerModel appManagerModel;
    private WifiManagerModel wifiManagerModel;


    public HomeModel(Context context, Handler handler) {
        this.processManagerModel = new ProcessManagerModel(context);
        this.appManagerModel = new AppManagerModel(context);
        this.cacheManagerModel = new CacheManagerModel(context);
        this.move2SDModel = new Move2SDModel(context);
        this.wifiManagerModel = new WifiManagerModel(context, handler);
    }

    public long getMemoryAvailable() {
        return processManagerModel.getMemoryAvailable();
    }

    public int getNumRunningProcess() {
        return processManagerModel.getNumProcessNumber();
    }

    public long getSDMemoryAvailable() {
        return appManagerModel.getSDMemoryAvailable();
    }

    public long getInternalMemoryAvailable() {
        return appManagerModel.getInternalMemoryAvailable();
    }

    public long getCacheTotal() {
        return cacheManagerModel.getCacheTotal();
    }

    public int getAppMoves2SD() {
        return move2SDModel.getAppMoves2SD();
    }

    public void getWifisAvailable() {
        wifiManagerModel.getWifisAvailable();
    }
}
