package gef.example.helloworld.parser.declare;

import java.util.regex.Pattern;

public class XULDeclareParser {
	private static Pattern doctypePattern = Pattern.compile("<!DOCTYPE\\s+([^\\s])\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>", Pattern.MULTILINE);
	private static Pattern doctypelistPattern = Pattern.compile("<!DOCTYPE\\s+([^\\s])\\s+\\[(.+)\\]\\s*>", Pattern.MULTILINE);
	private static Pattern entityPattern = Pattern.compile("<!ENTITY\\s+%\\s+([^\\s])\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>\\s+%(.+);", Pattern.MULTILINE);
	private static Pattern stylesheetPattern = Pattern.compile("<\\?xml-stylesheet\\s+href=\\s*\"([^\"]+)\"\\s*.*\\?>", Pattern.MULTILINE);

	public void parse(String xul){
		
	}
}
