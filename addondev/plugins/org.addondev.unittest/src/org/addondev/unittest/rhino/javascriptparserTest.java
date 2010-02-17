package org.addondev.unittest.rhino;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.addondev.rhino.JavascriptSyntaxCheck;
import org.addondev.util.FileUtil;
import org.junit.Test;

public class javascriptparserTest {

	@Test
	public void JavascriptSyntaxCheckTest() {
		// TODO Auto-generated method stub
		//JavascriptParser p = new JavascriptParser();
		
		InputStream input = javascriptparserTest.class.getResourceAsStream("test.js");
		String src = null;
		try {
			src = FileUtil.getContent(input);
			//p.test(src);
			JavascriptSyntaxCheck js = new JavascriptSyntaxCheck();
			js.check(src);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.exit(0);
	}

}
