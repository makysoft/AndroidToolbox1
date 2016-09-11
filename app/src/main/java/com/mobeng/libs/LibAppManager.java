package com.mobeng.libs;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

import com.tresksoft.toolbox.ProcessApplication;
import com.tresksoft.toolbox.data.CAplicacion;
import com.tresksoft.toolbox.data.CTamanhoBytes;

public class LibAppManager {

	private static final String BOGOMIPS_PATTERN = "BogoMIPS[\\s]*:[\\s]*(\\d+\\.\\d+)[\\s]*\n";
	private static final String MEMTOTAL_PATTERN = "MemTotal[\\s]*:[\\s]*(\\d+)[\\s]*kB\n";
	private static final String MEMFREE_PATTERN = "MemFree[\\s]*:[\\s]*(\\d+)[\\s]*kB\n";
	private CAplicacion mAplicacion;
	
	public static final int FLAG_FORWARD_LOCK = 1<<20;
	
	private boolean mSemaforo;
	
	long total = 0;
	long idle = 0;
	float usage = 0;

	public static int getInstalledLocation(Class<PackageInfo> paramClass, Object paramObject, String paramString)
	  {
	    int i = 1;
	    try
	    {
	      int j = ((Integer)paramClass.getField(paramString).get(paramObject)).intValue();
	      i = j;
	    }
	    catch (SecurityException localSecurityException)
	    {
	        localSecurityException.printStackTrace();
	    }
	    catch (NoSuchFieldException localNoSuchFieldException)
	    {
	        localNoSuchFieldException.printStackTrace();
	    }
	    catch (IllegalArgumentException localIllegalArgumentException)
	    {
	        localIllegalArgumentException.printStackTrace();
	    }
	    catch (IllegalAccessException localIllegalAccessException)
	    {
	        localIllegalAccessException.printStackTrace();
	    }
	    return i;
	  }

	
	public static boolean isSDInstallable(Context context, String packageName) {
		boolean retVal = false;
		
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pkgInfo = pm.getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			
		      int k = getInstalledLocation(PackageInfo.class, pkgInfo, "installLocation");
		      if ((k == 0) || (k == 2))
		    	  retVal = true;
		      
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
		/*
		// Experimentally determined
		final int auto = 0;
		final int internalOnly = 1;
		final int preferExternal = 2;
		boolean retval = false;
		try {
			AssetManager am = context.createPackageContext(packageName, 0).getAssets();
			XmlResourceParser xml = am.openXmlResourceParser("AndroidManifest.xml");
			int eventType = xml.getEventType();
			xmlloop:
			while (eventType != XmlPullParser.END_DOCUMENT) {
			    switch (eventType) {
			        case XmlPullParser.START_TAG:
			            if (! xml.getName().matches("manifest")) {
			                break xmlloop;
			            } else {
			                attrloop:
			                for (int j = 0; j < xml.getAttributeCount(); j++) {
			                    if (xml.getAttributeName(j).matches("installLocation")) {
			                        switch (Integer.parseInt(xml.getAttributeValue(j))) {
			                            case auto:
			                                // Do stuff
			                            	retval = true;
			                                break;
			                            case internalOnly:
			                                // Do stuff
			                            	retval = false;
			                                break;
			                            case preferExternal:
			                                // Do stuff
			                            	retval = true;
			                                break;
			                            default:
			                                // Shouldn't happen
			                                // Do stuff
			                                break;
			                        }
			                        break attrloop;
			                    }
			                }
			            }
			            break;
			        }
			        eventType = xml.nextToken();
			    }
		}catch(Exception e) {
			e.printStackTrace();
		}
		return retval;
		*/
	}
	
	private static IPackageManager packageManager()
	  {
		IPackageManager pack = null;
	    try
	    {
	      Class localClass = Class.forName("android.os.ServiceManager");
	      Class[] arrayOfClass = new Class[1];
	      arrayOfClass[0] = String.class;
	      Method localMethod = localClass.getMethod("getService", arrayOfClass);
	      Object[] arrayOfObject = new Object[1];
	      arrayOfObject[0] = "package";
	      IPackageManager localIPackageManager1 = IPackageManager.Stub.asInterface((IBinder)localMethod.invoke(null, arrayOfObject));
	      pack = localIPackageManager1;
	    }
	    catch (InvocationTargetException e)
	    {
	    	e.printStackTrace();
	    }
	    catch (IllegalArgumentException e)
	    {
	    	e.printStackTrace();
	    }
	    catch (IllegalAccessException e)
	    {
	    	e.printStackTrace();
	    }
	    catch (NoSuchMethodException e)
	    {
	    	e.printStackTrace();
	    }
	    catch (ClassNotFoundException e)
	    {
	    	e.printStackTrace();
	    }
	    return pack;
	  }	
	
