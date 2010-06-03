package org.addondev.unittest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class XULParserTest {
	
	private static Pattern doctypePattern = Pattern.compile("<!DOCTYPE\\s+([^\\s]+)\\s+SYSTEM\\s+\"(([^\"]+))\"\\s*>", Pattern.MULTILINE);
	private static Pattern doctypelistPattern = Pattern.compile("<!DOCTYPE\\s+([^\\s]+)\\s*\\[(.+)\\]\\s*>", Pattern.MULTILINE | Pattern.DOTALL);
	
	private static Pattern entityPattern = Pattern.compile("<!ENTITY\\s+%\\s+([^\\s]+)\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>\\s+%(.+);", Pattern.MULTILINE);
	//private static Pattern stylesheetPattern = Pattern.compile("<\\?xml-stylesheet\\s+href=\\s*\"([^\"]+)\"\\s*.*\\?>", Pattern.MULTILINE);
	private static Pattern stylesheetPattern = Pattern.compile("<\\?xml-stylesheet\\s+((.*))\\?>");
	private static Pattern attrPattern = Pattern.compile("([^\\s]+)\\s*=\\s*\"(([^\"]+))\"");
	//private static Pattern stylesheetPattern = Pattern.compile("<\\?xml-stylesheet\\s+(\\w+\\s*=\\s*\".+\")+\\?>");
	
	private static Pattern docPattern = Pattern.compile("<\\?([^\\s]+)\\s+((.*))\\?>");
	
	
	@Test
	public void test1() {
		String text = getSource(XULParserTest.class.getResourceAsStream("test.xul"));
		
		ArrayList<Pattern> patterns = new ArrayList<Pattern>();
		//patterns.add(doctypePattern);
		patterns.add(doctypelistPattern);
		//patterns.add(entityPattern);
		//patterns.add(docPattern);
		
		for (Pattern pattern : patterns) {
			Matcher m = pattern.matcher(text);
			while(m.find()){
				if(m.pattern().equals(doctypelistPattern)){
					System.out.println("diclist pattern = " + doctypelistPattern.pattern());
					System.out.println("diclist entity text = " + m.group(2));
					Matcher m2 = entityPattern.matcher(m.group(2));
					if(m2.find()){
						System.out.println("diclist entity pattern = " + entityPattern.pattern());
						for (int i = 0; i <m2.groupCount(); i++) {
							System.out.println(m2.group(i));
						}
					}
				}else{
					System.out.println("pattern = " + pattern.pattern());
					for (int i = 0; i <m.groupCount(); i++) {
						System.out.println(i + " = " + m.group(i));
					}
					String attr =  m.group(2);
					System.out.println("pattern = " + attrPattern.pattern());
					//attr.split("\\s");
					Matcher attrm = attrPattern.matcher(attr);
					while(attrm.find()){
						for (int j = 1; j <attrm.groupCount(); j++) {
							System.out.println(j + " = " + attrm.group(j));
						}					
					}
				}
			}
		}
	}
	
	private static String getSource(InputStream in) {
		StringBuffer buf = new StringBuffer();
		BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(in));

		String line;
		//String res = "";
		try {
			while ((line = reader.readLine()) != null) {
				//doSomething(line);
				buf.append(line+ "\n");
				//res += line + "\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return res;
		return buf.toString();
	}
}
