package org.addondev.parser.javascript;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class rhinoTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ParsarTest p = new ParsarTest();
		

		
		URL url = rhinoTest.class.getResource("test.js");
		File file = FileUtils.toFile(url);
		String src = FileUtils.readFileToString(file);
		p.test(src);
	}

}
