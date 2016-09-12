package com.tresksoft.toolbox.ProccessManager;

import java.net.URLDecoder;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tresksoft.toolbox.R;
import com.tresksoft.toolbox.data.CProcess;

public class AdapterProcessIgnored extends BaseAdapter{
	
	private Context context;
	private ArrayList<CProcess> procesos;

	public AdapterProcessIgnored(Context context, ArrayList<CProcess> procesos) {
		this.context = context;
		this.procesos = procesos;
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
		
		if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
			LayoutInflater li = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.item_process_ignored, null);
			
			holder = new ViewHolder();
			holder.tvNombreProceso = (TextView) convertView.findViewById(R.id.text);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		CProcess proceso = this.procesos.get(position);

		holder.tvNombreProceso.setText(URLDecoder.decode(proceso.getProcessName64()));
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView tvNombreProceso;
	}
	
}
