package com.tresksoft.toolbox.ProccessManager;

import java.net.URLEncoder;

import android.app.ActivityManager.MemoryInfo;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.mobeng.libs.LibAppManager;
import com.mobeng.libs.LibProcessManager;
import com.mobeng.third_party.ActionItem;
import com.mobeng.third_party.QuickAction;
import com.tresksoft.Views.Main;
import com.tresksoft.toolbox.ApplicationManager.ActivityApplicationManager;
import com.tresksoft.graphics.SaundProgressBar;
import com.tresksoft.toolbox.ActivityPreferences;
import com.tresksoft.toolbox.ColeccionProcesos;
import com.tresksoft.toolbox.DatabaseHelper;
import com.tresksoft.toolbox.Funciones;
import com.tresksoft.toolbox.Home.ProcessApplication;
import com.tresksoft.toolbox.R;
import com.tresksoft.toolbox.data.CRunningProcess;
import com.tresksoft.toolbox.data.CTamanhoBytes;
import com.tresksoft.toolbox.data.SQLFunctions;

public class ActivityProcessManager extends ListActivity {
	
	private AdapterProcess adapterProcess;
	
	private DatabaseHelper dh;
	private ProcessApplication app;
	
	private Handler mHandler = new Handler();
	private long mStartTime = 0L;
	
	private SaundProgressBar bar;
	private SaundProgressBar cpuBar;
	private TextView tvStatusMem; 
	private int total = 0;
	private int requestCode;
	
