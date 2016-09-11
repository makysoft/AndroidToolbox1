package com.tresksoft.toolbox.data;

public class CProcess {
	private Integer process_id;
	private String pkgName;
	private String processName64;
	private String process_selected;
	
	public CProcess() {
		
	}
	
	public CProcess(Integer process_id, String pkgName, String process_selected) {
		this.process_id = process_id;
		this.pkgName = pkgName;
		this.process_selected = process_selected;
	}
	
	public void setProcessID(Integer id){
		this.process_id = id;
	}
	
	public Integer getProcessID(){
		return this.process_id;
	}
	
	public void setPkgName(String pkgName){
		this.pkgName = pkgName;
	}
	
	public String getPkgName(){
		return this.pkgName;
	}
	
	public void setProcessName64(String processName64) {
		this.processName64 = processName64;
	}
	
	public String getProcessName64() {
		return this.processName64;
	}
	
	public void setProcessSelected(String process_selected){
		this.process_selected = process_selected;
	}
	
	public String getProcessSelected(){
		return this.process_selected;
	}
}
