package org.addondev.tools.jsjava;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class JsData {
	
	@ElementList
	private List<JsElement> elements;
	@Element
	private String name;
	
	public List<JsElement> getElements() {
		return elements;
	}
	public void setElements(List<JsElement> elements) {
		this.elements = elements;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JsData() {
		super();
		// TODO Auto-generated constructor stub
	}
}
