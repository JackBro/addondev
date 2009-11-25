package org.addondev.debug.core.model;

public class JSError {

	public String filefullpath;
	public int line;
	public String message;
	
	public JSError(String filefullpath, int line, String message) {
		this.filefullpath = filefullpath;
		this.line = line;
		this.message = message;
	}
}
