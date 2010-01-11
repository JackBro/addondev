package org.addondev.unittest;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class javascriptparserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JavascriptParser p = new JavascriptParser();
		
		URL url = javascriptparserTest.class.getResource("test.js");
		File file = FileUtils.toFile(url);
		String src = FileUtils.readFileToString(file);

			p.test(src);
	}

}
