package org.addondev.parser.javascript.serialize;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class JsClass {
	
	@Attribute
	private String name;
	@ElementList
	private ArrayList<JsElement> elements;
		
	public ArrayList<JsElement> getElements() {
		return elements;
	}
	public void setElements(ArrayList<JsElement> elements) {
		this.elements = elements;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JsClass() {
		super();
		// TODO Auto-generated constructor stub
	}
	public JsClass(String name)
	{
		this.name = name;
		elements = new ArrayList<JsElement>();
	}
	
	public void addElement(JsElement elem)
	{
		elements.add(elem);
	}
}
