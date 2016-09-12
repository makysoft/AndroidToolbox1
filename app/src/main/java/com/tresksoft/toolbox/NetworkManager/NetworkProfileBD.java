package com.tresksoft.toolbox.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class NetworkProfileBD extends NetworkProfileGeneric {

	private NetworkProfileDH dh;
	
	public NetworkProfileBD(Context context) {
		super(context);
		dh = new NetworkProfileDH(context);
	}
	
	public List<NetworkProfile> getPerfiles() {
		ArrayList<NetworkProfile> list_network_profiles = new ArrayList<NetworkProfile>();
		// Pasando como argumento -1 selecciona todos los registros
		list_network_profiles = dh.select(-1);
		
		return list_network_profiles;
	}
	
	public void crearPerfil(NetworkProfile profile) {
		// Inserta el perfil en la base de datos
		borrarPerfil(profile.nombreperfil);
		dh.insert(profile);
	}
	
	public void borrarPerfil(String nombreperfil) {
		dh.delete(nombreperfil);
	}
	
	public void actualizarPerfil(long id, NetworkProfile profile) {
		// Actualiza el perfil en la base de datos
		dh.update(id, profile);
	}
	
	public void close() {
		dh.close();
	}
	
}
