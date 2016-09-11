package com.tresksoft.wifi;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiManager;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tresksoft.toolbox.R;
import com.tresksoft.toolbox.data.CCodificacion;

public class WifiLib {
	
	public static boolean inicializarWifi(final Context context, final WifiManager mainWifi) {
		boolean retval = false;
		
		if (!isWifiEnabled(mainWifi)) 
			retval = wifiOn(mainWifi);
		else
			retval = true;
		
		return retval;
		
	}
	
	public static ArrayList<String> obtenerWifisConfiguradas(Context context) {
		ArrayList<String> wifis = new ArrayList<String>();
		WifiManager mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// Comprobamos y activamos Wifi
		if (wifiOn(mainWifi)) {
			inicializarWifi(context, mainWifi);
		}
		// Obtenemos el listado de wifis configuradas
		List<WifiConfiguration> configuredWifis = mainWifi.getConfiguredNetworks();
		for (WifiConfiguration wifi: configuredWifis) {
			wifis.add(wifi.SSID);
		}
		return wifis;
	}
	
	public static boolean wifiOn(WifiManager mainWifi) {
		return mainWifi.setWifiEnabled(true);
	}
	
	public static boolean wifiOff(WifiManager mainWifi) {
		return mainWifi.setWifiEnabled(false);
	}
	
	public static int buscarWifiConfigurada(WifiManager mainWifi, String wifi) {
		List<WifiConfiguration> wifis = mainWifi.getConfiguredNetworks();
		return buscarWifiConfigurada(wifi, wifis);
	}
	
	public static boolean isWifiEnabled(WifiManager mainWifi) {
		return mainWifi.isWifiEnabled();
	}
	
	public static int buscarWifiConfigurada(String wifi, List<WifiConfiguration> redesConfiguradas) {
		for(WifiConfiguration red: redesConfiguradas) {
				if (wifi != null && red.SSID != null) {
					if (red.SSID.equals("\"".concat(wifi).concat("\"")))
						return red.networkId;
				}
		}
		return -1;
	}
	
	public static int buscarWifiConfigurada(WifiScanItem wifi, List<WifiConfiguration> redesConfiguradas) {
		for(WifiConfiguration red: redesConfiguradas) {
			if (wifi.BSSID != null && red.BSSID != null) {
				if (wifi.BSSID.equals(red.BSSID))
					return red.networkId;
			}else{
				if (wifi.SSID != null && red.SSID != null) {
					if (red.SSID.equals("\"".concat(wifi.SSID).concat("\"")))
						return red.networkId;
				}
			}
		}
		return -1;
	}
	
	public static int addWifi(WifiManager mainWifi, String ssid, String seguridad, String password) {
		WifiConfiguration wifiConfig = new WifiConfiguration();
		wifiConfig.SSID = "\"" + ssid + "\"";
		if (seguridad.equals("Open")) {
			wifiConfig.allowedAuthAlgorithms.clear();
			wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wifiConfig.allowedKeyManagement.set(KeyMgmt.NONE);
			wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			wifiConfig.status = WifiConfiguration.Status.ENABLED;	
		} else if (seguridad.equals("WEP (ASCII)") || seguridad.equals("WEP (Hex)")) {
			wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wifiConfig.allowedKeyManagement.set(KeyMgmt.NONE);
			wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			if (seguridad.equals("WEP (ASCII)")) {
				wifiConfig.wepKeys[0] = "\"" + password + "\"";
			} else {
				wifiConfig.wepKeys[0] = password;
			}
			wifiConfig.wepTxKeyIndex = 0;
		} else if (seguridad.equals("WPA/WPA2 PSK")) {
			wifiConfig.status = WifiConfiguration.Status.ENABLED;
			wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			wifiConfig.preSharedKey = "\"" + password + "\"";
		} else if (seguridad.equals("802.1x EAP")) {
			
		} else {
			return -1;
		}
		
		int netId = -1;
		if ((netId = buscarWifiConfigurada(mainWifi, ssid)) != -1) {
			wifiConfig.networkId = netId;
			mainWifi.disableNetwork(netId);
			netId = mainWifi.updateNetwork(wifiConfig);
		} else {
			netId = mainWifi.addNetwork(wifiConfig);
		}
		// Activamos la nueva configuración
		mainWifi.enableNetwork(netId, true);
		// Guardamos la configuración
		mainWifi.saveConfiguration();
		
		return netId;
	}
	
