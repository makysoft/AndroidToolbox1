package com.tresksoft.toolbox.ProccessManager;

import android.content.Context;

import com.mobeng.libs.LibProcessManager;
import com.tresksoft.toolbox.Funciones;
import com.tresksoft.toolbox.Home.HomePresenter;

/**
 * Created by Salva on 18/9/16.
 */
public class ProcessManagerModel {

    private final Context context;

    public ProcessManagerModel(Context context) {
        this.context = context;
    }

    public long getMemoryAvailable() {
        Funciones funciones = new Funciones(context);
        return funciones.getMemoryInfo().availMem;
    }

    public int getNumProcessNumber() {
        return LibProcessManager.getNumProcesos(context);
    }
}
