package com.tresksoft.toolbox.SpeedSettingsManager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

import com.mobeng.libs.LibBase;
import com.mobeng.libs.LibBluetooth;
import com.tresksoft.Views.Main;
import com.tresksoft.toolbox.Home.ProcessApplication;
import com.tresksoft.apn.ControladorAPN;
import com.tresksoft.apn.FactoryAPN;
import com.tresksoft.toolbox.R;

public class ActivitySpeedSettings extends Activity
	implements OnClickListener, OnSeekBarChangeListener{

	private ToggleButton tb_bluetooth;
	private ToggleButton tb_apn;
	private LinearLayout linear_layout_input;
	private LinearLayout linear_layout_select;
	private LinearLayout linear_layout_ringer_volume;
	private LinearLayout linear_layout_lock;
	private LinearLayout linear_layout_install;
	private LinearLayout linear_layout_debug;

	private SeekBar seekbar_volume_alarm;
	private SeekBar seekbar_volume_dtmf;
	private SeekBar seekbar_volume_music;
	private SeekBar seekbar_volume_notification;
	private SeekBar seekbar_volume_ring;
	private SeekBar seekbar_volume_system;
	private SeekBar seekbar_volume_voice_call;
	
	private int volumeAlarm;
	private int volumeDTMF;
	private int volumeMusic;
	private int volumeNotification;
	private int volumeRing;
	private int volumeSystem;
	private int volumeVoiceCall;
	
	private int maxVolumeAlarm;
	private int maxVolumeDTMF;
	private int maxVolumeMusic;
	private int maxVolumeNotification;
	private int maxVolumeRing;
	private int maxVolumeSystem;
	private int maxVolumeVoiceCall;
	
	public static Intent intent(String component, String param2) {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(component, param2));
		return intent;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_speed_settings);
		
		// Buscamos los items
		this.tb_bluetooth = (ToggleButton) findViewById(R.id.toggle_activate_bluetooth);
		this.tb_apn = (ToggleButton) findViewById(R.id.toggle_activate_apn);
		this.linear_layout_input = (LinearLayout) findViewById(R.id.settings_input_method);
		this.linear_layout_select = (LinearLayout) findViewById(R.id.settings_select_input_method);
		this.linear_layout_ringer_volume = (LinearLayout) findViewById(R.id.settings_ringer_volume_method);
		this.linear_layout_lock = (LinearLayout) findViewById(R.id.settings_security_location_method);
		this.linear_layout_install = (LinearLayout) findViewById(R.id.settings_security_install_method);
		this.linear_layout_debug = (LinearLayout) findViewById(R.id.settings_security_debug_method);
		this.seekbar_volume_alarm = (SeekBar) findViewById(R.id.seekbar_volume_alarm);
		this.seekbar_volume_dtmf = (SeekBar) findViewById(R.id.seekbar_volume_dtmf);
		this.seekbar_volume_music = (SeekBar) findViewById(R.id.seekbar_volume_music);
		this.seekbar_volume_notification = (SeekBar) findViewById(R.id.seekbar_volume_notification);
		this.seekbar_volume_ring = (SeekBar) findViewById(R.id.seekbar_volume_ring);
		this.seekbar_volume_system = (SeekBar) findViewById(R.id.seekbar_volume_system);
		this.seekbar_volume_voice_call = (SeekBar) findViewById(R.id.seekbar_volume_voice_call);
		
		// Establece los valores m�ximos de volumen
		maxVolumeAlarm = LibBase.getMaxVolume(this, LibBase.VOLUME_ALARM);
		maxVolumeDTMF = LibBase.getMaxVolume(this, LibBase.VOLUME_DTMF);
		maxVolumeMusic = LibBase.getMaxVolume(this, LibBase.VOLUME_MUSIC);
		maxVolumeNotification = LibBase.getMaxVolume(this, LibBase.VOLUME_NOTIFICATION);
		maxVolumeRing = LibBase.getMaxVolume(this, LibBase.VOLUME_RING);
		maxVolumeSystem = LibBase.getMaxVolume(this, LibBase.VOLUME_SYSTEM);
		maxVolumeVoiceCall = LibBase.getMaxVolume(this, LibBase.VOLUME_VOICE_CALL);
		
		this.seekbar_volume_alarm.setMax(maxVolumeAlarm);
		this.seekbar_volume_dtmf.setMax(maxVolumeDTMF);
		this.seekbar_volume_music.setMax(maxVolumeMusic);
		this.seekbar_volume_notification.setMax(maxVolumeNotification);
		this.seekbar_volume_ring.setMax(maxVolumeRing);		
		this.seekbar_volume_system.setMax(maxVolumeSystem);
		this.seekbar_volume_voice_call.setMax(maxVolumeVoiceCall);
		
		// Actualizamos el volumen
		actualizaVolumen();
		
		
		// Se configuran los items
			
		// Bluetooth listener
		if (LibBluetooth.isEnabled() == 0) {
			this.tb_bluetooth.setChecked(false);
		} else {
			this.tb_bluetooth.setChecked(true);
		}

		// APN listener
		int index = LibBase.getDefaultAPN(this);
		if (LibBase.isEnableAPN(this, index)) {
			this.tb_apn.setChecked(true);
		} else {
			this.tb_apn.setChecked(false);
		}
		
		// Configurar listeners
		this.tb_bluetooth.setOnClickListener(this);
		this.tb_apn.setOnClickListener(this);
		this.linear_layout_input.setOnClickListener(this);
		this.linear_layout_select.setOnClickListener(this);
		this.linear_layout_ringer_volume.setOnClickListener(this);
		this.linear_layout_lock.setOnClickListener(this);
		this.linear_layout_install.setOnClickListener(this);
		this.linear_layout_debug.setOnClickListener(this);
		
		this.seekbar_volume_alarm.setOnSeekBarChangeListener(this);
		this.seekbar_volume_dtmf.setOnSeekBarChangeListener(this);
		this.seekbar_volume_music.setOnSeekBarChangeListener(this);
		this.seekbar_volume_notification.setOnSeekBarChangeListener(this);
		this.seekbar_volume_ring.setOnSeekBarChangeListener(this);
		this.seekbar_volume_system.setOnSeekBarChangeListener(this);
		this.seekbar_volume_voice_call.setOnSeekBarChangeListener(this);
	}
	
	public void onHomeClick(View v) {
		Intent intent = new Intent(this, Main.class);
    	startActivity(intent);
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		// *********************************** BLUETOOTH
		case R.id.toggle_activate_bluetooth:
			if (this.tb_bluetooth.isChecked()) {
				// Activar bluetooth
				int enable = LibBluetooth.isEnabled();
				if (enable == 0) {
					LibBluetooth.enable(ActivitySpeedSettings.this);
				}
			}else{
				// Desactivar bluetooth
				LibBluetooth.disable();
			}
			break;
		// *********************************** APN
		case R.id.toggle_activate_apn:
			new Thread() {
				public void run() {		
					ProcessApplication app = (ProcessApplication) getApplication();
					FactoryAPN factory = new FactoryAPN(app.apiLevel);
					ControladorAPN controladorAPN = factory.getControlador();
					if (!tb_apn.isChecked()) {
						controladorAPN.disable(ActivitySpeedSettings.this);
					} else {
						controladorAPN.enable(ActivitySpeedSettings.this);
					}
				}
			}.start();
			break;
		// *********************************** AUDIO AND VIDEO SETTINGS
		case R.id.settings_ringer_volume_method:
			startActivityForResult(new Intent(android.provider.Settings.ACTION_SOUND_SETTINGS), 0);
			break;
		// *********************************** SECURITY SETTINGS
		case R.id.settings_security_location_method:
			startActivityForResult(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS), 0);
			break;
		case R.id.settings_security_install_method:
			startActivityForResult(new Intent(android.provider.Settings.ACTION_APPLICATION_SETTINGS), 0);
			break;
		case R.id.settings_security_debug_method:
			//startActivityForResult(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS), 0);
			startActivity(intent("com.android.settings", "com.android.settings.DevelopmentSettings"));
			//startActivity(intent("com.android.settings", "com.android.settings.fuelgauge.PowerUsageSummary"));
			break;
		// *********************************** INPUT
		case R.id.settings_input_method:
			startActivity(intent("com.android.settings", "com.android.settings.LanguageSettings"));
			break;
		case R.id.settings_select_input_method:
			((InputMethodManager)getSystemService("input_method")).showInputMethodPicker();
			break;
		}
	}
	
	protected void onResume() {
		super.onResume();
		actualizaVolumen();
	}
	
	protected void onDestroy() {
		super.onDestroy();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			LibBase.adjustVolume(this, LibBase.VOLUME_DOWN, 0);
			actualizaVolumen();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			LibBase.adjustVolume(this, LibBase.VOLUME_UP, 0);
			actualizaVolumen();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private void actualizaVolumen() {
		volumeAlarm = LibBase.getVolume(this, LibBase.VOLUME_ALARM);
		volumeDTMF = LibBase.getVolume(this, LibBase.VOLUME_DTMF);
		volumeMusic = LibBase.getVolume(this, LibBase.VOLUME_MUSIC);
		volumeNotification = LibBase.getVolume(this, LibBase.VOLUME_NOTIFICATION);
		volumeRing = LibBase.getVolume(this, LibBase.VOLUME_RING);
		volumeSystem = LibBase.getVolume(this, LibBase.VOLUME_SYSTEM);
		volumeVoiceCall = LibBase.getVolume(this, LibBase.VOLUME_VOICE_CALL);
		
		this.seekbar_volume_alarm.setProgress(volumeAlarm);
		this.seekbar_volume_dtmf.setProgress(volumeDTMF);
		this.seekbar_volume_music.setProgress(volumeMusic);
		this.seekbar_volume_notification.setProgress(volumeNotification);
		this.seekbar_volume_ring.setProgress(volumeRing);
		this.seekbar_volume_system.setProgress(volumeSystem);
		this.seekbar_volume_voice_call.setProgress(volumeVoiceCall);
	}

	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		int type = 0;
		
		switch (seekBar.getId()) {
		case R.id.seekbar_volume_alarm:
			type = LibBase.VOLUME_ALARM;
			break;
		case R.id.seekbar_volume_dtmf:
			type = LibBase.VOLUME_DTMF;
			break;
		case R.id.seekbar_volume_music:
			type = LibBase.VOLUME_MUSIC;
			break;
		case R.id.seekbar_volume_notification:
			type = LibBase.VOLUME_NOTIFICATION;
			break;
		case R.id.seekbar_volume_ring:
			type = LibBase.VOLUME_RING;
			break;
		case R.id.seekbar_volume_system:
			type = LibBase.VOLUME_SYSTEM;
			break;
		case R.id.seekbar_volume_voice_call:
			type = LibBase.VOLUME_VOICE_CALL;
			break;
		}
		LibBase.setVolume(this, type, progress, 0);
		// Forzamos la actualizaci�n de las barra de volumen por si alguno depende de otro
		actualizaVolumen();
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	

}
