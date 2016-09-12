package com.tresksoft.toolbox.Move2SDManager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.treksoft.apps.LibApps;
import com.tresksoft.toolbox.data.CAplicacion;

public class LibMove2SD {

	private Context context;
	
	public LibMove2SD(Context context) {
		this.context = context;
	}
	
	public void getMoveableApps() {
		List<CAplicacion> aplicaciones = getAplicacionesInstaladas();
		
	}
	
	private ArrayList<CAplicacion> getAplicacionesInstaladas() {
		List<CAplicacion> retval = null;
		
		LibApps libApps = new LibApps(this.context);
		try {
			retval = libApps.getAplicacionesInstaladas();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return (ArrayList<CAplicacion>) retval;
	}
	
}
