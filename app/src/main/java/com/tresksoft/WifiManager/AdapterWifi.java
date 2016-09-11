package com.tresksoft.WifiManager;

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

import com.tresksoft.toolbox.R;
import com.tresksoft.wifi.WifiScanItem;

public class AdapterWifi extends BaseAdapter implements OnClickListener{
	
	private Context context;
	private List<WifiScanItem> wifis;
	private WifiScanItem wifi;

	public AdapterWifi(Context context, List<WifiScanItem> wifis){
		this.context = context;
		this.wifis = wifis;
	}
	
	public int getCount() {
		return this.wifis.size();
	}
	
	public Object getItem(int position) {
		return this.wifis.get(position);
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(final int position, View converView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (converView == null || !(converView.getTag() instanceof ViewHolder)) {
			LayoutInflater li = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			converView = li.inflate(R.layout.item_wifi2, null);
			
			holder = new ViewHolder();
			holder.tvSSID = (TextView)converView.findViewById(R.id.ssid);
			holder.tvBSSID = (TextView)converView.findViewById(R.id.bssid);
			holder.tvCapabilities = (TextView)converView.findViewById(R.id.capabilities);
			holder.cb = (CheckBox)converView.findViewById(R.id.star);
			holder.iv = (ImageView)converView.findViewById(R.id.wifi_signal_strength);
			
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}
		
		
		wifi = this.wifis.get(position);
		
		holder.tvSSID.setText(wifi.SSID);
		holder.tvBSSID.setText("BSSID " + wifi.BSSID);
		if (wifi.capabilities.contains("WEP")) {
			holder.tvCapabilities.setText("WEP");
		} else if (wifi.capabilities.contains("WPA2")) {
			holder.tvCapabilities.setText("WPA2");
		} else if (wifi.capabilities.contains("WPA")) {
			holder.tvCapabilities.setText("WPA");
		} else {
			holder.tvCapabilities.setText("Open");
		}
		
		if (wifi.wificonfigurada != null) {
			holder.cb.setVisibility(View.VISIBLE);
		} else {
			holder.cb.setVisibility(View.INVISIBLE);
		}
		
		switch (wifi.level) {
		case 0:
			holder.iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_strength_1));
			break;
		case 1:
			holder.iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_strength_2));
			break;
		case 2:
			holder.iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_strength_3));
			break;
		case 3:
			holder.iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_strength_4));
			break;
		case 4:
			holder.iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_strength_5));
			break;
		}
		return converView;
	}
	
	public void onClick(View v) {

	}
	
	private class ViewHolder {
		TextView tvSSID;
		TextView tvBSSID;
		TextView tvCapabilities;
		CheckBox cb;
		ImageView iv;
	}


}