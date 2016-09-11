package com.tresksoft.toolbox;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tresksoft.toolbox.R;
import com.tresksoft.toolbox.data.CAplicacion;

public class AdapterAplicaciones extends BaseAdapter implements OnClickListener {

	private Context context;
	private List<CAplicacion> aplicaciones;
	private CAplicacion aplicacion;

	public AdapterAplicaciones(Context context, List<CAplicacion> aplicaciones) {
		this.context = context;
		this.aplicaciones = aplicaciones;
	}
	
	public int getCount() {
		return this.aplicaciones.size();
	}
	
	public Object getItem(int position) {
		return this.aplicaciones.get(position);
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
			LayoutInflater li = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.item_aplicacion, null);
			
			holder = new ViewHolder();
			holder.tvNombreAplicacion = (TextView)convertView.findViewById(R.id.text);
			holder.ivIconoAplicacion = (ImageView)convertView.findViewById(R.id.image);
			holder.ivIconoMemoria = (ImageView)convertView.findViewById(R.id.image_memory);
			holder.tvDetail1 = (TextView)convertView.findViewById(R.id.detail_1);
			holder.tvDetail2 = (TextView)convertView.findViewById(R.id.detail_2);
			holder.tvDetail3 = (TextView)convertView.findViewById(R.id.detail_3);
			holder.tvDetail4 = (TextView)convertView.findViewById(R.id.detail_4);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		aplicacion = this.aplicaciones.get(position);
		
		holder.tvNombreAplicacion.setText(aplicacion.getsNombreAplicacion());
		holder.ivIconoAplicacion.setImageDrawable(aplicacion.getIconAplicacion());
		if ((aplicacion.getFlags() & 0x40000) != 0) {
			holder.ivIconoMemoria.setVisibility(View.VISIBLE);
			holder.ivIconoMemoria.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_micro_sd_azul));
		} else {
			if (aplicacion.isInstallIntoSD){
				holder.ivIconoMemoria.setVisibility(View.VISIBLE);
				holder.ivIconoMemoria.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_micro_sd_gris));
			} else {
				holder.ivIconoMemoria.setVisibility(View.INVISIBLE);
			}
		}
		holder.tvDetail1.setText(aplicacion.getPaquete());
		holder.tvDetail2.setText(aplicacion.getTamanhoAplicacion().toString());
		holder.tvDetail2.setTextColor(Color.GRAY);
		holder.tvDetail3.setText(aplicacion.getCacheAplicacion().toString());
		holder.tvDetail3.setTextColor(Color.GRAY);
		holder.tvDetail4.setText(aplicacion.getDataAplicacion().toString());
		holder.tvDetail4.setTextColor(Color.GRAY);
		return convertView;
		
	}
	
	private class ViewHolder {
		TextView tvNombreAplicacion;
		TextView tvDetail1;
		TextView tvDetail2;
		TextView tvDetail3;
		TextView tvDetail4;
		ImageView ivIconoAplicacion;
		ImageView ivIconoMemoria;
		
	}
	
	public void onClick(View v) {
		
	}
	

	
}
