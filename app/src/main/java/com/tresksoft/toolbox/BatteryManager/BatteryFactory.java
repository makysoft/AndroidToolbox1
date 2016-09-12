package com.tresksoft.toolbox.BatteryManager;

import com.tresksoft.graphics.Grid;
import com.tresksoft.toolbox.data.Constants;

public class BatteryFactory {

	protected String type;
	
	public BatteryFactory (String type) {
		this.type = type;
	}
	
	public Grid createGrid() {
		if (type.equals("gridLevel")) {
			return new BatteryGridLevel();
		} else if (type.equals("gridLevelCustom")) {
			return new BatteryGridLevel();
		} else if (type.equals("gridLevelCustomWH")) {
			return new BatteryGridLevel(Constants.bigGridPosX, 
											 Constants.bigGridPosY, 
											 Constants.bigGridWidth, 
											 Constants.bigGridHeight);
		} else {
			return new BatteryGridGeneric();
		}
	}
}
