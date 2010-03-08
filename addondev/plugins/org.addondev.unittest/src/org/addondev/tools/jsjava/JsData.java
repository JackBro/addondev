package org.addondev.tools.jsjava;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;

public class JsData {
	
	@ElementList
	private ArrayList<JsClass> classes;

	public ArrayList<JsClass> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<JsClass> classes) {
		this.classes = classes;
	}

	public JsData() {
		super();
		// TODO Auto-generated constructor stub
	}
}
