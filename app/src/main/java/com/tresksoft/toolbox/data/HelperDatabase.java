package com.tresksoft.toolbox.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

public class HelperDatabase extends SQLiteOpenHelper{
	
	private final Context context;
	public SQLiteDatabase dbSqlite;
	
	public HelperDatabase(Context context) {
		super(context, Constants.TBL_NAME, null, 1);
		this.context = context;
	}
	
	public void onCreate(SQLiteDatabase db) {
		createDB();
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}
	
	public void createDatabase(){
		createDB();
	}
	
	private void createDB(){
		boolean dbExist = DBExists();
		if(!dbExist) {
			copyDBFromResource();
		}
	}
	
	private boolean DBExists() {
		SQLiteDatabase db = null;
		
		try {
			String databasePath = Constants.DATABASE_PATH + Constants.DB_NAME;
			db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
			db.setLocale(Locale.getDefault());
			db.setLockingEnabled(true);
			db.setVersion(1);
		}catch(SQLiteException e) {
			e.printStackTrace();
		}
		
		if (db != null){
			db.close();
		}
		
		return db != null ? true : false;
	}
	
	private void copyDBFromResource() {
		InputStream inputStream = null;
		OutputStream outStream = null;
		String dbFilePath = Constants.DATABASE_PATH + Constants.DB_NAME;
		try {
			File f = new File(dbFilePath);
			if(!f.exists()){
				File f2 = new File(Constants.DATABASE_PATH);
				f2.mkdirs();
				f.createNewFile();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			inputStream = context.getAssets().open(Constants.DB_NAME);
			outStream = new FileOutputStream(dbFilePath);
			
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0){
				outStream.write(buffer, 0, length);
			}
			outStream.flush();
			outStream.close();
			inputStream.close();
		}catch (IOException e) {
			throw new Error("Ha ocurrido un problema copiando la base de datos.");
		}
		
	}
	
	public void openDatabase() throws SQLException {
		String myPath = Constants.DATABASE_PATH + Constants.DB_NAME;
		dbSqlite = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	public synchronized void close() {
		if (dbSqlite != null)
			dbSqlite.close();
		super.close();
	}
	
	public Cursor getCursor(){
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(Constants.TBL_NAME);
		String[] asColumnsToReturn = new String[] {
				Constants.COLUMN_ID, Constants.COLUMN_PKGNAME, Constants.COLUMN_SELECTED };
		Cursor mCursor = queryBuilder.query(dbSqlite, asColumnsToReturn, null, null, null, null, "process_desc ASC");
		return mCursor;
	}

}
