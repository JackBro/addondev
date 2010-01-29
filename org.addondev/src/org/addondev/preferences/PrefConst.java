package org.addondev.preferences;

import org.eclipse.swt.graphics.RGB;

public class PrefConst {
	public static final String IMPORT_PATH = "org.addondev.import.path";
	
	public static final String DEBUGGER_PORT = "org.addondev.pref.debugger.port";
	public static final String ECLIPSE_PORT = "org.addondev.pref.eclipse.port";
	public static final String DEBUGGER_CONEECT_TIMEOUT = "org.addondev.pref.debugger.timeout";
	
	public static final String XULRUNNER_PATH = "org.addondev.pref.xulrunner.path";
	public static final String XULPREVIEW_W = "org.addondev.pref.xulpreview.width";
	public static final String XULPREVIEW_H = "org.addondev.pref.xulpreview.height";
	
	//public static final String DEFAULT_DEBUGGER_PORT = "8083";
	//public static final String DEFAULT_ECLIPSE_PORT = "8084";
	public static final int DEFAULT_DEBUGGER_CONEECT_TIMEOUT = 20000;
	
	public static final String FIREFOX_ADDON_GUID = "org.addondev.pref.firefox.addon.guid";
	public static final String FIREFOX_ADDON_VERSION = "org.addondev.pref.firefox.addon.version";
	public static final String FIREFOX_ADDON_MINVERSION = "org.addondev.pref.firefox.addon.minversion";
	public static final String FIREFOX_ADDON_MAXVERSION = "org.addondev.pref.firefox.addon.maxversion";
	
	public static final String DEFAULT_FIREFOX_ADDON_GUID = "{ec8030f7-c20a-464f-9b0e-13a3a9e97384}";
	public static final String DEFAULT_FIREFOX_ADDON_VERSION = "0.1.0";
	public static final String DEFAULT_FIREFOX_ADDON_MINVERSION = "3.0";
	public static final String DEFAULT_FIREFOX_ADDON_MAXVERSION = "3.5.*";
	
	public static final String COLOR_JAVASCRIPT_BACKGROUND = "org.addondev.pref.color.javascript.background"; 
	public static final String COLOR_JAVASCRIPT_FOREGROUND = "org.addondev.pref.color.javascript.foreground"; 
	public static final String COLOR_JAVASCRIPT_COMMENT = "org.addondev.pref.color.javascript.comment"; 
	public static final String COLOR_JAVASCRIPT_KEYWORD = "org.addondev.pref.color.javascript.keyword"; 
	public static final String COLOR_JAVASCRIPT_STRING = "org.addondev.pref.color.javascript.string"; 
	
	public static final RGB DEFAULT_COLOR_JAVASCRIPT_COMMENT = new RGB(0, 128, 0);
	public static final RGB DEFAULT_COLOR_JAVASCRIPT_KEYWORD = new RGB(150, 0, 50);
	public static final RGB DEFAULT_COLOR_JAVASCRIPT_STRING = new RGB(0, 0, 170);
}
