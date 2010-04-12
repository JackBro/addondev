package org.addondev.tools.jsjava;

import java.util.ArrayList;

import org.addondev.parser.javascript.Node;
import org.addondev.parser.javascript.serialize.NodeSerializer;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class JsReadTest {
	public static void main(String[] args) {
		//String filename = "D:\\data\\src\\PDE\\workrepository\\plugins\\addondev\\plugins\\org.addondev.unittest\\tmp\\text.xml";
		String filename = args[0];
		Node root = NodeSerializer.read(filename);
		root.dump("");
		System.exit(0);
		
//		Serializer serializer = new Persister();
//		try {
//			JsData data = serializer.read(JsData.class, filename);
//			ArrayList<JsClass> cls = data.getClasses();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
