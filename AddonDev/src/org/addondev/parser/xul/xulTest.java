package org.addondev.parser.xul;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class xulTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void ptest()
	{

		Pattern content = Pattern.compile("<!DOCTYPE\\s+overlay\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>");
		String data = "<!DOCTYPE overlay SYSTEM \"chrome://dendzones/locale/dendzones.dtd\">";
		Matcher matcher5 = content.matcher(data);
		if (matcher5.find()) {
			System.out.println(matcher5.group(0));
			System.out.println(matcher5.group(1));
			String hh = matcher5.group(0).replaceAll(matcher5.group(1), "replacement");
			System.out.println(hh);
		}
		
		//<?xml-stylesheet href="chrome://dendzones/skin/dendzones.css" type="text/css"?>
		Pattern content2 = Pattern.compile("<!DOCTYPE\\s+overlay\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>");
		String data2 = "<?xml-stylesheet href=\"chrome://dendzones/skin/dendzones.css\" type=\"text/css\"?>";
		Matcher matcher2 = content2.matcher(data2);
		if (matcher2.find()) {
			System.out.println(matcher2.group(0));
			System.out.println(matcher2.group(1));
			String hh = matcher2.group(0).replaceAll(matcher2.group(1), "replacement");
			System.out.println(hh);
		}
	}

}
