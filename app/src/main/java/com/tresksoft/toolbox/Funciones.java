package com.tresksoft.toolbox;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Debug;
import android.widget.Toast;

import com.mobeng.libs.LibProcessManager;
import com.tresksoft.toolbox.data.CProcess;
import com.tresksoft.toolbox.data.CRunningProcess;
import com.tresksoft.toolbox.data.CTamanhoBytes;
import com.tresksoft.toolbox.data.ColeccionRunningProcess;
import com.tresksoft.toolbox.data.Constants;

public class Funciones {

	private Context context;
	
	int minUID;
	
	public Funciones(Context context) {
		this.context = context;
	}
	
	public void matarProceso() {
		MemoryInfo memFinal;
		String pkgName;
		ColeccionRunningProcess crp = new ColeccionRunningProcess();
		crp = getRunningProcess();
		for (CRunningProcess rp: crp.getColeccionRunningProcess()) {
			if (rp.getMatarProceso()) {
				pkgName = rp.getPackageName();
				LibProcessManager.matarProceso(context, pkgName);
			}
		}
		//Obtenemos la memoria liberada por el proceso
		memFinal = this.getMemoryInfo();
		Toast.makeText(context, context.getResources().getString(R.string.process_mem_free) + " " + (new CTamanhoBytes(memFinal.availMem)).toString(), Toast.LENGTH_LONG).show();
	}
		
	public ColeccionRunningProcess getRunningProcess() {
		ProcessApplication app = (ProcessApplication) this.context.getApplicationContext();
		ColeccionProcesos coleccionProcesosDB = app.getColeccionProcesosDB();
		CProcess killProcess;
		
		ColeccionRunningProcess colRP = new ColeccionRunningProcess();
		CRunningProcess rp;
		Integer numProcessToKill = 0;
		
		loadPreferencias();
		
        // Listar las Activities
        PackageManager pm = this.context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        // Listar las aplicaciones en ejecución
        ActivityManager am = (ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE);
        List <RunningAppProcessInfo> procInfos = am.getRunningAppProcesses();
        // Recorremos el listado de activities
        
        for (RunningAppProcessInfo rapi: procInfos) {
            for(ResolveInfo ri: list) {
       		 if (ri.activityInfo.processName.equals(rapi.processName) && rapi.uid > minUID) {
       			 // Creamos una nueva instancia de runningProcess
       			 rp = new CRunningProcess();
       			 rp.setUID(rapi.uid);
       			 rp.setPID(rapi.pid);
       			 rp.setProcessName(ri.activityInfo.applicationInfo.loadLabel(pm).toString());
       			 rp.setPackageName(ri.activityInfo.applicationInfo.packageName);
       			 rp.setImportance(rapi.importance);
       			 rp.setIcon(ri.activityInfo.applicationInfo.loadIcon(pm));
       			 // Setear memoria que ocupa el proceso
	        	 int pids[] = new int [1];
	        	 pids[0] = rapi.pid;
	        	 Debug.MemoryInfo[] memoryProcessInfo = am.getProcessMemoryInfo(pids);
	        	 for (Debug.MemoryInfo pidMemoryInfo: memoryProcessInfo) {
	        	    	rp.setProcessMemory(pidMemoryInfo.getTotalPrivateDirty());
	        	 }
	        	 killProcess = coleccionProcesosDB.buscar(new CProcess(null,ri.activityInfo.applicationInfo.packageName, ""));
	        	 if (killProcess == null) {
	        		 rp.setMatarProceso(true);
	        		 numProcessToKill++;
	        	 } else {
	        		 rp.setMatarProceso(false);
	        	 }
       			 colRP.add(rp);
       			 break;
       		 }
       	 }
        }
        colRP.setRunningProcessToKill(numProcessToKill);
        
		return colRP;
	}
	
	public void loadPreferencias() {
		String tipoProcesosMostrados;
		SharedPreferences prefs = this.context.getSharedPreferences(Constants.SHARED_PREFERENCES, Activity.MODE_PRIVATE);
		tipoProcesosMostrados = prefs.getString("listProcess", "");
		if (tipoProcesosMostrados.equals("1")){
			minUID = 10000;
		}else{
			minUID = 0;
		}
	}
	
	public MemoryInfo getMemoryInfo() {
		ActivityManager activityManager = (ActivityManager) this.context.getSystemService(this.context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		return memoryInfo;
	}
	
}
