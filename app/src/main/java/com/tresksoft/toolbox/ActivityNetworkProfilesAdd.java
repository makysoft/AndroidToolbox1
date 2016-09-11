package com.tresksoft.toolbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.tresksoft.Home.ActivityMain;
import com.tresksoft.network.NetworkData;
import com.tresksoft.network.NetworkProfile;
import com.tresksoft.network.NetworkProfileFactory;
import com.tresksoft.network.NetworkProfileGeneric;

public class ActivityNetworkProfilesAdd extends Activity{

	NetworkProfileFactory factory;
	NetworkProfileGeneric profileGeneric;
	private ProcessApplication app;
	private NetworkProfile profile;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_network_profile_add);
		
		// Configurar listener
		RadioButton cb = (RadioButton) findViewById(R.id.profile_type_dhcp);
		cb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				desactivarCampos();
			}
		});
		
		cb = (RadioButton) findViewById(R.id.profile_type_manual);
		cb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activarCampos();
			}
		});		
		
		app = (ProcessApplication) getApplication();
		profile = app.network_profile;
		if (profile != null) {
			rellenarCampos();
		} else {
			// Selecciona por defecto el tipo de red manual
			cb.performClick();
		}
		// Create factory
		factory = new NetworkProfileFactory(this, "database");
		profileGeneric = factory.createNetworkProfile();
		
	}
	
	private void activarCampos() {
		EditText edittext = (EditText) findViewById(R.id.profile_ip);
		edittext.setEnabled(true);
		edittext = (EditText) findViewById(R.id.profile_mask);
		edittext.setEnabled(true);
		edittext = (EditText) findViewById(R.id.profile_gateway);
		edittext.setEnabled(true);
		edittext = (EditText) findViewById(R.id.profile_dns1);
		edittext.setEnabled(true);
		edittext = (EditText) findViewById(R.id.profile_dns2);
		edittext.setEnabled(true);
	}
	
	private void desactivarCampos() {
		EditText edittext = (EditText) findViewById(R.id.profile_ip);
		edittext.setEnabled(false);
		edittext = (EditText) findViewById(R.id.profile_mask);
		edittext.setEnabled(false);
		edittext = (EditText) findViewById(R.id.profile_gateway);
		edittext.setEnabled(false);
		edittext = (EditText) findViewById(R.id.profile_dns1);
		edittext.setEnabled(false);
		edittext = (EditText) findViewById(R.id.profile_dns2);
		edittext.setEnabled(false);		
	}
	
	private void rellenarCampos() {
		EditText edittext = (EditText) findViewById(R.id.profile_ssid);
		edittext.setText(this.profile.nombreperfil);
		if (this.profile.data.type_connection.equals("dhcp")) {
			RadioButton cb = (RadioButton) findViewById(R.id.profile_type_dhcp);
			cb.performClick();
		} else {
			RadioButton cb = (RadioButton) findViewById(R.id.profile_type_manual);
			cb.performClick();
		}
		edittext = (EditText) findViewById(R.id.profile_ip);
		edittext.setText(this.profile.data.ip);
		edittext = (EditText) findViewById(R.id.profile_mask);
		edittext.setText(this.profile.data.mask);
		edittext = (EditText) findViewById(R.id.profile_gateway);
		edittext.setText(this.profile.data.gateway);
		edittext = (EditText) findViewById(R.id.profile_dns1);
		edittext.setText(this.profile.data.dns1);
		edittext = (EditText) findViewById(R.id.profile_dns2);
		edittext.setText(this.profile.data.dns2);
	}
	
	public void onSaveProfile(View v) {
		NetworkProfile profile = new NetworkProfile();
		boolean empty = false;

		EditText edittext = (EditText) findViewById(R.id.profile_ssid);
		profile.nombreperfil = edittext.getText().toString();
		
		// Inicializamos los variables de la red
		NetworkData data = new NetworkData();
		
		data.ip = "";
		data.mask = "";
		data.gateway = "";
		data.dns1 = "";
		data.dns2 = "";
		
		RadioButton radio = (RadioButton) findViewById(R.id.profile_type_dhcp);
		if (radio.isChecked()) {
			data.type_connection = "dhcp";
		} else {
			data.type_connection = "manual";
			edittext = (EditText) findViewById(R.id.profile_ip);
			data.ip = edittext.getText().toString();

			edittext = (EditText) findViewById(R.id.profile_mask);
			data.mask = edittext.getText().toString();
			edittext = (EditText) findViewById(R.id.profile_gateway);
			data.gateway = edittext.getText().toString();
			edittext = (EditText) findViewById(R.id.profile_dns1);
			data.dns1 = edittext.getText().toString();
			edittext = (EditText) findViewById(R.id.profile_dns2);
			data.dns2 = edittext.getText().toString();
			
			if (profile.nombreperfil.equals("") || data.ip.equals("") || data.mask.equals("") || data.gateway.equals("") || data.dns1.equals("") || data.dns2.equals("")) {
				empty = true;
			}
		}
		
		if (!empty) {
			profile.data = data;
				
			profileGeneric.crearPerfil(profile);
			
			Intent intent = new Intent(this, ActivityNetworkProfiles.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
		} else {
			Toast.makeText(this, getResources().getString(R.string.toast_empty_fields), Toast.LENGTH_LONG).show();
		}
	}
	
	public void onHomeClick(View v) {
		Intent intent = new Intent(this, ActivityMain.class);
    	startActivity(intent);
	}
	
}
