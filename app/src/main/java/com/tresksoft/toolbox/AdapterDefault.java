package com.tresksoft.toolbox;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tresksoft.toolbox.data.CItemDefault;

public class AdapterDefault extends BaseAdapter{

	private Context context;
	
	private List<CItemDefault> items;
	
	public AdapterDefault(Context context, List<CItemDefault> items) {
		this.context = context;
		this.items = items;
	}
	
	public int getCount() {
		return this.items.size();
	}
	
	public Object getItem(int position) {
		return this.items.get(position);
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_default, null);			
			
			holder = new ViewHolder();
			holder.tv1 = (TextView)convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView)convertView.findViewById(R.id.tv2);
			holder.iv = (ImageView)convertView.findViewById(R.id.image);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.tv1.setText(this.items.get(position).tv1);
		holder.tv2.setText(this.items.get(position).tv2);
		holder.iv.setImageDrawable(this.items.get(position).icon);
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView tv1;
		TextView tv2;
		ImageView iv;
	}
	
}
