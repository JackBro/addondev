package org.addondev.tools.jsjava;

import java.io.File;
import java.util.HashMap;

import org.addondev.parser.javascript.serialize.JsData;
import org.addondev.parser.javascript.serialize.JsElement;
import org.addondev.tools.javadoc.EasyDoclet;
import org.addondev.tools.javadoc.MyDoclet;
import org.addondev.tools.javadoc.RefJava;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.sun.javadoc.RootDoc;

public class JsJavaTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//String docpath = "D:/data/src/PDE/xpcom/docsrc";
		//String datapath = "D:/data/src/PDE/xpcom/docsrc";
		String docpath = args[0];
		String datapath = args[0];
		
		HashMap<String, JsElement> map = new HashMap<String, JsElement>();
		String path = docpath;
		File dir = new File(path);
	    File[] files = dir.listFiles();
		for (File file : files) {
			EasyDoclet doclet = new EasyDoclet(dir, file);
			RootDoc doc = doclet.getRootDoc();
			MyDoclet md = new MyDoclet("org.mozilla.interfaces");
			md.setMap(map);
			md.list(doc);
		}
		
		RefJava m = new RefJava();
		JsData data = m.makeXML(datapath, map);
		
		Serializer serializer = new Persister();
		File result = new File("tmp/text.xml");
		System.out.println("      " + result);
		try {
			serializer.write(data, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.exit(0);
	}

}
