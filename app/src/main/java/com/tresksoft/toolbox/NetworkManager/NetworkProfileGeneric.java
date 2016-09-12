package com.tresksoft.toolbox.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class NetworkProfileGeneric {

	protected Context context;
	
	public NetworkProfileGeneric(Context context) {
		this.context = context;
	}
	
	public List<NetworkProfile> getPerfiles() {
		return new ArrayList<NetworkProfile>();
	}
	
	public void crearPerfil(NetworkProfile profile) {
		
	}
	
	public void borrarPerfil(String nombreperfil) {
		
	}
	
	public void actualizarPerfil(long id, NetworkProfile profile) {
	
	}
	
	public void close() {
		
	}
}
