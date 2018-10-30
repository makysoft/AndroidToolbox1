package com.tresksoft.toolbox.ApplicationManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobeng.libs.LibAppManager;
import com.mobeng.libs.LibBase;
import com.mobeng.third_party.ActionItem;
import com.mobeng.third_party.QuickAction;
import com.treksoft.apps.LibApps;
import com.tresksoft.graphics.PieChart;
import com.tresksoft.Views.ActivityMain;
import com.tresksoft.toolbox.ActivityPreferences;
import com.tresksoft.toolbox.R;
import com.tresksoft.toolbox.data.CAplicacion;
import com.tresksoft.toolbox.data.ComparatorNombreAplicacion;
import com.tresksoft.toolbox.data.ComparatorTamanhoAplicacion;
import com.tresksoft.toolbox.data.Constants;

public class ActivityApplicationManager extends ListActivity
	implements OnClickListener{

	
	private int currentOrder;
	private int currentFilter;
	private int requestCode;
	
	private ArrayList<CAplicacion> list_ai;
	private ArrayList<CAplicacion> filter_list_ai;
	private ProgressDialog dialog;
	private AdapterAplicaciones adapterAplicaciones = null;
	
	private LinearLayout ll_filter;
	private LinearLayout ll_order;
	
	private TextView tvMemoriaInternaTotal;
	private TextView tvMemoriaInternaDisponible;
	private TextView tvMemoriaSDTotal;
	private TextView tvMemoriaSDDisponible;
	private TextView tvFilterApps;
	private TextView tvOrderApps;
	
	private PieChart pieChartInternalMemory;
	private PieChart pieChartSDMemory;
	
	private QuickAction mQuickAction = null;
	private QuickAction mQuickActionFiltro = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_listado_aplicaciones);
		
		// Titulamos el activity
		this.setTitle("Application Manager");
		
        // Registrar context menu
        ListView listview = (ListView) findViewById(android.R.id.list);
        registerForContextMenu(listview);		
        
		//Cargar preferencias
		loadPreferencias();
		
		initialize();
		
		dialog = ProgressDialog.show(ActivityApplicationManager.this,"",getResources().getText(R.string.lbl_cargando),true);
		
		// Refrescamos las etiquetas
		refrescarEtiquetas();
		
		// Cargamos las aplicaciones
		loadAplicaciones();
		
	}
	
	private void initialize() {
		
		currentOrder = 1;
		currentFilter = 1;
		
		ll_filter = (LinearLayout)findViewById(R.id.id_menu_filter_apps);
		ll_order = (LinearLayout)findViewById(R.id.id_menu_order_apps);
		
		ll_filter.setOnClickListener(this);
		ll_order.setOnClickListener(this);
	
		tvFilterApps = (TextView) findViewById(R.id.lbl_filter_apps);
		tvOrderApps = (TextView) findViewById(R.id.lbl_order_apps);
        tvMemoriaInternaTotal = (TextView) findViewById(R.id.memoria_interna_total);
        tvMemoriaInternaDisponible = (TextView) findViewById(R.id.memoria_interna_disponible);
        tvMemoriaSDTotal = (TextView) findViewById(R.id.memoria_sd_total);
        tvMemoriaSDDisponible = (TextView) findViewById(R.id.memoria_sd_disponible);
        
        pieChartInternalMemory = (PieChart) findViewById(R.id.piechart_internal_memory);
        pieChartSDMemory = (PieChart) findViewById(R.id.piechart_sd_memory);
        
        pieChartInternalMemory.tamanho = (int) getResources().getDimensionPixelOffset(R.dimen.piechart);
        pieChartSDMemory.tamanho = (int) getResources().getDimension(R.dimen.piechart);
		
		ActionItem ascAction = new ActionItem();
		ascAction.setTitle(getResources().getString(R.string.lbl_order_name));
		ascAction.setIcon(getResources().getDrawable(R.drawable.ic_order_name_asc));
		
		ActionItem descAction = new ActionItem();
		descAction.setTitle(getResources().getString(R.string.lbl_order_name));
		descAction.setIcon(getResources().getDrawable(R.drawable.ic_order_name_desc));
		
		ActionItem sizeAscAction = new ActionItem();
		sizeAscAction.setTitle(getResources().getString(R.string.lbl_order_size));
		sizeAscAction.setIcon(getResources().getDrawable(R.drawable.ic_order_size_asc));
		
		ActionItem sizeDesAction = new ActionItem();
		sizeDesAction.setTitle(getResources().getString(R.string.lbl_order_size));
		sizeDesAction.setIcon(getResources().getDrawable(R.drawable.ic_order_size_desc));		
		
		mQuickAction = new QuickAction(this);
		mQuickAction.addActionItem(ascAction);
		mQuickAction.addActionItem(descAction);
		mQuickAction.addActionItem(sizeAscAction);
		mQuickAction.addActionItem(sizeDesAction);
		
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			public void onItemClick(QuickAction source, int pos, int actionId) {
				int tipoOrdenacion = 0;
				// TODO Auto-generated method stub
				if (pos == 0) {
					tipoOrdenacion = 1;
					tvOrderApps.setText(getResources().getString(R.string.lbl_order_name));
				} else if (pos == 1) {
					tipoOrdenacion = 3;
					tvOrderApps.setText(getResources().getString(R.string.lbl_order_name));
				} else if (pos == 3) {
					tipoOrdenacion = 2;
					tvOrderApps.setText(getResources().getString(R.string.lbl_order_size));
				} else if (pos == 2) {
					tipoOrdenacion = 4;
					tvOrderApps.setText(getResources().getString(R.string.lbl_order_size));
				}
				ordenar(tipoOrdenacion);
				handler.sendEmptyMessage(0);
			}
		});
		
		ActionItem allAppsAction = new ActionItem();
		allAppsAction.setTitle(getResources().getString(R.string.lbl_filter_all_apps));
		
		ActionItem onlySDAction = new ActionItem();
		onlySDAction.setTitle(getResources().getString(R.string.lbl_filter_only_sd_apps));
		
		ActionItem onlyInternalAction = new ActionItem();
		onlyInternalAction.setTitle(getResources().getString(R.string.lbl_filter_only_internal_apps));
		
		ActionItem onlySDMoveable = new ActionItem();
		onlySDMoveable.setTitle("Solo apps movibles a SD");
		
		mQuickActionFiltro = new QuickAction(this);
		mQuickActionFiltro.addActionItem(allAppsAction);
		mQuickActionFiltro.addActionItem(onlySDAction);
		mQuickActionFiltro.addActionItem(onlyInternalAction);
		mQuickActionFiltro.addActionItem(onlySDMoveable);
		
		mQuickActionFiltro.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			public void onItemClick(QuickAction source, int pos, int actionId) {
				int tipoFiltro = 0;
				// TODO Auto-generated method stub
				if (pos == 0) {
					tipoFiltro = 1;
					tvFilterApps.setText(getResources().getString(R.string.lbl_filter_all_apps));
				} else if (pos == 1) {
					tipoFiltro = 2;
					tvFilterApps.setText(getResources().getString(R.string.lbl_filter_only_sd_apps));
				} else if (pos == 2) {
					tipoFiltro = 3;
					tvFilterApps.setText(getResources().getString(R.string.lbl_filter_only_internal_apps));
				} else if (pos == 3) {
					tipoFiltro = 4;
					tvFilterApps.setText("Solo apps movibles a SD");
				}
				filtrar(tipoFiltro);
				handler.sendEmptyMessage(0);
			}
		});
		
	}
	
	private void filtrar(int tipoFiltro) {
		currentFilter = tipoFiltro;
		switch(tipoFiltro) {
		case 1:
			mostrarTodos();
			break;
		case 2:
			mostrarSoloSD();
			break;
		case 3:
			mostrarSoloInterna();
			break;
		case 4:
			mostrarSoloMovibles();
			break;
		}
	}
	
	private void mostrarTodos() {
		filter_list_ai = (ArrayList<CAplicacion>) list_ai.clone();
	}
	
	private void mostrarSoloSD() {
		mostrarTodos();
		for(CAplicacion ai: list_ai) {
			if ((ai.getFlags() & 0x40000) == 0) {
				filter_list_ai.remove(ai);
			}
		}
	}
	
	private void mostrarSoloInterna() {
		mostrarTodos();
		for(CAplicacion ai: list_ai) {
			if ((ai.getFlags() & 0x40000) != 0) {
				filter_list_ai.remove(ai);
			}
		}
	}
	
	private void mostrarSoloMovibles() {
		mostrarTodos();
		for(CAplicacion ai: list_ai) {
			if ((ai.getFlags() & 0x00000001) != 0 || (ai.getFlags() & 0x40000) != 0 || !LibAppManager.isSDInstallable(this, ai.getPaquete())) {
				filter_list_ai.remove(ai);
			}
		}
	}
	
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_contextual_applicationmanager, menu);
    }
    
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()) {
    	case R.id.application_info:
    		goApplicationInfo(info.position);
    		return true;
    	case R.id.application_market:
    		goMarket(info.position);
    		return true;
    	default:
    		return super.onContextItemSelected(item);	
    	}
    }	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_principal, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.item_preferences:
			mostrarPreferencias();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void mostrarPreferencias() {
		Intent intent = new Intent(this, ActivityPreferences.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
	private void handleOrdenarNombre(String tipoOrdenacion) {
		ComparatorNombreAplicacion comparator = new ComparatorNombreAplicacion();
		comparator.setTipoOrden(tipoOrdenacion);
		Collections.sort(filter_list_ai,comparator);
	}
	
	private void handleOrdenarTamanho(String tipoOrdenacion) {
		ComparatorTamanhoAplicacion comparator = new ComparatorTamanhoAplicacion();
		comparator.setTipoOrden(tipoOrdenacion);
		Collections.sort(filter_list_ai, comparator);
	}
	
	private void ordenar(int tipoOrdenacion) {
		currentOrder = tipoOrdenacion;
		switch(tipoOrdenacion) {
		case 1:
			handleOrdenarNombre("ASC");
			break;
		case 2:
			handleOrdenarTamanho("DESC");
			break;
		case 3:
			handleOrdenarNombre("DESC");
			break;
		case 4:
			handleOrdenarTamanho("ASC");
			break;
		}
	}
	
	private final Handler handler = new Handler() {
		public void handleMessage(final Message msg) {
/*			if ((filter_list_ai == null) || (filter_list_ai.size() == 0)) {
				
			}else{ */
				//loadPreferencias();
				filtrar(currentFilter);
				ordenar(currentOrder);
				adapterAplicaciones = new AdapterAplicaciones(ActivityApplicationManager.this, filter_list_ai);
				adapterAplicaciones.notifyDataSetChanged();
				setListAdapter(adapterAplicaciones);
			/*}*/
			dialog.dismiss();
		}
	};
	
	private void loadAplicaciones() {
		new Thread() {
			public void run() {
				try {
					LibApps fncs = new LibApps(ActivityApplicationManager.this);
					
					list_ai = (ArrayList<CAplicacion>) fncs.getAplicacionesInstaladas();
					filter_list_ai = (ArrayList<CAplicacion>) list_ai.clone();
					
					handler.sendEmptyMessage(0);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		}.start();
	}
	
	private void loadPreferencias() {
		String listApplication;
		SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFERENCES, Activity.MODE_PRIVATE);
		listApplication = prefs.getString("listApplication", "1");
		currentOrder = Integer.valueOf(listApplication);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		goApplicationInfo(position);
	}
	
	public void goApplicationInfo(int position) {
		LibAppManager.showPackageInfo(this, this.filter_list_ai.get(position).getPaquete(), requestCode);
	}
	
	public void goMarket(int position) {
		String market = "market://details?id=" + this.filter_list_ai.get(position).getPaquete();
		Intent updateIntent = new Intent(Intent.ACTION_VIEW);
		updateIntent.setData(Uri.parse(market));
		try {
			startActivity(updateIntent);
		}catch(Exception e) {
			
		}
	}
	
	public void onHomeClick(View v) {
		final Intent intent = new Intent(this, ActivityMain.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
	public void onSetupClick(View v) {
		final Intent intent = new Intent(this, ActivityPreferences.class);
		this.startActivity(intent);
	}
	
	public void onRefreshClick(View v) {
		dialog = ProgressDialog.show(ActivityApplicationManager.this,"",getResources().getText(R.string.lbl_cargando),true);
		loadAplicaciones();
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.id_menu_filter_apps:
			mQuickActionFiltro.show(v);
			break;
		case R.id.id_menu_order_apps:
			mQuickAction.show(v);
			break;
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		refrescarEtiquetas();
		super.onResume();
	}

	public void refrescarEtiquetas() {
		long memoriaInternaTotal = 0;
		long memoriaInternaDisponible = 0;
		long memoriaSDTotal = 0;
		long memoriaSDDisponible = 0;
		
		long porcionMemoriaInterna = 0;
		long porcionMemoriaSD = 0;
		
		String sMemoriaInternaTotal;
		String sMemoriaInternaAvailable;
		String sMemoriaSDTotal;
		String sMemoriaSDAvailable;
		
		memoriaInternaTotal = LibBase.getInternalMemoryTotal() / 1048576;
		memoriaInternaDisponible = LibBase.getInternalMemoryAvailable() / 1048576;
		memoriaSDTotal = LibBase.getSDMemoryTotal() / 1048576;
		memoriaSDDisponible = LibBase.getSDMemoryAvailable() / 1048576;
				
		if (memoriaInternaTotal == 0) {
			porcionMemoriaInterna = 0;
			sMemoriaInternaTotal = getResources().getString(R.string.lbl_nodisponible);
			sMemoriaInternaAvailable = "";
		} else {
			porcionMemoriaInterna = (memoriaInternaDisponible * 360) / memoriaInternaTotal;
			sMemoriaInternaTotal = getResources().getString(R.string.lblmask_memoria_total).replace("%MASK%", String.valueOf(memoriaInternaTotal));
			sMemoriaInternaAvailable = getResources().getString(R.string.lblmask_memoria_disponible).replace("%MASK%", String.valueOf(memoriaInternaDisponible));
		}
		
		if (memoriaSDTotal == 0) {
			porcionMemoriaSD = 0;
			sMemoriaSDTotal = getResources().getString(R.string.lbl_nodisponible);
			sMemoriaSDAvailable = "";
		} else {
			porcionMemoriaSD = (memoriaSDDisponible * 360) / memoriaSDTotal;
			sMemoriaSDTotal = getResources().getString(R.string.lblmask_memoria_total).replace("%MASK%", String.valueOf(memoriaSDTotal));
			sMemoriaSDAvailable = getResources().getString(R.string.lblmask_memoria_disponible).replace("%MASK%", String.valueOf(memoriaSDDisponible));
		}

		tvMemoriaInternaTotal.setText(sMemoriaInternaTotal);
		tvMemoriaInternaDisponible.setText(sMemoriaInternaAvailable);
		tvMemoriaSDTotal.setText(sMemoriaSDTotal);
		tvMemoriaSDDisponible.setText(sMemoriaSDAvailable);		
		
		pieChartInternalMemory.valorPorcion = porcionMemoriaInterna;
		pieChartSDMemory.valorPorcion = porcionMemoriaSD;
		
		pieChartInternalMemory.invalidate();
		pieChartSDMemory.invalidate();
		
	}
	
}
