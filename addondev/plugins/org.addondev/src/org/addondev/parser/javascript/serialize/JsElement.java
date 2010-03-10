package org.addondev.parser.javascript.serialize;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class JsElement {
	
	@Element(data=true, required=false)
	private String jsDoc;
	@Attribute
	private String name;
	@Attribute
	private String nodetype;
	@Attribute(required=false)
	private String returntype;	
	@ElementList(empty=true, required=false)
	private ArrayList<JsElement> params;
	
	public String getReturntype() {
		return returntype;
	}

	public void setReturntype(String returntype) {
		this.returntype = returntype;
	}

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

	public ArrayList<JsElement> getParams() {
		return params;
	}

	public void setParams(ArrayList<JsElement> params) {
		this.params = params;
	}

	public JsElement() {
		// TODO Auto-generated constructor stub
		super();
		//params = new ArrayList<JsElement>();
	}
	
	public JsElement(String name, String nodetype, String jsdoc, ArrayList<JsElement> params, String returntype) {
		super();
		this.name = name;
		this.jsDoc = jsdoc;
		this.nodetype = nodetype;
		this.params = params;
		this.returntype = returntype;
		// TODO Auto-generated constructor stub
	}
	
	public JsElement(String name, String nodetype, String jsdoc, ArrayList<JsElement> params) {
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
		//params = new ArrayList<JsElement>();
		this.returntype = returntype;
		// TODO Auto-generated constructor stub
	}
	
	public JsElement(String name, String nodetype, String jsdoc) {
		super();
		this.name = name;
		this.jsDoc = jsdoc;
		this.nodetype = nodetype;
		//params = new ArrayList<JsElement>();
		this.returntype = "";
		// TODO Auto-generated constructor stub
	}
}
