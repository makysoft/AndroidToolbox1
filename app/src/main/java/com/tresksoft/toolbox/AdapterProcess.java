package com.tresksoft.toolbox;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tresksoft.toolbox.data.CProcess;
import com.tresksoft.toolbox.data.CRunningProcess;
import com.tresksoft.toolbox.data.HelperDatabase;
import com.tresksoft.toolbox.data.SQLFunctions;

public class AdapterProcess extends BaseAdapter implements OnClickListener{
	
	private final Context context;
	private final List<CRunningProcess> procesos;
	private final ArrayList<CProcess> procesosDB;
	private DatabaseHelper dh;
	
	public AdapterProcess(Context context, List<CRunningProcess> procesos, ArrayList<CProcess> procesosDB, HelperDatabase dbHelper) {
		this.context = context;
		this.procesos = procesos;
		this.procesosDB = procesosDB;
		quitarProcesosIgnorados();
	}
	
	public AdapterProcess(Context context, List<CRunningProcess> procesos, ArrayList<CProcess> procesosDB, DatabaseHelper dh) {
		this.context = context;
		this.procesos = procesos;
		this.dh = dh;
		this.procesosDB = procesosDB;
		quitarProcesosIgnorados();
	}
	
	public void quitarProcesosIgnorados() {
		int i = 0;
		int j = 0;
		int sizeI = procesos.size();
		int sizeJ = procesosDB.size(); 
		
		for (i = 0; i < sizeI; i++) {
			procesos.get(i).setMatarProceso(true);
			for (j = 0; j < sizeJ; j++) {
				if (procesos.get(i).getPackageName().equals(procesosDB.get(j).getPkgName())) {
					procesos.get(i).setMatarProceso(false);
				}
			}
		}
	}

	public int getCount() {
		return this.procesos.size();
	}

	public Object getItem(int position) {
		return this.procesos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		String sTipoProceso = new String("");
		String sUID;
		try {
			if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
				LayoutInflater li = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.item_process, null);
				
				holder = new ViewHolder();
				holder.ivIconoAplicacion = (ImageView)convertView.findViewById(R.id.image);
				holder.tvNombreProceso = (TextView)convertView.findViewById(R.id.text);	
				holder.tvUIDProceso = (TextView)convertView.findViewById(R.id.uid_proceso);
				holder.tvTipoProceso = (TextView)convertView.findViewById(R.id.tipo_proceso);	
				holder.tvMemProceso = (TextView)convertView.findViewById(R.id.mem_proceso);
				holder.cb = (CheckBox)convertView.findViewById(R.id.plain_cb);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			
			CRunningProcess proceso = this.procesos.get(position);
			
			holder.ivIconoAplicacion.setImageDrawable(proceso.getIcon());
			holder.tvNombreProceso.setText(proceso.getProcessName());
			if (proceso.getUID() >= 10000) {
				sUID = (String) this.context.getResources().getText(R.string.lbl_process_uid_user);
			}else{
				sUID = (String) this.context.getResources().getText(R.string.lbl_process_uid_system);
			}
			holder.tvUIDProceso.setText(sUID);
			switch(proceso.getImportance()){
			case 400:
				sTipoProceso = (String) this.context.getResources().getText(R.string.lbl_status_background);
				break;
			case 500:
				sTipoProceso = (String) this.context.getResources().getText(R.string.lbl_status_empty);
				break;
			case 100:
				sTipoProceso = (String) this.context.getResources().getText(R.string.lbl_status_foreground);
				break;
			case 130:
				sTipoProceso = (String) this.context.getResources().getText(R.string.lbl_status_perceptible);
				break;
			case 300:
				sTipoProceso = (String) this.context.getResources().getText(R.string.lbl_status_service);
				break;
			case 200:
				sTipoProceso = (String) this.context.getResources().getText(R.string.lbl_status_visible);
				break;
			}
			holder.tvTipoProceso.setText(sTipoProceso);
			holder.tvMemProceso.setText("Mem: " + String.valueOf(proceso.getProcessMemory()/1024) + "MB");
			
			holder.cb.setTag(position);
			holder.cb.setOnClickListener(this);
			holder.cb.setChecked(proceso.getMatarProceso());
			

		}catch(Exception e){
			e.printStackTrace();
		}
		return convertView;
	}
	
	public void onClick(View v) {
		CheckBox cBox = (CheckBox) v;
		int position = (Integer) cBox.getTag();
		CRunningProcess pr = procesos.get(position);
		pr.setMatarProceso(cBox.isChecked());
		SQLFunctions.addProcess(dh, pr.getPackageName(), URLEncoder.encode(pr.getProcessName()), String.valueOf(cBox.isChecked()));
	}
	
	private class ViewHolder {
		ImageView ivIconoAplicacion;
		TextView tvNombreProceso;
		TextView tvUIDProceso;
		TextView tvTipoProceso;
		TextView tvMemProceso;
		CheckBox cb;
	}
	
}