	public List<CAplicacion> getAppsInstall(Context context) {
		
		PackageManager pm = context.getPackageManager();
		
		ArrayList<CAplicacion> aplicaciones = null;
		List<ApplicationInfo> appInfoList = null;
		
		aplicaciones = new ArrayList<CAplicacion>();
		appInfoList = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		
		for (ApplicationInfo appInfo: appInfoList) {
			// Introducimos en el list
			aplicaciones.add(getAppDetails(pm, appInfo));
		}
		
		return aplicaciones;
		
	}
	
	public CAplicacion getAppDetails(PackageManager pm, ApplicationInfo appInfo) {
		CAplicacion aplicacion = new CAplicacion();
		
		aplicacion.setFlags(appInfo.flags);
		aplicacion.setsNombreAplicacion(pm.getApplicationLabel(appInfo).toString());
		aplicacion.setPaquete(appInfo.packageName);
		aplicacion.setIconAplicacion(appInfo.loadIcon(pm));
		
		return aplicacion;		
	}
	
	public CAplicacion getAppCache(PackageManager pm, String pkgName) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InterruptedException {
		
		mSemaforo = true;
		
		Method getPackageSizeInfo = null;
		
		getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
		getPackageSizeInfo.invoke(pm, pkgName,
				new  IPackageStatsObserver.Stub() {
					
					public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
							throws RemoteException {
						// TODO Auto-generated method stub
						synchronized(LibAppManager.this) {
							mSemaforo = false;
							if (succeeded) {
								mAplicacion = new CAplicacion();
								mAplicacion.setCacheAplicacion(new CTamanhoBytes(pStats.cacheSize));
								mAplicacion.setTamanhoAplicacion(new CTamanhoBytes(pStats.codeSize));
								mAplicacion.setDataAplicacion(new CTamanhoBytes(pStats.dataSize));
							}
							LibAppManager.this.notify();
						}
					}
				});
		
		while(mSemaforo) {
			synchronized(this) {
				wait();
			}
		}
		
		return mAplicacion;
	}
	
	public static void showPackageInfo(Object obj, String paquete, int requestCode) {
		ProcessApplication app = (ProcessApplication) ((Activity) obj).getApplicationContext();
		// Según que versión de Android tengamos se llama al activity details de una u otra forma
		try {
			Intent intent;
			if (app.apiLevel >= 9) {
				intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + paquete));
				intent.putExtra("paquete", paquete);
				((Activity) obj).startActivityForResult(intent, requestCode);
			}else{
				intent = new Intent();
				final String appPkgName = (app.apiLevel == 8 ? "pkg" : "com.android.settings.ApplicationPkgName");
				intent.setAction(Intent.ACTION_VIEW);
				intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
				intent.putExtra(appPkgName, paquete);
				intent.putExtra("paquete", paquete);
				((Activity) obj).startActivityForResult(intent, requestCode);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int getMemoryTotal() throws Exception {
	    final MatchResult matchResult = LibAppManager.matchSystemFile("/proc/meminfo", MEMTOTAL_PATTERN, 1000);

	    try {
	      if(matchResult.groupCount() > 0) {
	        return Integer.parseInt(matchResult.group(1));
	      } else {
	        throw new Exception();
	      }
	    } catch (final NumberFormatException e) {
	      throw new Exception(e);
	    }
	}

	  private static MatchResult matchSystemFile(final String pSystemFile, final String pPattern, final int pHorizon) throws Exception {
	    InputStream in = null;
	    try {
	      final Process process = new ProcessBuilder(new String[] { "/system/bin/cat", pSystemFile }).start();

	      in = process.getInputStream();
	      final Scanner scanner = new Scanner(in);

	      final boolean matchFound = scanner.findWithinHorizon(pPattern, pHorizon) != null;
	      if(matchFound) {
	        return scanner.match();
	      } else {
	        throw new Exception();
	      }
	    } catch (final IOException e) {
	      throw new Exception(e);
	    } 
	      
	  }	
	  
	  public static float readUsage( )
	  {
		  try {
		        RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
		        String load = reader.readLine();

		        String[] toks = load.split(" ");

		        long idle1 = Long.parseLong(toks[5]);
		        long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
		              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

		        try {
		            Thread.sleep(360);
		        } catch (Exception e) {}

		        reader.seek(0);
		        load = reader.readLine();
		        reader.close();

		        toks = load.split(" ");

		        long idle2 = Long.parseLong(toks[5]);
		        long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
		            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

		        return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }

		    return 0;
	  }
}
