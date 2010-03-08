package org.addondev.tools.jsjava;

import java.util.ArrayList;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class JsReadTest {
	public static void main(String[] args) {
		Serializer serializer = new Persister();
		try {
			JsData data = serializer.read(JsData.class, "tmp.data.xml");
			ArrayList<JsClass> cls = data.getClasses();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
