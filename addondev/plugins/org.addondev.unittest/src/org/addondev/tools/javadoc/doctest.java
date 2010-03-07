package org.addondev.tools.javadoc;

import java.io.File;
import java.util.HashMap;

import com.sun.javadoc.RootDoc;

public class doctest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, String> map = new HashMap<String, String>();
		EasyDoclet doclet = new EasyDoclet(new File("D:/data/src/PDE/xpcom"), new File("D:/data/src/PDE/xpcom/nsIFile.java"));
		RootDoc doc = doclet.getRootDoc();
		MyDoclet md = new MyDoclet("org.mozilla.interfaces.");
		md.setMap(map);
		md.list(doc);
		
		System.exit(0);
	}

}
