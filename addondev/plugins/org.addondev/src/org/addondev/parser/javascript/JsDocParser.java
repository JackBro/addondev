package org.addondev.parser.javascript;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsDocParser {
	private static Pattern fJsDocTypePattern = Pattern.compile("@type\\s+(\\w+)");
	private static Pattern fJsDocReturnPattern = Pattern.compile("@returns\\s+\\{\\s*(.*)\\s*\\}");
	
	public static void parse(String jsdoc)
	{
//		String key = null;
//		if(jsdoc != null && fType == null)
//		{
//			
//			Matcher m = fJsDocTypePattern.matcher(jsdoc);
//			 if (m.find()) {
//				 key = m.group(1);
//			 }
//			 else
//			 {
//				 m = fJsDocReturnPattern.matcher(jsdoc);
//				 if (m.find()) {
//					 key = m.group(1);
//				 }
//			 }
//			 fType = key;
//		}
	}
}
