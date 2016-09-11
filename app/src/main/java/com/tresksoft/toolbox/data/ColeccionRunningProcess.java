package com.tresksoft.toolbox.data;

import java.util.ArrayList;

public class ColeccionRunningProcess {

	public ArrayList<CRunningProcess> coleccionRunningProcess;
	public Integer runningProcessToKill;
	
	public ColeccionRunningProcess() {
		coleccionRunningProcess = new ArrayList<CRunningProcess>();
	}
	
	public ColeccionRunningProcess(ArrayList<CRunningProcess> coleccionRunningProcess) {
		this();
		this.coleccionRunningProcess = coleccionRunningProcess; 
	}
	
	public void add(CRunningProcess runningProcess) {
		coleccionRunningProcess.add(runningProcess);
	}
	
	public void setRunningProcessToKill(Integer runningProcessToKill) {
		this.runningProcessToKill = runningProcessToKill;
	}
	
	public Integer getRunningProcessToKill() {
		return this.runningProcessToKill;
	}
	
	public void setColeccionRunningProcess(ArrayList<CRunningProcess> coleccionRunningProcess) {
		this.coleccionRunningProcess = coleccionRunningProcess;
	}
	
	public ArrayList<CRunningProcess> getColeccionRunningProcess() {
		return this.coleccionRunningProcess;
	}
	
}
