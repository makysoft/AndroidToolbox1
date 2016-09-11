package com.tresksoft.toolbox;

import java.util.ArrayList;

import com.tresksoft.toolbox.data.CProcess;

public class ColeccionProcesos {

	private ArrayList<CProcess> coleccionProcesos;
	
	public ColeccionProcesos() {
		coleccionProcesos = new ArrayList<CProcess>();
	}
	
	public ColeccionProcesos(ArrayList<CProcess> coleccionProcesos) {
		this();
		this.coleccionProcesos = coleccionProcesos;
	}
	
	public void add(CProcess proceso) {
		coleccionProcesos.add(proceso);
	}
	
	public CProcess buscar(CProcess proceso) {
		CProcess ret = null;
		for (CProcess proc: coleccionProcesos){
			if (proc.getPkgName().equals(proceso.getPkgName())){
				ret = proc;
			}
		}
		return ret;
	}
	
	public ArrayList<CProcess> getColeccion() {
		return coleccionProcesos;
	}
	
}
