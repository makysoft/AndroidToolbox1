package com.mobeng.libs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.os.Environment;

import com.tresksoft.BatteryManager.BatteryInfo;

public class LibBatteryManager {

	public static BatteryInfo tokenizer(String cadena) {
		BatteryInfo batteryInfo = null;
		StringTokenizer st = new StringTokenizer(cadena);
		
		batteryInfo = new BatteryInfo();
		if (st.hasMoreElements())
			batteryInfo.time = st.nextToken();
		if (st.hasMoreElements())
			batteryInfo.level = Integer.valueOf(st.nextToken());
		if (st.hasMoreElements())
			batteryInfo.scale = Integer.valueOf(st.nextToken());
		if (st.hasMoreElements())
			batteryInfo.health = Integer.valueOf(st.nextToken());
		if (st.hasMoreElements())
			batteryInfo.plugged = Integer.valueOf(st.nextToken());
		if (st.hasMoreElements())
			batteryInfo.status = Integer.valueOf(st.nextToken());
		if (st.hasMoreElements())
			batteryInfo.voltage = Integer.valueOf(st.nextToken());
/*		if (st.hasMoreElements())
			batteryInfo.temp = Integer.valueOf(st.nextToken());*/
		if (st.hasMoreElements())
			batteryInfo.tech = st.nextToken();
		
		return batteryInfo;
	}
	
	public static ArrayList<BatteryInfo> leerLog() {
		ArrayList<BatteryInfo> log = new ArrayList<BatteryInfo>();
		File file = new File(Environment.getExternalStorageDirectory(), "battery.log");
		BatteryInfo batteryInfo = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				batteryInfo = LibBatteryManager.tokenizer(line);
				log.add(batteryInfo);
			}
			br.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return log;
	}
	
	public static int escribirLog(String buffer) throws Exception {
		// Escribir en LOG
		int ret = 0;
		
		File file = new File(Environment.getExternalStorageDirectory(), "battery.log");
		try {
			if (file.createNewFile()) {
				ret = 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter out = null;
		try {
			out = new FileWriter(file, true);
			out.append(buffer + "\n");
			out.flush();
		} finally {
			out.close();
		}
		
		return ret;
	}
	
}
