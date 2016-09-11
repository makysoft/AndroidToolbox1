package com.tresksoft.toolbox.data;

import android.graphics.drawable.Drawable;

public class CRunningProcess extends CProcess{
	private Integer iUID;
	private Integer iPID;
	private String sProcessName;
	private String sPackageName;
	private boolean bMatarProceso;
	private Drawable oIcon;
	private Integer iImportance;
	private Integer iMemoryProcess;
	
	public CRunningProcess() {
		super();
	}
	
	public void setUID(Integer iUID){
		this.iUID = iUID;
	}
	
	public Integer getUID() {
		return this.iUID;
	}
	
	public void setPID(Integer iPID){
		this.iPID = iPID;
	}
	
	public Integer getPID(){
		return this.iPID;
	}
	
	public void setImportance(Integer iImportance){
		this.iImportance = iImportance;
	}
	
	public Integer getImportance(){
		return this.iImportance;
	}
	
	public String getProcessName() {
		return this.sProcessName;
	}
	
	public void setProcessName(String processName) {
		this.sProcessName = processName;
	}
	
	public void setPackageName(String sPackageName){
		this.sPackageName = sPackageName;
	}
	
	public String getPackageName(){
		return this.sPackageName;
	}
	
	public void setIcon(Drawable oIcon){
		this.oIcon = oIcon;
	}
	
	public Drawable getIcon(){
		return this.oIcon;
	}
	
	public void setMatarProceso(boolean bMatarProceso){
		this.bMatarProceso = bMatarProceso;
	}
	
	public boolean getMatarProceso() {
		return this.bMatarProceso;
	}
	
	public void setProcessMemory(int lenght) {
		this.iMemoryProcess = lenght;
	}
	
	public int getProcessMemory() {
		return this.iMemoryProcess;
	}
	
}
