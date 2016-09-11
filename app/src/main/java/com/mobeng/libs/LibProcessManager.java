package com.mobeng.libs;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Debug;

import com.tresksoft.toolbox.DatabaseHelper;
import com.tresksoft.toolbox.data.CProcess;
import com.tresksoft.toolbox.data.CRunningProcess;
import com.tresksoft.toolbox.data.ColeccionRunningProcess;
import com.tresksoft.toolbox.data.SQLFunctions;

public class LibProcessManager {

	public static void matarProceso(Context context, String pkgName) {
		int apiLevel = Build.VERSION.SDK_INT;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if ((apiLevel >= 3) && (apiLevel <= 8)) {
			am.restartPackage(pkgName);
		}else if (apiLevel >= 8) {
			am.killBackgroundProcesses(pkgName);
		}
	}
	
	public static void matarProceso(Context context, ColeccionRunningProcess crp) {
		String pkgName;
		for (CRunningProcess rp: crp.coleccionRunningProcess) {
			if (rp.getMatarProceso()) {
				pkgName = rp.getPackageName();
				LibProcessManager.matarProceso(context, pkgName);
			}
		}
	}
	
	public static void matarProceso(Context context) {
		ArrayList<String> killPkgs = new ArrayList<String>();
		boolean kill;
		DatabaseHelper dh = new DatabaseHelper(context);
		ArrayList<CRunningProcess> crp = getRunningProcess(context);
		ArrayList<CProcess> cpr = SQLFunctions.getProcesosDB(dh, "true");
		// Buscar procesos ha matar
		for(CRunningProcess rp: crp) {
			kill = true;
			for (CProcess pr: cpr) {
				if (rp.getPackageName().equals(pr.getPkgName())) {
					kill = false;
					break;
				}
			}
			if (kill) 
				killPkgs.add(rp.getPackageName());
		}
		for(String pkgName: killPkgs) {
			matarProceso(context, pkgName);
		}
	}
	
	public static ArrayList<CRunningProcess> getRunningProcess(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List <RunningAppProcessInfo> procInfos = am.getRunningAppProcesses();
		PackageManager pm = context.getPackageManager();
		ApplicationInfo ai = null;
		ArrayList<CRunningProcess> crp = new ArrayList<CRunningProcess>();
		CRunningProcess rp;
		String pkgName;
		for (RunningAppProcessInfo rapi: procInfos) {
			pkgName = pm.getNameForUid(rapi.uid);
			try {
				ai = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
				rp = new CRunningProcess();
				rp.setUID(rapi.uid);
				rp.setPID(rapi.pid);
				rp.setProcessName(String.valueOf(pm.getApplicationLabel(ai)));
				rp.setPackageName(pkgName);
				rp.setImportance(rapi.importance);
				rp.setIcon(pm.getApplicationIcon(ai));
				int pids[] = new int [1];
	        	pids[0] = rapi.pid;
	        	Debug.MemoryInfo[] memoryProcessInfo = am.getProcessMemoryInfo(pids);
	        	for (Debug.MemoryInfo pidMemoryInfo: memoryProcessInfo) {
	        	   	rp.setProcessMemory(pidMemoryInfo.getTotalPrivateDirty());
	        	}
				crp.add(rp);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return crp;
	}
	
	public static int getNumProcesos(Context context) {
		ArrayList<CRunningProcess> procesos = getRunningProcess(context);
		return procesos.size();
	}
	
	public static MemoryInfo getMemoryInfo(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		return memoryInfo;
	}
	
}
