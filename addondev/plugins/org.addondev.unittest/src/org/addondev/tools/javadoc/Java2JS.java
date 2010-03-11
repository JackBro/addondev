package org.addondev.tools.javadoc;

public class Java2JS {
	private String type;
	private String jspropname;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getJsname() {
		return jspropname;
	}
	public void setJsname(String jsname) {
		this.jspropname = jsname;
	}
	public Java2JS(String type, String jsname) {
		super();
		this.type = type;
		this.jspropname = jsname;
	}
}
