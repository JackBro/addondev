package org.addondev.debug.core.model;

public class AddonDevError {

	public String filefullpath;
	public int line;
	public String message;
	
	public AddonDevError(String filefullpath, int line, String message) {
		this.filefullpath = filefullpath;
		this.line = line;
		this.message = message;
	}
}
