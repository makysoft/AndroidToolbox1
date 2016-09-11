package com.tresksoft.toolbox.data;

import java.util.ArrayList;

import com.tresksoft.toolbox.DatabaseHelper;

public class SQLFunctions {
	
	public static ArrayList<CProcess> getProcesosDB(DatabaseHelper dh) {
		return dh.selectAll();
	}
	
	public static ArrayList<CProcess> getProcesosDB(DatabaseHelper dh, String selected) {
		return dh.select("%", "%", selected);
	}
	
	public static boolean exist(DatabaseHelper dh, String pkgName) {
		ArrayList<CProcess> aProcess = dh.select(pkgName);
		if (aProcess.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean addProcess(DatabaseHelper dh, String pkgName, String process, String selected) {
		if (!exist(dh, pkgName)) {
			// Hacemos el insert
			dh.insert(pkgName, process, selected);
		}else{
			// Hacemos el update
			dh.update(pkgName, selected);
		}
		return false;
	}
	
}
