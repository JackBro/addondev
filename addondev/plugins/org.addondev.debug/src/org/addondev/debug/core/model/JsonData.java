package org.addondev.debug.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonData {
	private String cmd;
	//private String name;
	private List<Map<String, String>> propertylist;
	
	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}

	public List<Map<String, String>> getPropertylist() {
		return propertylist;
	}

	public void setPropertylist(
			List<Map<String, String>> propertylist) {
		this.propertylist = propertylist;
	}

	public JsonData(){}
	
	public void setProperty(Map<String, String> map)
	{		
		if(propertylist == null)
		{
			propertylist = new ArrayList<Map<String,String>>();
		}
		
		propertylist.add(map);
	}
}
