package com.tresksoft.toolbox;

import java.util.ArrayList;

import com.tresksoft.toolbox.data.CProcess;
import com.tresksoft.toolbox.data.Constants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DatabaseHelper {

	private Context context;
	private SQLiteDatabase db;
	
	private SQLiteStatement insertStmt;
	private SQLiteStatement updateStmt;
	
	private static final String INSERT = "insert into " + Constants.TBL_NAME 
		+ " (" + Constants.COLUMN_ID + ", " + Constants.COLUMN_PKGNAME
		+ ", " + Constants.COLUMN_PROCESSNAME_BASE64
		+ ", " + Constants.COLUMN_SELECTED + ") values (?, ?, ?, ?)";
	
	private static final String UPDATE = "update " + Constants.TBL_NAME
		+ " SET " + Constants.COLUMN_SELECTED + " = ?"
		+ " WHERE " + Constants.COLUMN_PKGNAME + " = ?";
	
	public DatabaseHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();
		this.insertStmt = this.db.compileStatement(INSERT);
		this.updateStmt = this.db.compileStatement(UPDATE);
	}
	
	public void close () {
		db.close();
	}
	
	public long insert (String pkgName, String processName64, String selected){
		this.insertStmt.bindNull(1);
		this.insertStmt.bindString(2, pkgName);
		this.insertStmt.bindString(3, processName64);
		this.insertStmt.bindString(4, selected);
		return this.insertStmt.executeInsert();
	}
	
	public void update (String pkgName, String selected){
		this.updateStmt.bindString(1, selected);
		this.updateStmt.bindString(2, pkgName);
		this.updateStmt.execute();
	}
	
	public void deleteAll(){
		this.db.delete(Constants.TBL_NAME, null, null);
	}
	
	public ArrayList<CProcess> selectAll() {
		ArrayList<CProcess> list = new ArrayList<CProcess>();
		CProcess proceso;
		Cursor cursor = this.db.query(Constants.TBL_NAME, new String[] {Constants.COLUMN_PKGNAME, Constants.COLUMN_PROCESSNAME_BASE64, Constants.COLUMN_SELECTED},
				null, null, null, null, Constants.COLUMN_PKGNAME + " desc");
		if (cursor.moveToFirst()){
			do {
				proceso = new CProcess();
				proceso.setPkgName(cursor.getString(0));
				proceso.setProcessName64(cursor.getString(1));
				proceso.setProcessSelected(cursor.getString(2));
				list.add(proceso);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()){
			cursor.close();
		}
		return list;
	}
	
	public ArrayList<CProcess> select(String pkgName){
		ArrayList <CProcess> list = new ArrayList<CProcess>();
		CProcess proceso;
		Cursor cursor = this.db.query(Constants.TBL_NAME, new String[] {Constants.COLUMN_PKGNAME, Constants.COLUMN_PROCESSNAME_BASE64, Constants.COLUMN_SELECTED}, "pkg_name = '" + pkgName + "'", null, null, null, null);
		if(cursor.moveToFirst()){
			do {
				proceso = new CProcess();
				proceso.setPkgName(cursor.getString(0));
				proceso.setProcessName64(cursor.getString(1));
				proceso.setProcessSelected(cursor.getString(2));
				list.add(proceso);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()){
			cursor.close();
		}
		return list;
	}
	
	public ArrayList<CProcess> select(String pkgName, String processName, String selected) {
		ArrayList<CProcess> list = new ArrayList<CProcess>();
		CProcess proceso;
		Cursor cursor = this.db.query(Constants.TBL_NAME, new String[] {Constants.COLUMN_PKGNAME, Constants.COLUMN_PROCESSNAME_BASE64, Constants.COLUMN_SELECTED},
						Constants.COLUMN_PKGNAME + " like '" + pkgName + "' AND " + Constants.COLUMN_PROCESSNAME_BASE64 + " like '" + processName + "' AND "
						 + Constants.COLUMN_SELECTED + " like '" + selected + "'", null, null, null, null);
		if(cursor.moveToFirst()) {
			do {
				proceso = new CProcess();
				proceso.setPkgName(cursor.getString(0));
				proceso.setProcessName64(cursor.getString(1));
				proceso.setProcessSelected(cursor.getString(2));
				list.add(proceso);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}
	
/*	public Cursor getCursor(){
		Cursor mCursor = this.db.query(Constants.TBL_NAME, new String[] {Constants.COLUMN_PKGNAME, Constants.COLUMN_PROCESSNAME_BASE64, 
				Constants.COLUMN_SELECTED}, null, null, null, null, Constants.COLUMN_PKGNAME + " desc");
		return mCursor;
	}*/
	
	private static class OpenHelper extends SQLiteOpenHelper {
		
		OpenHelper(Context context){
			super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
			onCreate(this.getWritableDatabase());
		}

		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE IF NOT EXISTS " + Constants.TBL_NAME
					+ " ( " + Constants.COLUMN_ID + " INTEGER PRIMARY KEY, "
					+ Constants.COLUMN_PKGNAME + " TEXT, "
					+ Constants.COLUMN_PROCESSNAME_BASE64 + " TEXT, "
					+ Constants.COLUMN_SELECTED + " TEXT)"
					);
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + Constants.TBL_NAME);
			onCreate(db);
		}
		
	}
	
}
