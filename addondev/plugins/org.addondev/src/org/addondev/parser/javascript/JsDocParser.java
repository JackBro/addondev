package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsDocParser {
	private static Pattern fTypePattern = Pattern.compile("@type\\s+(\\w+)\n");
	
	//@param {paramType} paramName paramDescription 
	private static Pattern fParamPattern = Pattern.compile("@param\\s+\\{(.*)\\}\\s+(.*)\\s+(.*)\n");

	//@type  String[] 
	
	//private boolean isParsed=false;
	private String fType = null;
	private Map<String, List<String>> fParam;
	
	public void parse(String jsdoc)
	{	
		if(jsdoc == null) return;
		//isParsed = true;
		Matcher m = fTypePattern.matcher(jsdoc);
		if (m.find()) {
			fType = m.group(1);
		}
		
		fParam = new HashMap<String, List<String>>();
		m = fParamPattern.matcher(jsdoc);
		while (m.find()) {
			String name = m.group(2);
			String type = m.group(1);
			String description = m.group(3);
			ArrayList<String> list = new ArrayList<String>();
			list.add(type);
			list.add(description);
			fParam.put(name, list);
		}
	}
	
	public String getType()
	{
		return fType;
	}
	
	public String getParamType(String paramname)
	{
		if(fParam == null) return null;
		if(!fParam.containsKey(paramname)) return null;
		
		return fParam.get(paramname).get(0);		
	}
}
