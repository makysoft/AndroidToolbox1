package com.tresksoft.toolbox.ProccessManager;

import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tresksoft.Home.ActivityMain;
import com.tresksoft.toolbox.DatabaseHelper;
import com.tresksoft.toolbox.R;
import com.tresksoft.toolbox.data.CProcess;
import com.tresksoft.toolbox.data.SQLFunctions;

public class ActivityProcessManagerIgnore extends ListActivity{

	ArrayList<CProcess> procesos;
	AdapterProcessIgnored adapter;
	DatabaseHelper dh;
	
	private LinearLayout empty;
	private TextView empty_tv;
	private TextView tvNumProcessIgnored;
	
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_process_manager_ignore);
		
		// Titulamos el activity
		this.setTitle("Process Manager - Ignored");
		
		initialize();
		
        // Registrar context menu
        ListView listview = (ListView) findViewById(android.R.id.list);
        registerForContextMenu(listview);
        listview.setEmptyView(this.empty);
        empty_tv.setText(getResources().getString(R.string.lbl_no_process_ignored));
		
        // Inicializar variables
        dh = new DatabaseHelper(this);
        
		loadProcesosIgnorados();
	}
	
	private void initialize() {
		this.empty = (LinearLayout)findViewById(R.id.empty);
		this.empty_tv = (TextView)findViewById(R.id.empty_tv);
		this.tvNumProcessIgnored = (TextView)findViewById(R.id.tv_status_process_ignored);
	}
	
	private void actualizarInfoStatus() {
		this.tvNumProcessIgnored.setText(procesos.size() + " " + getResources().getString(R.string.lbl_process_ignored));
	}
	
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_contextual_processmanager_ignore, menu);
    }
    
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()) {
    	case R.id.remove_ignored:
    		quitarListaIgnorados(info.position);
    		return true;
    	default:
    		return super.onContextItemSelected(item);	
    	}
    }
	
	private final Handler handler = new Handler() {
		public void handleMessage(final Message msg) {
			if ((procesos == null) || (procesos.size() == 0)) {
				empty_tv.setText(getResources().getString(R.string.lbl_no_process_ignored));
			} else {
				adapter = new AdapterProcessIgnored(ActivityProcessManagerIgnore.this, procesos);
				setListAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}
	};
	
	private void quitarListaIgnorados(int position) {
		CProcess proceso = procesos.get(position);
		SQLFunctions.addProcess(dh, proceso.getPkgName(), URLEncoder.encode(proceso.getProcessName64()), "false");
		procesos.clear();
		if (adapter != null)
			adapter.notifyDataSetChanged();
		loadProcesosIgnorados();
	}
	
	private void loadProcesosIgnorados() {
		procesos = dh.select("%", "%", "true");
		actualizarInfoStatus();
		handler.sendEmptyMessage(0);
	}
	
	public void onProcessList(View v) {
		Intent intent = new Intent(this, ActivityProcessManager.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	public void onMainMenuClick(View v) {
		Intent intent = new Intent(this, ActivityMain.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
}
