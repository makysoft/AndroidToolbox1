package com.tresksoft.network;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.tresksoft.toolbox.data.Constants;

public class NetworkProfileDH {

	// Variables estáticas
	private static final String TBL = "network_profiles";
	private static final String SCRIPT_CREATE_DATABASE = "CREATE TABLE IF NOT EXISTS " + TBL + " (ID PRIMARY KEY, "
														 + "NOMBRE_PERFIL TEXT, "
														 + "IP TEXT, "
														 + "MASCARA TEXT, "
														 + "PUERTA_ENLACE TEXT, "
														 + "DNS1 TEXT, "
														 + "DNS2 TEXT, "
														 + "TYPE TEXT)";
	private static final String SCRIPT_DROP_DATABASE = "DROP TABLE IF EXISTS " + TBL;
	
	private Context context;
	private SQLiteDatabase db;
	
	private SQLiteStatement insertStmt;
	private SQLiteStatement updateStmt;
	
	private static final String INSERT = "INSERT INTO " + TBL + " (ID, NOMBRE_PERFIL, IP, MASCARA, PUERTA_ENLACE, DNS1, DNS2, TYPE) values (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE " + TBL + " SET NOMBRE_PERFIL = ?, IP = ?, MASCARA = ?, PUERTA_ENLACE = ?, DNS1 = ?, DNS2 = ?, TYPE = ? WHERE ID = ?";
	
	public NetworkProfileDH(Context context) {
		this.context = context;
		OpenHelper oh = new OpenHelper(this.context);
		db = oh.getWritableDatabase();
		insertStmt = db.compileStatement(INSERT);
		updateStmt = db.compileStatement(UPDATE);
	}
	
	public long insert (NetworkProfile profile) {
		
		// Parseamos el insert
		insertStmt.bindNull(1);
		insertStmt.bindString(2, profile.nombreperfil);
		insertStmt.bindString(3, profile.data.ip);
		insertStmt.bindString(4, profile.data.mask);
		insertStmt.bindString(5, profile.data.gateway);
		insertStmt.bindString(6, profile.data.dns1);
		insertStmt.bindString(7, profile.data.dns2);
		insertStmt.bindString(8, profile.data.type_connection);
		// Retornamos el resultado de ejecutar el insert
		return insertStmt.executeInsert();
	}
	
	public void update (long id, NetworkProfile profile) {
	
		// Parseamos el update
		updateStmt.bindString(1, profile.nombreperfil);
		updateStmt.bindString(2, profile.data.ip);
		updateStmt.bindString(3, profile.data.mask);
		updateStmt.bindString(4, profile.data.gateway);
		updateStmt.bindString(5, profile.data.dns1);
		updateStmt.bindString(6, profile.data.dns2);
		updateStmt.bindString(7, profile.data.type_connection);
		updateStmt.bindLong(8, id);
		// Ejecutamos el update
		updateStmt.execute();
	}
	
	public void deleteAll() {
		db.delete(TBL, null, null);
	}
	
	public void delete(String nombreperfil) {
		db.delete(TBL, "NOMBRE_PERFIL='" + nombreperfil + "'", null);
	}
	
	public ArrayList<NetworkProfile> select(int id) {
		ArrayList<NetworkProfile> list = new ArrayList<NetworkProfile>();
		NetworkProfile network_profile;
		Cursor c = null;
		
		// Si id = -1 seleccionamos toda la tabla
		// Si id <> -1 seleccionamos los registros que coincidan con id
		if (id == -1) {
			c = db.query(TBL, new String[] {"NOMBRE_PERFIL", "IP", "MASCARA", "PUERTA_ENLACE", "DNS1", "DNS2", "TYPE"}, null, null, null, null, null);
		} else {
			c = db.query(TBL, new String[] {"NOMBRE_PERFIL", "IP", "MASCARA", "PUERTA_ENLACE", "DNS1", "DNS2", "TYPE"}, "ID = " + id, null, null, null, null);
		}
		if(c.moveToFirst()) {
			do {
				network_profile = new NetworkProfile();
				network_profile.nombreperfil = c.getString(0);
				network_profile.data.ip = c.getString(1);
				network_profile.data.mask = c.getString(2);
				network_profile.data.gateway = c.getString(3);
				network_profile.data.dns1 = c.getString(4);
				network_profile.data.dns2 = c.getString(5);
				network_profile.data.type_connection = c.getString(6);
				list.add(network_profile);
			}while (c.moveToNext());
		}
		if (c != null && !c.isClosed()) {
			c.close();
		}
		return list;
	}
	
	public void close () {
		db.close();
	}
	
/*	public Cursor getCursor() {
		Cursor c = db.query(TBL, new String[] {"NOMBRE_PERFIL", "IP", "MASCARA", "PUERTA_ENLACE", "DNS1", "DNS2", "TYPE"}, null, null, null, null, null);
		return c;
	}*/
	
	private static class OpenHelper extends SQLiteOpenHelper {
		
		OpenHelper(Context context) {
			super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
			onCreate(this.getWritableDatabase());
		}
		
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SCRIPT_CREATE_DATABASE);
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SCRIPT_DROP_DATABASE);
			onCreate(db);
		}
	}
}
