package org.addondev.tools.jsjava;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class JsElement {
	
	@Element(data=true)
	private String jsDoc;
	@Attribute
	private String name;
	@Attribute
	private String nodetype;
	@Attribute
	private String returntype;	
	
	public String getReturntype() {
		return returntype;
	}

	public void setReturntype(String returntype) {
		this.returntype = returntype;
	}

	@ElementList
	private ArrayList<String> params;
	
	public String getJsDoc() {
		return jsDoc;
	}

	public void setJsDoc(String jsDoc) {
		this.jsDoc = jsDoc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNodeType() {
		return nodetype;
	}

	public void setNodeType(String nodetype) {
		this.nodetype = nodetype;
	}

	public ArrayList<String> getParams() {
		return params;
	}

	public void setParams(ArrayList<String> params) {
		this.params = params;
	}

	public JsElement() {
		// TODO Auto-generated constructor stub
		super();
		params = new ArrayList<String>();
	}
	
	public JsElement(String name, String nodetype, String jsdoc, ArrayList<String> params, String returntype) {
		super();
		this.name = name;
		this.jsDoc = jsdoc;
		this.nodetype = nodetype;
		this.params = params;
		this.returntype = returntype;
		// TODO Auto-generated constructor stub
	}
	
	public JsElement(String name, String nodetype, String jsdoc, ArrayList<String> params) {
		super();
		this.name = name;
		this.jsDoc = jsdoc;
		this.nodetype = nodetype;
		this.params = params;
		this.returntype = "";
		// TODO Auto-generated constructor stub
	}
	
	public JsElement(String name, String nodetype, String jsdoc, String returntype) {
		super();
		this.name = name;
		this.jsDoc = jsdoc;
		this.nodetype = nodetype;
		params = new ArrayList<String>();
		this.returntype = returntype;
		// TODO Auto-generated constructor stub
	}
	
	public JsElement(String name, String nodetype, String jsdoc) {
		super();
		this.name = name;
		this.jsDoc = jsdoc;
		this.nodetype = nodetype;
		params = new ArrayList<String>();
		this.returntype = "";
		// TODO Auto-generated constructor stub
	}
}
