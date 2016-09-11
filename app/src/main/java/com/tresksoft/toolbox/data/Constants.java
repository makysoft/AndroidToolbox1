package com.tresksoft.toolbox.data;

public class Constants {
	
	public static boolean gEmulatorMode = true;
	
	public static final String DATABASE_PATH = "/data/data/com.tresksoft.processmanager/databases/";
	public static final String DB_NAME = "process_manager.db";
	public static final Integer DB_VERSION = 10;
	public static final String TBL_NAME = "process";
	
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_PKGNAME = "pkg_name";
	public static final String COLUMN_PROCESSNAME_BASE64 = "process_name64";
	public static final String COLUMN_SELECTED = "selected";
	
	public static final String SHARED_PREFERENCES = "myCustomSharedPrefs";
	
	/**
	 * BatteryManager
	 */
	
	public static final int NUMERO_LINEAS_X = 6;
	public static final int NUMERO_LINEAS_Y = 10;
	
	public static final float bigGridPosX = 0.01f;
	public static final float bigGridPosY = 0.05f;
	public static final float bigGridWidth = 0.98f;
	public static final float bigGridHeight = 0.90f;
	
	public static final float smallGridPosX = 0.01f;
	public static final float smallGridPosY = 0.05f;
	public static final float smallGridWidth = 0.98f;
	public static final float smallGridHeight = 0.50f;
}