	public static AlertDialog dialogAddWifi(final Context context, final WifiManager mainWifi, String SSID, int security) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		final View dialoglayout = inflater.inflate(R.layout.custom_dialog_add_profile_wifi, null);
		final EditText etSSID = (EditText) dialoglayout.findViewById(R.id.ssid_name);
		final EditText et = (EditText) dialoglayout.findViewById(R.id.wifi_password);
		final TextView tv = (TextView) dialoglayout.findViewById(R.id.tv_password);
		final CheckBox cb = (CheckBox) dialoglayout.findViewById(R.id.wifi_show_password);		
		final Spinner spinner = (Spinner) dialoglayout.findViewById(R.id.encriptacion);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.encriptacion_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Object item = parent.getItemAtPosition(pos);
				if (item.toString().equals("Open")) {
					tv.setVisibility(View.INVISIBLE);
					et.setVisibility(View.INVISIBLE);
					cb.setVisibility(View.INVISIBLE);
				}else{
					tv.setVisibility(View.VISIBLE);
					et.setVisibility(View.VISIBLE);
					cb.setVisibility(View.VISIBLE);
				}
			}
			
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				EditText et = (EditText) dialoglayout.findViewById(R.id.wifi_password);
				if (!isChecked) {
					et.setTransformationMethod(new PasswordTransformationMethod());
				}else{
					et.setTransformationMethod(null);
				}
			}
			
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Configurar WI-FI");
		builder.setView(dialoglayout);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   	int retval = WifiLib.addWifi(mainWifi, etSSID.getText().toString(), spinner.getSelectedItem().toString(), et.getText().toString());
	        	   	if (retval == -1) {
	        	   		Toast.makeText(context, context.getResources().getString(R.string.msg_add_connection_failure), Toast.LENGTH_SHORT).show();
	        	   	} else {
	        	   		Toast.makeText(context, context.getResources().getString(R.string.msg_add_connection), Toast.LENGTH_SHORT).show();
	        	   	}
	        	   	
	        	    dialog.dismiss();
	           }
	       });
	    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	           }
	       });
	    	    
	    // Inicializar los campos
		etSSID.setText(SSID);
		// Si WPA corregimos security
		if (security == 2)
			security = 3;
		spinner.setSelection(security);
	    
		AlertDialog alert = builder.create();
		alert.show();
		
		return alert;
	}
	
	public static AlertDialog dialogAddWifi(Context context, final WifiManager mainWifi) {
		return dialogAddWifi(context, mainWifi, "",0);
	}
	
	public static CCodificacion getSecurity(Context context, BitSet authAlgorithm,
			BitSet groupCipher,
			BitSet keyMgmt,
			BitSet pairwiseCipher,
			BitSet protocol) {
		
		Resources res = context.getResources();
		String[] codificaciones = res.getStringArray(R.array.encriptacion_prompt);
		
		if (Integer.toHexString(authAlgorithm.hashCode()).equals("4d2") &&
				Integer.toHexString(keyMgmt.hashCode()).equals("4d3")) {
			return new CCodificacion(0, codificaciones[0]);
		} else if (Integer.toHexString(authAlgorithm.hashCode()).equals("4d1")) {
			return new CCodificacion(1, codificaciones[1]);
		} else if (Integer.toHexString(authAlgorithm.hashCode()).equals("4d2") &&
				Integer.toHexString(keyMgmt.hashCode()).equals("4d0")) {
			return new CCodificacion(3, codificaciones[2]);
		} else {
			return new CCodificacion(-1, "none");
		}
		
	}

}