	private QuickAction mQuickAction = null;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_process_manager);
        
		// Titulamos el activity
		this.setTitle("Process Manager");
        
		// Inicializar variables e instancias
		initialize();
		
        try {
        	total = LibAppManager.getMemoryTotal();
        }catch(Exception e) {
        	
        }
		
        // Registrar context menu
        ListView listview = (ListView) findViewById(android.R.id.list);
        registerForContextMenu(listview);
        
        this.dh = new DatabaseHelper(this);
        app = (ProcessApplication) getApplication();
                
        app.setColeccionProcesosDB(new ColeccionProcesos(SQLFunctions.getProcesosDB(dh,"true")));
        
        refrescarListView();
        
        // Activamos el timer
        if (mStartTime == 0L) {
        	mStartTime = System.currentTimeMillis();
        	mHandler.removeCallbacks(mUpdateTimeTask);
        	mHandler.postDelayed(mUpdateTimeTask, 100);
        }
    }

    public void initialize() {
        bar = (SaundProgressBar)findViewById(R.id.miniprogressbar);
        cpuBar = (SaundProgressBar)findViewById(R.id.miniprogressbar2);
        tvStatusMem = (TextView) findViewById(R.id.status_mem);
        
        ActionItem actShowIgnored = new ActionItem();
        actShowIgnored.setTitle(getResources().getString(R.string.lbl_show_ignored_list));
        
        mQuickAction = new QuickAction(this);
        mQuickAction.addActionItem(actShowIgnored);
        
        mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			
			public void onItemClick(QuickAction source, int pos, int actionId) {
				// TODO Auto-generated method stub
				onIgnoreList();
			}
		});
    }
    
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_contextual_processmanager, menu);
    }
    
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()) {
    	case R.id.add_ignore_list:
    		enviarListaIgnorados(info.position);
    		return true;
    	case R.id.kill_process:
    		matarProceso(info.position);
    		return true;
    	case R.id.open_process:
    		moverTareaAlFrente(info.position);
    		return true;
    	case R.id.force_stop_process:
    		forzarParada(info.position);
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
	
	private void moverTareaAlFrente(int position) {
		PackageManager pm = getPackageManager();
		CRunningProcess running = (CRunningProcess) adapterProcess.getItem(position);
		Intent intent = pm.getLaunchIntentForPackage(running.getPackageName());
		if (intent != null) {
			this.startActivity(intent);
		}
	}
	
	private void matarProceso(int position) {
		CRunningProcess running = (CRunningProcess) adapterProcess.getItem(position);
		LibProcessManager.matarProceso(this, running.getPackageName());
		refrescarListView();
	}
	
	private void enviarListaIgnorados(int position) {
		CRunningProcess running = (CRunningProcess) adapterProcess.getItem(position);
		SQLFunctions.addProcess(dh, running.getPackageName(), URLEncoder.encode(running.getProcessName()), "true");
		refrescarListView();
	}
	
	private void mostrarPreferencias() {
		Intent intent = new Intent(this, ActivityPreferences.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}    

	public void onIgnoreList(){
		Intent intent = new Intent(this, ActivityProcessManagerIgnore.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
    public void onKillClick(View v) {
    	int count = 0;
    	int i = 0;
    	count = adapterProcess.getCount();
    	
    	for(i=0;i<count;i++) {
    		CRunningProcess rp = (CRunningProcess) adapterProcess.getItem(i);
    		if (rp.getMatarProceso())
    			LibProcessManager.matarProceso(this, rp.getPackageName());
    	}
    	refrescarListView();
    }
    
    public void onOverflowClick(View v) {
    	mQuickAction.show(v);
    }
    
    public void onRefreshClick(View v) {
    	refrescarListView();
    }
    
	public void onSetupClick(View v) {
		final Intent intent = new Intent(this, ActivityPreferences.class);
		this.startActivity(intent);
	}
    
    public void onListAplicacionesClick(View v) {
    	Intent intent = new Intent(this, ActivityApplicationManager.class);
    	startActivity(intent);
    }
    
    public void onMainMenuClick(View v) {
    	Intent intent = new Intent(this, Main.class);
    	startActivity(intent);
    }
    
    private void refrescarListView() {
    	//Funciones funciones = new Funciones(this);
    	//actualizarStatusBar();
   		//rp = funciones.getRunningProcess();
   		app.setColeccionProcesosDB(new ColeccionProcesos(SQLFunctions.getProcesosDB(dh, "true")));
	    adapterProcess = new AdapterProcess(ActivityProcessManager.this, 
	    		   					LibProcessManager.getRunningProcess(this), 
	    		   					app.coleccionProcesosDB.getColeccion(), 
	    		   					dh);
	    setListAdapter(adapterProcess);
    }
    
    private void actualizarStatusBar()  {
    	Funciones funciones = new Funciones(this);
        // Obtener la memoria disponible
        MemoryInfo memInfo = funciones.getMemoryInfo();
        int cpu = (int) (LibAppManager.readUsage()*100);
        cpuBar.setProgress(cpu);
        
        int progress = (int) (((memInfo.availMem/1024)*100)/total);
        bar.setProgress(progress);
        tvStatusMem.setText((String)getResources().getString(R.string.lbl_statusbar_free_mem) + " " + (new CTamanhoBytes(memInfo.availMem)).toString());
    }
       
    private void forzarParada(int position) {
    	// Mostrar advertencia
    	// Aqui....
    	CRunningProcess running = (CRunningProcess) adapterProcess.getItem(position);
    	LibAppManager.showPackageInfo(this, running.getPackageName(), requestCode);
    }
    
    protected void onRestart() {
    	super.onRestart();
    }
    
    protected void onResume() {
    	mHandler.removeCallbacks(mUpdateTimeTask);
    	mHandler.postDelayed(mUpdateTimeTask, 100);
    	super.onResume();
    }
    
    protected void onPause() {
    	mHandler.removeCallbacks(mUpdateTimeTask);
    	super.onPause();
    }
    
    protected void onDestroy() {
    	dh.close();
    	super.onDestroy();
    }
    
    private Runnable mUpdateTimeTask = new Runnable() {
    	public void run() {
    		final long start = mStartTime;
    		long millis = System.currentTimeMillis() - start;
    		int seconds = (int) (millis / 1000);
    		seconds = seconds % 60;
    		
    		actualizarStatusBar();
    		
    		mHandler.postDelayed(this, 2000);
    	}
    };
   
}