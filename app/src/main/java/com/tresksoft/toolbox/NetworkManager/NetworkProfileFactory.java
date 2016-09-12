package com.tresksoft.toolbox.NetworkManager;

import android.content.Context;

public class NetworkProfileFactory {

	protected String type;
	private Context context;
	
	public NetworkProfileFactory(Context context, String type) {
		this.type = type;
		this.context = context;
	}
	
	public NetworkProfileGeneric createNetworkProfile() {
		if (type.equals("database")) {
			return new NetworkProfileBD(context);
		} else {
			return new NetworkProfileGeneric(context);
		}
	}
	
}
