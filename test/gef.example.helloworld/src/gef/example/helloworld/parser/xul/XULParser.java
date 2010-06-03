package gef.example.helloworld.parser.xul;

import java.util.regex.Pattern;

public class XULParser {
	private static Pattern doctypePattern = Pattern.compile("<!DOCTYPE\\s+([^\\s]+)\\s+SYSTEM\\s+\"(([^\"]+))\"\\s*>", Pattern.MULTILINE);
	private static Pattern doctypelistPattern = Pattern.compile("<!DOCTYPE\\s+([^\\s]+)\\s*\\[(.+)\\]\\s*>", Pattern.MULTILINE | Pattern.DOTALL);
	private static Pattern entityPattern = Pattern.compile("<!ENTITY\\s+%\\s+([^\\s]+)\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>\\s+%(.+);", Pattern.MULTILINE);
	
	private static Pattern attrPattern = Pattern.compile("([^\\s]+)\\s*=\\s*\"(([^\"]+))\"", Pattern.MULTILINE);
	private static Pattern declarePattern = Pattern.compile("<\\?([^\\s]+)\\s+((.*))\\?>", Pattern.MULTILINE);

	public void parse(String xul){
		
	}
}
