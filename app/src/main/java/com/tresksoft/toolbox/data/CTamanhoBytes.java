package com.tresksoft.toolbox.data;

public class CTamanhoBytes {

	public final Long bytes;
	
	public CTamanhoBytes(long bytes) {
		this.bytes = bytes;
	}
	
	public String toString() {
		String retString = null;
		if (this.bytes < 1024){
			retString = bytes + " bytes";
		}else if (this.bytes < 1048576) {
			retString = (bytes/1024) + " KB";
		} else {
			retString = (bytes/1048576) + " MB";
		}
		return retString;
	}
	
}
