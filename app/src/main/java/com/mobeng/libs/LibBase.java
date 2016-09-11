package com.mobeng.libs;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

public class LibBase {

	/**
	 * Constantes
	 */
	public static final int RINGER_MODE_NORMAL = AudioManager.RINGER_MODE_NORMAL;
	public static final int RINGER_MODE_VIBRATE = AudioManager.RINGER_MODE_VIBRATE;
	public static final int RINGER_MODE_SILENT = AudioManager.RINGER_MODE_SILENT;
	
	public static final int VOLUME_ALARM = AudioManager.STREAM_ALARM;
	public static final int VOLUME_DTMF = AudioManager.STREAM_DTMF;
	public static final int VOLUME_MUSIC = AudioManager.STREAM_MUSIC;
	public static final int VOLUME_NOTIFICATION = AudioManager.STREAM_NOTIFICATION;
	public static final int VOLUME_RING = AudioManager.STREAM_RING;
	public static final int VOLUME_SYSTEM = AudioManager.STREAM_SYSTEM;
	public static final int VOLUME_VOICE_CALL = AudioManager.STREAM_VOICE_CALL;
	
	public static final int VOLUME_UP = AudioManager.ADJUST_RAISE;
	public static final int VOLUME_DOWN = AudioManager.ADJUST_LOWER;
	
	/**
	 * Funciones para gestionar la memoria de almacenamiento
	 */
	
	public static long getInternalMemoryAvailable() {
		StatFs fs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
		long blockSize = fs.getBlockSize();
		return fs.getAvailableBlocks()*blockSize;
	}
	
	public static long getInternalMemoryTotal() {
		StatFs fs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
		long blockSize = fs.getBlockSize();
		return fs.getBlockCount()*blockSize;
	}	
	
	public static long getSDMemoryAvailable() {
		long retval = 0;
		//if (Environment.isExternalStorageRemovable()) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			StatFs fs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
			long blockSize = fs.getBlockSize();
			retval = fs.getAvailableBlocks()*blockSize;
		}
		return retval;
	}
	
	public static long getSDMemoryTotal() {
		long retval = 0;
		//if (Environment.isExternalStorageRemovable()){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			StatFs fs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
			long blockSize = fs.getBlockSize();
			retval = fs.getBlockCount()*blockSize;
		}
		return retval;
	}	
	
	public static boolean isExternalStorageRemovable() {
		return Environment.isExternalStorageRemovable();
	}
	
	
	/**
	 * Funciones para configurar las APN
	 */
	/*
		"name"
	    "apn"
	    "user"
	    "password"
	    "mcc"
	    "mnc"
	    "numeric"
	    "type"
	*/
	final static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	final static Uri CONTENT_URI = Uri.parse("content://telephony/carriers");
	
	public static boolean setDefaultAPN(Context context, int index) {
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
	
	public static short getDefaultAPN(Context context) {
		Short id = 0;
		Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
		if (c.moveToFirst()) {
			int index = c.getColumnIndex("_id");
			id = c.getShort(index);
		}
		return id;
	}
	
	public static void disableAPN(Context context) {
		ContentResolver resolver = context.getContentResolver();
		try {
			Cursor c = resolver.query(CONTENT_URI, new String[] {"_id", "name", "apn", "type"}, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				int indx = c.getColumnIndex("_id");
				Short index = c.getShort(indx);
				indx = c.getColumnIndex("apn");
				String s_apn = c.getString(indx);
				indx = c.getColumnIndex("type");
				String s_type = c.getString(indx);
				
				ContentValues values = new ContentValues();
				s_apn = s_apn + "[disabled]";
				values.put("apn", s_apn);
				if (s_type == null) {
					values.putNull("type");
				}else{
					values.put("type", s_type + "[disabled]");
				}
				resolver.update(CONTENT_URI, values, "_id=?", new String[]{String.valueOf(index)});
				c.moveToNext();
			}
			c.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void enableAPN(Context context) {
		ContentResolver resolver = context.getContentResolver();
		try {
			Cursor c = resolver.query(CONTENT_URI, new String[] {"_id", "name", "apn", "type"}, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				int indx = c.getColumnIndex("_id");
				Short index = c.getShort(indx);
				indx = c.getColumnIndex("apn");
				String s = c.getString(indx);
				indx = c.getColumnIndex("type");
				String s_type = c.getString(indx);		
				
				s = s.replace("[disabled]", "");
				
				ContentValues values = new ContentValues();
								
				values.put("apn", s);
				if (s_type == null) {
					values.putNull("type");
				} else {
					values.put("type", s_type.replace("[disabled]", ""));
				}	
						
				resolver.update(CONTENT_URI, values, "_id=?", new String[]{String.valueOf(index)});
				
				c.moveToNext();
			}
			c.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isEnableAPN(Context context, int index) {
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
	
	/**
	 * 
	 * Audio functions
	 * 
	 * 
	 */
	
	public static void enableAudio(Context context, int mode) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int ringerMode = 0;
		if (mode == RINGER_MODE_NORMAL) {
			ringerMode = AudioManager.RINGER_MODE_NORMAL;
		} else if (mode == RINGER_MODE_SILENT){
			ringerMode = AudioManager.RINGER_MODE_SILENT;
		} else if (mode == RINGER_MODE_VIBRATE){
			ringerMode = AudioManager.RINGER_MODE_VIBRATE;
		}
		audioManager.setRingerMode(ringerMode);
	}
	
	public static int getMaxVolume(Context context, int stream) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		return audioManager.getStreamMaxVolume(stream);
	}
	
	public static int getVolume(Context context, int stream) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		return audioManager.getStreamVolume(stream);
	}
	
	public static void adjustVolume(Context context, int direction, int flags) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audioManager.adjustVolume(direction, flags);
	}
	
	public static void setVolume(Context context, int streamType, int index, int flags) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(streamType, index, flags);
	}
	
}
