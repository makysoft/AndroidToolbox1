package com.tresksoft.toolbox.BatteryManager;

public class BatteryInfo {

	// Informaci�n Bater�a
	public String time;
	public int level;
	public int scale;
	public int health;
	public int plugged;
	public int status;
	public int voltage;
	public int temp;
	public String tech;	
	
	public BatteryInfo() {
		
	}
	
	public String toString() {
		return time + " " + String.valueOf(level) + " " + String.valueOf(scale) + " " + String.valueOf(health) +
		  String.valueOf(plugged) + " " + String.valueOf(status) + " " + String.valueOf(voltage) + " " +
		  String.valueOf(temp) + " " + tech;
		
	}
	
}
