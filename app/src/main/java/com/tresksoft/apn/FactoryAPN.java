package com.tresksoft.apn;

public class FactoryAPN {

	protected int apiLevel;
	
	public FactoryAPN (int apiLevel) {
		this.apiLevel = apiLevel;
	}
	
	public ControladorAPN getControlador() {
		return new ControladorAPNOld();
	}
}
