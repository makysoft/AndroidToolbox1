package com.mobeng.libs;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public class LibBluetooth {

	static final int DISCOVERABLE_DURATION = 0;
	static BluetoothAdapter adapter;
	
	public static boolean isSupport() {
		adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public static int isEnabled() {
		if (isSupport()) {
			if (!adapter.isEnabled()) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return -1;
		}
	}
	
	public static void enable(Activity act) {
		adapter.enable();
		activateDiscoverable(act);
/*		Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		act.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);*/
	}
	
	public static void disable() {
		adapter.disable();
	}
	
	public static void activateDiscoverable(Activity act) {
		Intent discoverableIntent = new
		Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
		act.startActivity(discoverableIntent);
	}
}
