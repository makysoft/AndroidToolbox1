package com.tresksoft.toolbox.Move2SDManager;

import android.content.Context;

import com.treksoft.apps.LibApps;
import com.tresksoft.toolbox.data.CItemDefault;

import java.util.List;

/**
 * Created by Salva on 19/9/16.
 */
public class Move2SDModel {
    private final Context context;

    public Move2SDModel(Context context) {
        this.context = context;
    }

    public int getAppMoves2SD() {
        List<CItemDefault> apps = LibApps.getInstallApps(context, 0);
        return apps.size();
    }
}
