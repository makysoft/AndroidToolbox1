package com.tresksoft.BatteryManager;

import java.util.ArrayList;

import android.text.format.Time;

public class BatteryHistory {

	public int indiceTo = 0;
	public int indiceFrom = 0;
	public Time dateFrom = new Time();
	public Time dateTo = new Time();
	public int periodoMinutos = 0;
	public ArrayList<BatteryInfo> batteryinfos = new ArrayList<BatteryInfo>();
	
	public BatteryHistory(ArrayList<BatteryInfo> batteryinfos, int periodoMinutos) {
		this.batteryinfos = batteryinfos;
		this.periodoMinutos = periodoMinutos;
		dateTo.parse(batteryinfos.get(batteryinfos.size()-1).time);
		dateFrom.parse(batteryinfos.get(batteryinfos.size()-1).time);
		dateFrom.minute -= periodoMinutos;
		dateFrom.normalize(false);
		indiceTo = batteryinfos.size()-1;
		for (int i = indiceTo; i >= 0; i--) {
			Time t = new Time();
			t.parse(batteryinfos.get(i).time);
			if(t.before(dateFrom)) {
				indiceFrom = i;
				break;
			}
		}
	}
	
	public ArrayList<BatteryInfo> avanzarPeriodo(int minutos) {
		ArrayList<BatteryInfo> ret = new ArrayList<BatteryInfo>();
		dateFrom.minute += minutos;
		dateFrom.normalize(false);
		dateTo.minute += minutos;
		dateTo.normalize(false);
		for (int i = batteryinfos.size()-1; i == 0; i--) {
			Time t = new Time();
			t.parse(batteryinfos.get(i).time);
			if (t.after(dateTo) && t.before(dateFrom)) {
				ret.add(batteryinfos.get(i));
			}
		}
		return ret;
	}
	
}
