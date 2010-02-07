package org.addondev.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	private static Pattern rnp = Pattern.compile("\r\n");
	private static Pattern rp = Pattern.compile("\r");
	private static Pattern np = Pattern.compile("\n");
	
	public static String getNewlineType(String text) {
		Matcher m = rnp.matcher(text);
		if (m.find()) {
			return "\r\n";
		}

		m = rp.matcher(text);
		if (m.find()) {
			return "\r";
		}

		m = np.matcher(text);
		if (m.find()) {
			return "\n";
		}
		
		return "";
	}
	
	public static String[] getLines(String text, Boolean includLF)
	{
		String nt = getNewlineType(text);
		String[] lines = text.split(nt);
		if(includLF)
		{
			ArrayList<String> LFlines = new ArrayList<String>();
			for (String string : lines) {
				LFlines.add(string + nt);
			}
			return LFlines.toArray(new String[LFlines.size()]);
		}
		return lines;
	}
}
