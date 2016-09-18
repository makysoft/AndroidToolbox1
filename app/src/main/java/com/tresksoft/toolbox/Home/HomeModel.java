package com.tresksoft.toolbox.Home;

import android.content.Context;

import com.tresksoft.toolbox.ApplicationManager.AppManagerModel;
import com.tresksoft.toolbox.ProccessManager.ProcessManagerModel;

/**
 * Created by Salva on 13/9/16.
 */
public class HomeModel {

    private ProcessManagerModel processManagerModel;

    private AppManagerModel appManagerModel;

    public HomeModel(Context context) {
        this.processManagerModel = new ProcessManagerModel(context);
        this.appManagerModel = new AppManagerModel(context);
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
}
