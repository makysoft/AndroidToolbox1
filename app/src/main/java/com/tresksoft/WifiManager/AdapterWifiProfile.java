package com.tresksoft.WifiManager;

import java.util.List;

import com.tresksoft.toolbox.R;
import com.tresksoft.wifi.WifiLib;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterWifiProfile extends BaseAdapter implements OnClickListener{

	private List<WifiConfiguration> redesConfiguradas;
	private WifiConfiguration red;
	private Context context;
	
	public AdapterWifiProfile(Context context, List<WifiConfiguration> redesConfiguradas) {
		this.context = context;
		this.redesConfiguradas = redesConfiguradas;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return this.redesConfiguradas.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.redesConfiguradas.get(arg0);
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
			convertView = li.inflate(R.layout.item_wifi_profile, null);
			
			holder = new ViewHolder();
			holder.tvSSID = (TextView)convertView.findViewById(R.id.ssid);
			holder.tvCapabilities = (TextView)convertView.findViewById(R.id.capabilities);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		red = this.redesConfiguradas.get(position);

		if (red.SSID != null) {
			holder.tvSSID.setText(red.SSID.replace("\"", ""));
		}
		
		holder.tvCapabilities.setText(WifiLib.getSecurity(context, red.allowedAuthAlgorithms, red.allowedGroupCiphers, red.allowedKeyManagement, red.allowedPairwiseCiphers, red.allowedProtocols).codificacionString);
		
		return convertView;
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private class ViewHolder {
		TextView tvSSID;
		TextView tvCapabilities;
	}
	
}
