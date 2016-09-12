package com.tresksoft.toolbox.NetworkManager;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tresksoft.toolbox.R;

public class AdapterNetworkProfiles extends BaseAdapter{

	private Context context;
	private List<NetworkProfile> perfiles;

	public AdapterNetworkProfiles(Context context, List<NetworkProfile> perfiles) {
		this.context = context;
		this.perfiles = perfiles;
	}
	
	public int getCount() {
		return this.perfiles.size();
	}

	public Object getItem(int arg0) {
		return this.perfiles.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		
		if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
			LayoutInflater li = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.item_network_profile, null);
			
			holder = new ViewHolder();
			holder.tvNombrePerfil = (TextView)convertView.findViewById(R.id.profile_name);
			holder.tvModoConfiguracion = (TextView)convertView.findViewById(R.id.profile_ip);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		NetworkProfile profile = this.perfiles.get(position);

		holder.tvNombrePerfil.setText(profile.nombreperfil);
		if (profile.data.type_connection.equals("dhcp")) {
			holder.tvModoConfiguracion.setText("IP (DHCP)");
		} else {
			holder.tvModoConfiguracion.setText("IP (" + profile.data.ip + ")");			
		}
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView tvNombrePerfil;
		TextView tvModoConfiguracion;
	}

}
