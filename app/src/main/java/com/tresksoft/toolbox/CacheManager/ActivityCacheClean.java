package com.tresksoft.toolbox.CacheManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobeng.libs.LibAppManager;
import com.mobeng.libs.LibFileSystem;
import com.tresksoft.toolbox.Home.ActivityMain;
import com.tresksoft.toolbox.R;
import com.tresksoft.toolbox.data.CAplicacion;
import com.tresksoft.toolbox.data.CTamanhoBytes;

public class ActivityCacheClean extends ListActivity{
	
	// Constants used in Handler
	private static final int OP_SUCCESSFUL = 1;
	private static final int OP_FAILED = 2;
	private static final int CLEAR_CACHE = 3;
	private static final int INSTALLED_APP_DETAILS = 1;
	
	// Variables
	private List<CAplicacion> apps = null;
	private List<CAplicacion> appsAdapter = null;
	private AdapterAppCache adapter = null;
	private LibAppManager appManager = null;
	private LinearLayout empty;
	private TextView empty_tv;
	private TextView tvNumApps;
	private TextView tvTotalCache;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// Calculamos el numero de aplicaciones y el total de la cache a limpiar
				int numApps = contarAplicaciones();
				long totalCache = calcularTotalCache();
				
				tvNumApps.setText(getResources().getString(R.string.lbl_total_apps) + ": " + numApps);
				tvTotalCache.setText(getResources().getString(R.string.lbl_total_cache) + ": " + (new CTamanhoBytes(totalCache)).toString());
				
				if ((appsAdapter == null) || (appsAdapter.size() == 0)) {
					empty_tv.setText(getResources().getString(R.string.msg_no_cache));
				}else{
					adapter = new AdapterAppCache(ActivityCacheClean.this, appsAdapter);
					setListAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
				break;
			}

		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cache_clean);
		
		initialize();
		
		final ListView lv = getListView();
		lv.setEmptyView(this.empty);
		
		empty_tv.setText(getResources().getString(R.string.msg_search_cache));
		
		appManager = new LibAppManager();
		appsAdapter = new ArrayList<CAplicacion>();
		
		createHiloLoadApps();
		
	}
	
	public void initialize() {
		this.empty = (LinearLayout) findViewById(R.id.empty);
		this.empty_tv = (TextView) findViewById(R.id.empty_tv);
		this.tvNumApps = (TextView) findViewById(R.id.tv_apps);
		this.tvTotalCache = (TextView) findViewById(R.id.tv_tot_cache);
	}
	
	public void onClean(View v) {
		// Borrar la cache
		LibFileSystem.cleanAllCache(this);
		// Buscamos los programas con cach� > 0 bytes
		createHiloLoadApps();
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		createHiloLoadApps();

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		goApplicationInfo(position);
	}
	
	public void goApplicationInfo(int position) {
		LibAppManager.showPackageInfo(this, appsAdapter.get(position).getPaquete(), INSTALLED_APP_DETAILS);
	}		
	
	private void createHiloLoadApps() {
		// Resetea el adapter
		appsAdapter.clear();
		if (adapter != null)
			adapter.notifyDataSetChanged();
		(new Thread(new Runnable() {
			public void run() {
				loadApps();
			}
		})).start();
	}
		
	
	public void onHomeClick(View v) {
		final Intent intent = new Intent(this, ActivityMain.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
	public void loadApps() {
		apps = appManager.getAppsInstall(this);
		for (CAplicacion app: apps) {
			try {
				CAplicacion retapp = appManager.getAppCache(getPackageManager(), app.getPaquete());
				if (retapp.getCacheAplicacion().bytes > 0) {
					app.setCacheAplicacion(retapp.getCacheAplicacion());
					app.setDataAplicacion(retapp.getDataAplicacion());
					app.setTamanhoAplicacion(retapp.getTamanhoAplicacion());
					appsAdapter.add(app);
				}
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		mHandler.sendEmptyMessage(0);
	}
	
	private int contarAplicaciones() {
		return appsAdapter.size();
	}
	
	private long calcularTotalCache() {
		long totalCache = 0;
		for(CAplicacion app: appsAdapter) {
			totalCache += app.getCacheAplicacion().bytes;
		}
		return totalCache;
	}
	
	class ClearCacheObserver extends IPackageDataObserver.Stub {
		public void onRemoveCompleted(final String packageName, final boolean succeeded) {
			final Message msg = mHandler.obtainMessage(CLEAR_CACHE);
			msg.arg1 = succeeded ? OP_SUCCESSFUL:OP_FAILED;
			mHandler.sendMessage(msg);
		}
	}
	
	
}
