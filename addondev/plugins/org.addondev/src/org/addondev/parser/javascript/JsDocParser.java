package org.addondev.parser.javascript;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsDocParser {
	private static Pattern fTypePattern = Pattern.compile("@type\\s+(\\w+)");
	//private static Pattern fJsDocReturnPattern = Pattern.compile("@returns\\s+\\{\\s*(.*)\\s*\\}");
	
	public String getType(String jsdoc)
	{
		if(jsdoc == null) return null;
		String res = null;
	
		Matcher m = fTypePattern.matcher(jsdoc);
		if (m.find()) {
			res = m.group(1);
		}
		return res;
	}
}
