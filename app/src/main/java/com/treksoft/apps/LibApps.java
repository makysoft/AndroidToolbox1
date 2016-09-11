package com.treksoft.apps;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.os.RemoteException;

import com.mobeng.libs.LibAppManager;
import com.tresksoft.toolbox.data.CAplicacion;
import com.tresksoft.toolbox.data.CItemDefault;
import com.tresksoft.toolbox.data.CTamanhoBytes;

public class LibApps {

	private int numLookups;
	private CAplicacion aplicacion;
	private PackageManager pm;
	private Context context;
	private ArrayList<CAplicacion> aplicaciones;
	
	public LibApps(Context context) {
		this.context = context;
		pm = context.getPackageManager();
	}
	
	public static List<ApplicationInfo> getInstallApps(Context context) {
		ArrayList<CItemDefault> apps = new ArrayList<CItemDefault>();
		PackageManager pm = context.getPackageManager();
		return pm.getInstalledApplications(PackageManager.GET_META_DATA);
	}
	
	public static List<CItemDefault> getInstallApps(Context context, int mode) {
		PackageManager pm = context.getPackageManager();
		List<CItemDefault> retApps = new ArrayList<CItemDefault>();
		List<ApplicationInfo> apps = getInstallApps(context);
		for (ApplicationInfo app: apps) {
			if ((app.flags & 0x00000001) == 0 && (app.flags & 0x40000) == 0) {
				if (LibAppManager.isSDInstallable(context, app.packageName)) {
					CItemDefault auxApp = new CItemDefault();
					auxApp.icon = app.loadIcon(pm);
					auxApp.tv1 = pm.getApplicationLabel(app).toString();
					auxApp.tv2 = "";
					auxApp.tv3 = app.packageName;
					retApps.add(auxApp);
				}
			}
		}
		return retApps;
	}
	
	public List<CAplicacion> getAplicacionesInstaladas() throws NameNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InterruptedException {
	
		aplicaciones = new ArrayList<CAplicacion>();
			
		for (final ApplicationInfo ai: pm.getInstalledApplications(PackageManager.GET_META_DATA)) {		
			changeNumLookups(1);
			Method getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
			getPackageSizeInfo.invoke(pm, ai.packageName,
					new IPackageStatsObserver.Stub() {
				
					public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
						//String sd = null;
						synchronized(LibApps.this) {
							changeNumLookups(-1);
							if (succeeded) {
								aplicacion = new CAplicacion();
								aplicacion.setFlags(ai.flags);
								aplicacion.setsNombreAplicacion(pm.getApplicationLabel(ai).toString());
								aplicacion.setPaquete(ai.packageName);
								if ((aplicacion.getFlags() & 0x00000001) == 0) {
									aplicacion.isInstallIntoSD = LibAppManager.isSDInstallable(LibApps.this.context, ai.packageName);
								} else {
									aplicacion.isInstallIntoSD = false;
								}
								aplicacion.setTamanhoAplicacion(new CTamanhoBytes(pStats.codeSize));
								aplicacion.setCacheAplicacion(new CTamanhoBytes(pStats.cacheSize));
								aplicacion.setDataAplicacion(new CTamanhoBytes(pStats.dataSize));
								aplicacion.setIconAplicacion(ai.loadIcon(pm));
								aplicaciones.add(aplicacion);
							}
							LibApps.this.notify();
						}
					}
			});
		}
		while(true) {
			synchronized(this) {
				if (numLookups != 0) {
					wait();
				} else {
					break;
				}
			}
		}
		return aplicaciones;
	}

	private synchronized void changeNumLookups(int change) {
		numLookups += change;
	}
	
}
