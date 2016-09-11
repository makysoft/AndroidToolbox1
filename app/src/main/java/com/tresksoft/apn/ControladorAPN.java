package com.tresksoft.apn;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class ControladorAPN {

	final static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	final static Uri CONTENT_URI = Uri.parse("content://telephony/carriers");
	
	public boolean setDefaultAPN(Context context, int index) {
		boolean res = false;
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		
		values.put("apn_id", index);
		try {
			resolver.update(PREFERRED_APN_URI, values, null, null);
			Cursor c = resolver.query(PREFERRED_APN_URI, new String[] {"name","apn"}, "_id=" + index, null, null);
			if (c != null) {
				res = true;
				c.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public short getDefaultAPN (Context context) {
		Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
		c.moveToFirst();
		
		int index = c.getColumnIndex("_id");
		
		return c.getShort(index);
	}
	
	public void disable (Context context) {
		
	}
	
	public void enable (Context context) {
		
	}
	
	public boolean isEnable (Context context, int index) {
		return false;
	}
}
