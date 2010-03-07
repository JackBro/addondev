package org.addondev.tools.jsjava;

import java.io.File;
import java.util.ArrayList;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class JsJavaTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<JsElement> elms = new ArrayList<JsElement>();
		
		JsElement elm = new JsElement();
		elm.setName("name1");
		elm.setJsDoc("/* 1 */");
		elm.setType("m1");
		elms.add(elm);
		
		JsElement elm2 = new JsElement();
		elm2.setName("name1");
		elm2.setJsDoc("/* 2 */");
		elm2.setType("m2");
		elms.add(elm2);
		
		JsData textData = new JsData();
		textData.setName("func");
		textData.setElements(elms);
		
		ArrayList<JsData> datas = new ArrayList<JsData>();
		datas.add(textData);
		
		Serializer serializer = new Persister();
		File result = new File("text.xml");
		System.out.println("      " + result);
		try {
			serializer.write(textData, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
