package com.tresksoft.toolbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.mobeng.libs.LibBatteryManager;
import com.tresksoft.batterymanager.Battery;
import com.tresksoft.batterymanager.BatteryHistory;
import com.tresksoft.batterymanager.BatteryInfo;

public class ActivityBatteryManager extends Activity{

	BatteryHistory history;
	
	ProcessApplication app;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_battery_manager);
	}
	
	private BroadcastReceiver batInfoReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			
			BatteryInfo batteryInfo = new BatteryInfo();
			
			batteryInfo.level = intent.getIntExtra("level", 0);
			batteryInfo.scale = intent.getIntExtra("scale", 100);
			batteryInfo.health = intent.getIntExtra("health", 1);
			batteryInfo.plugged = intent.getIntExtra("plugged", 0);
			batteryInfo.status = intent.getIntExtra("status", 1);
			batteryInfo.voltage = intent.getIntExtra("voltage", 0);
			batteryInfo.temp = intent.getIntExtra("temperature", 0);
			batteryInfo.tech = intent.getStringExtra("technology");
			Time time = new Time();
			time.setToNow();
			batteryInfo.time = time.format2445();
			
			String batInfo = String.valueOf((batteryInfo.level*100)/batteryInfo.scale);
			TextView tv = (TextView)findViewById(R.id.battery_info);
			tv.setText("Battery remaining " + batInfo + " %");
			
			// Battery Health
			String healthStatus = new String("");
			switch (batteryInfo.health) {
			case 7:
				healthStatus = "Cold";
				break;
			case 4:
				healthStatus = "Dead";
				break;
			case 2:
				healthStatus = "Good";
				break;
			case 3:
				healthStatus = "Overheat";
				break;
			case 5:
				healthStatus = "Over Voltage";
				break;
			case 1:
				healthStatus = "Unknown";
				break;
			case 6:
				healthStatus = "Unspecified Failure";
				break;
			}
			
//			tv = (TextView)findViewById(R.id.battery_health);
//			tv.setText(String.valueOf("Battery status: " + healthStatus));
		
			// Battery Plugged
			String pluggedStatus = new String("");
			switch(batteryInfo.plugged) {
			case 0:
				pluggedStatus = "Battery";
				break;
			case 1:
				pluggedStatus = "AC";
				break;
			case 2:
				pluggedStatus = "USB";
				break;
			}
			
//			tv = (TextView)findViewById(R.id.battery_plugged);
			//tv.setText(String.valueOf("Phone plugged on " + pluggedStatus));
			
			// Battery Status
			String statusBattery = new String("");
			switch(batteryInfo.status) {
			case 2:
				statusBattery = "Charging";
				break;
			case 3:
				statusBattery = "Discharging";
				break;
			case 5:
				statusBattery = "Full";
				break;
			case 4:
				statusBattery = "Not charging";
				break;
			case 1:
				statusBattery = "Unknown";
				break;
			}
			
//			tv = (TextView)findViewById(R.id.battery_status);
//			tv.setText(String.valueOf("Battery status: " + statusBattery));
			
			// Battery Voltage
//			tv = (TextView)findViewById(R.id.battery_voltage);
//			tv.setText("Battery Voltage " + batteryInfo.voltage + " mV");
			
			// Battery Temperature
			// Farenheit to celsius C=(F-32)*5/9
//			tv = (TextView)findViewById(R.id.battery_temp);
//			tv.setText("Battery Temperature: " + batteryInfo.temp + "º");
			
			// Battery Technology
//			tv = (TextView)findViewById(R.id.battery_tech);
//			tv.setText("Battery Technology " + batteryInfo.tech);
			
			// Montar cadena a escribir en log

		}
	};
	
	

	
	private void registrarListener() {
		// Iniciar receiver
		registerReceiver(batInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}
	
	private void desregistrarListener() {
		// Finalizar receiver
		unregisterReceiver(batInfoReceiver);
	}

	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		registrarListener();
	}
	
	public void onDetachedFromWindow() {
		desregistrarListener();
		super.onDetachedFromWindow();
	}
	
}
