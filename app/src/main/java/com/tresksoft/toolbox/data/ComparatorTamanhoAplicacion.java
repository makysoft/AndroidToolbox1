package com.tresksoft.toolbox.data;

import java.util.Comparator;

public class ComparatorTamanhoAplicacion implements Comparator{
	
	String tipoOrden = "ASC";
	
	public int compare(Object o1, Object o2) {
		CAplicacion a1 = (CAplicacion) o1;
		CAplicacion a2 = (CAplicacion) o2;
		if (tipoOrden.equals("ASC")) {
			return a1.getTamanhoAplicacion().bytes.compareTo(a2.getTamanhoAplicacion().bytes);
		}else{
			return a2.getTamanhoAplicacion().bytes.compareTo(a1.getTamanhoAplicacion().bytes);
		}
	}
	
	public boolean equals(Object o) {
		return this == o;
	}
	
	public String getTipoOrden() {
		return tipoOrden;
	}
	
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}

}
