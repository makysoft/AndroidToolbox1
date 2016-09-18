package com.tresksoft.toolbox.ApplicationManager;

import android.content.Context;

import com.mobeng.libs.LibBase;

/**
 * Created by Salva on 19/9/16.
 */
public class AppManagerModel {

    private static final long MB_VALUE = 1048576;
    private final Context context;

    public AppManagerModel(Context context) {
        this.context = context;
    }

    public long getSDMemoryAvailable() {
        return LibBase.getSDMemoryAvailable() / MB_VALUE;
    }

    public long getInternalMemoryAvailable() {
        return LibBase.getInternalMemoryAvailable() / MB_VALUE;
    }
}
