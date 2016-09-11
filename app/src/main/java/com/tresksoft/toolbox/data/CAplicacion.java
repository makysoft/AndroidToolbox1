package com.tresksoft.toolbox.data;

import android.graphics.drawable.Drawable;

public class CAplicacion implements Cloneable{

	private String sNombreAplicacion;
	private String sPaquete;
	private CTamanhoBytes tamanhoAplicacion;
	private CTamanhoBytes cacheAplicacion;
	private CTamanhoBytes dataAplicacion;
	private Drawable iconAplicacion;
	private int flags;
	public boolean isInstallIntoSD;
	
	public CAplicacion() {

	}

	public Object clone() {
		Object obj = null;
		try {
			obj=super.clone();
		}catch(CloneNotSupportedException ex) {
			System.out.println("No se puede duplicar");
		}
		return obj;
	}
	
	public String getsNombreAplicacion() {
		return sNombreAplicacion;
	}

	public void setsNombreAplicacion(String sNombreAplicacion) {
		this.sNombreAplicacion = sNombreAplicacion;
	}

	public CTamanhoBytes getTamanhoAplicacion() {
		return tamanhoAplicacion;
	}
	
	public void setTamanhoAplicacion(CTamanhoBytes tamanhoAplicacion) {
		this.tamanhoAplicacion = tamanhoAplicacion;
	}
	
	public CTamanhoBytes getCacheAplicacion() {
		return cacheAplicacion;
	}
	
	public void setCacheAplicacion(CTamanhoBytes cacheAplicacion) {
		this.cacheAplicacion = cacheAplicacion;
	}
	
	public CTamanhoBytes getDataAplicacion() {
		return dataAplicacion;
	}
	
	public void setDataAplicacion(CTamanhoBytes dataAplicacion) {
		this.dataAplicacion = dataAplicacion;
	}
	
	public String getPaquete() {
		return this.sPaquete;
	}
	
	public void setPaquete(String sPaquete) {
		this.sPaquete = sPaquete;
	}
	
	public Drawable getIconAplicacion() {
		return this.iconAplicacion;
	}
	
	public void setIconAplicacion(Drawable iconAplicacion) {
		this.iconAplicacion = iconAplicacion;
	}
	
	public int getFlags() {
		return this.flags;
	}
	
	public void setFlags(int flags) {
		this.flags = flags;
	}
	
}
