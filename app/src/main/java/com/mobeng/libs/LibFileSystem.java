package com.mobeng.libs;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;

public class LibFileSystem {
	
	private static long getEnvironmentSize() {
		File localFile = Environment.getDataDirectory();
		long l = 0L;
		if (localFile != null) {
			String str = localFile.getPath();
			StatFs localStatFs = new StatFs(str);
			long l2 = localStatFs.getBlockCount();
			long l3 = localStatFs.getBlockSize();
			l = l2 * l3;
		}
		return l;
	}
	
	public static void cleanAllCache(final Context context) {
		
		final Class[] classes = {
			Long.TYPE,	IPackageDataObserver.class	
		};
		
		Long localLong = Long.valueOf(getEnvironmentSize() - 1L);
		PackageManager pm = context.getPackageManager();
		
		try {
			Method localMethod = pm.getClass().getMethod("freeStorageAndNotify", classes);
			try {
				localMethod.invoke(pm, localLong, new IPackageDataObserver.Stub() {
					public void onRemoveCompleted(String packageName, boolean succeeded)
							throws RemoteException {
						// TODO Auto-generated method stub
					}
				});
				
			}catch(IllegalArgumentException e) {
				e.printStackTrace();
			}catch(IllegalAccessException e) {
				e.printStackTrace();
			}catch(InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}catch(NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
}
