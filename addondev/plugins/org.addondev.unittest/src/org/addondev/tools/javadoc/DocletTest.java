package org.addondev.tools.javadoc;

import java.io.File;
import java.util.HashMap;

import org.addondev.parser.javascript.serialize.JsElement;


import com.sun.javadoc.RootDoc;

public class DocletTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, JsElement> map = new HashMap<String, JsElement>();
		String path = args[0];
		File dir = new File(path);
	    File[] files = dir.listFiles();
		for (File file : files) {
			EasyDoclet doclet = new EasyDoclet(dir, file);
			RootDoc doc = doclet.getRootDoc();
			MyDoclet md = new MyDoclet("org.mozilla.interfaces.");
			md.setMap(map);
			md.list(doc);
		}
		System.exit(0);
	}

}
