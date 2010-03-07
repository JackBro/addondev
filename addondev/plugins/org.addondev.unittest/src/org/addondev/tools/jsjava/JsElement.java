package org.addondev.tools.jsjava;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class JsElement {
	
	@Element(data=true)
	private String jsDoc;
	@Element
	private String name;
	@Element
	private String type;
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JsElement() {
		super();
		// TODO Auto-generated constructor stub
	}
}
