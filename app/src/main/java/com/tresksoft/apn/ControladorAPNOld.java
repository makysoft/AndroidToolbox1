package com.tresksoft.apn;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ControladorAPNOld extends ControladorAPN{
	
	public void disable (Context context) {
		ContentResolver resolver = context.getContentResolver();
		try {
			Cursor c = resolver.query(CONTENT_URI, new String[] {"_id", "name", "apn", "type"}, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				
				int indx_id = c.getColumnIndex("_id");
				int indx_apn = c.getColumnIndex("apn");
				int indx_type = c.getColumnIndex("type");
				
				Short shortId = c.getShort(indx_id);
				String sApn = c.getString(indx_apn);
				String sType = c.getString(indx_type);
				
				ContentValues values = new ContentValues();
				sApn = sApn.concat("[disabled]");
				values.put("apn", sApn);
				
				if (sType == null) {
					values.putNull("type");
				}else{
					sType = sType.concat("[disabled]");
					values.put("type", sType);
				}
				
				resolver.update(CONTENT_URI, values, "_id=?", new String[]{String.valueOf(shortId)});
				
				c.moveToNext();
			}
			c.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void enable (Context context) {
		ContentResolver resolver = context.getContentResolver();
		try {
			Cursor c = resolver.query(CONTENT_URI, new String[] {"_id", "name", "apn", "type"}, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				
				int indx_id = c.getColumnIndex("_id");
				int indx_apn = c.getColumnIndex("apn");
				int indx_type = c.getColumnIndex("type");
				
				Short shortIndex = c.getShort(indx_id);
				String sApn = c.getString(indx_apn);
				String sType = c.getString(indx_type);		
				
				
				ContentValues values = new ContentValues();
				
				sApn = sApn.replace("[disabled]", "");
				values.put("apn", sApn);

				if (sType == null) {
					values.putNull("type");
				} else {
					sType = sType.replace("[disabled]", "");
					values.put("type", sType);
				}	
						
				resolver.update(CONTENT_URI, values, "_id=?", new String[]{String.valueOf(shortIndex)});
				
				c.moveToNext();
			}
			c.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isEnable (Context context, int index) {
		boolean res = false;
		ContentResolver resolver = context.getContentResolver();
		try {
			Cursor c = resolver.query(CONTENT_URI, new String[] {"name", "apn"}, "_id=" + index, null, null);
			c.moveToFirst();
			int indx = c.getColumnIndex("apn");
			String s = c.getString(indx);
			c.close();
			if (!s.contains("[disabled]")) {
				res = true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
}
