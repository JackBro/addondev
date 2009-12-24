package org.addondev.parser.dtd;

import java.util.HashMap;

public class DTDParser {
	private HashMap<String, HashMap<String, String>> fLocateEntityMap;
	private String fCurrntLocate;
	
	public DTDParser()
	{
		fLocateEntityMap = new HashMap<String, HashMap<String,String>>();
	}
	
	public void parse()
	{
		
	}
	
	public void setLocate(String Locate)
	{
		fCurrntLocate = Locate;
	}
	
	public String getLocate()
	{
		return fCurrntLocate;
	}
	
	public boolean hasLocate(String Locate)
	{
		return fLocateEntityMap.containsKey(Locate);
	}
	
	public String getEntity(String key)
	{
		
	}
}
