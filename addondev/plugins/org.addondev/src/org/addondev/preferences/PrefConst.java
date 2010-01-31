package org.addondev.preferences;

import org.eclipse.swt.graphics.RGB;

public class PrefConst {
	public static final String IMPORT_PATH = "org.addondev.import.path";
	
	public static final String XULRUNNER_PATH = "org.addondev.pref.xulrunner.path";
	public static final String XULPREVIEW_W = "org.addondev.pref.xulpreview.width";
	public static final String XULPREVIEW_H = "org.addondev.pref.xulpreview.height";
	
	public static final String DEBUG_PORT_AUTO = "org.addondev.pref.debug.port.auto";
	public static final String DEBUG_PORT_AUTO_ECLIPSE_START = "org.addondev.pref.debug.port.auto.eclipse.start";
	public static final String DEBUG_PORT_AUTO_ECLIPSE_END = "org.addondev.pref.debug.port.auto.eclipse.end";
	public static final String DEBUG_PORT_AUTO_DEBUGGER_START = "org.addondev.pref.debug.port.auto.debugger.start";
	public static final String DEBUG_PORT_AUTO_DEBUGGER_END = "org.addondev.pref.debug.port.auto.debugger.end";
	
	public static final String DEBUG_PORT_MANUAL_ECLIPSE ="org.addondev.pref.debug.port.manual.eclipse.port";
	public static final String DEBUG_PORT_MANUAL_DEBUGGER = "org.addondev.pref.debug.port.manual.debugger.port";
	public static final String DEBUGGER_CONEECT_TIMEOUT = "org.addondev.pref.debugger.timeout";
	
	public static final boolean DEFAULT_DEBUG_PORT_AUTO = true;
	//public static final boolean DEFAULT_DEBUG_PORT_MANUAL = false;
	public static final int DEFAULT_DEBUG_PORT_AUTO_ECLIPSE_START = 49200;
	public static final int DEFAULT_DEBUG_PORT_AUTO_ECLIPSE_END = 49300;
	public static final int DEFAULT_DEBUG_PORT_AUTO_DEBUGGER_START = 49500;
	public static final int DEFAULT_DEBUG_PORT_AUTO_DEBUGGER_END = 49700;
	public static final int DEFAULT_DEBUG_PORT_MANUAL_ECLIPSE = 49200;
	public static final int DEFAULT_DEBUG_PORT_MANUAL_DEBUGGER = 49500;
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
