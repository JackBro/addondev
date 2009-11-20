package jp.addondev.preferences;

import org.eclipse.swt.graphics.RGB;

public class PrefConst {
	public static final String IMPORT_PATH = "jp.addondev.import.path";
	
	public static final String DEBUGGER_PORT = "jp.addondev.pref.debugger.port";
	public static final String ECLIPSE_PORT = "jp.addondev.pref.eclipse.port";
	public static final String DEBUGGER_CONEECT_TIMEOUT = "jp.addondev.pref.debugger.timeout";
	
	public static final String DEFAULT_DEBUGGER_PORT = "8083";
	public static final String DEFAULT_ECLIPSE_PORT = "8084";
	public static final int DEFAULT_DEBUGGER_CONEECT_TIMEOUT = 20000;
	
	public static final String FIREFOX_PATH = "jp.addondev.pref.firefox.path";
	public static final String FIREFOX_PROFILE_NANE = "jp.addondev.pref.firefox.profile.name";
	public static final String FIREFOX_PROFILE_PATH = "jp.addondev.pref.firefox.profile.path";
	public static final String FIREFOX_ARGS = "jp.addondev.pref.firefox.args";
	public static final String FIREFOX_DEBUGTARGETADDONS = "jp.addondev.pref.firefox.debugtargetaddons";
	
	public static final String FIREFOX_ADDON_GUID = "jp.addondev.pref.firefox.addon.guid";
	public static final String FIREFOX_ADDON_VERSION = "jp.addondev.pref.firefox.addon.version";
	public static final String FIREFOX_ADDON_MINVERSION = "jp.addondev.pref.firefox.addon.minversion";
	public static final String FIREFOX_ADDON_MAXVERSION = "jp.addondev.pref.firefox.addon.maxversion";
	
	public static final String DEFAULT_FIREFOX_ADDON_GUID = "{ec8030f7-c20a-464f-9b0e-13a3a9e97384}";
	public static final String DEFAULT_FIREFOX_ADDON_VERSION = "0.1.0";
	public static final String DEFAULT_FIREFOX_ADDON_MINVERSION = "3.0";
	public static final String DEFAULT_FIREFOX_ADDON_MAXVERSION = "3.5.*";
	
	public static final String COLOR_JAVASCRIPT_BACKGROUND = "jp.addondev.pref.color.javascript.background"; 
	public static final String COLOR_JAVASCRIPT_FOREGROUND = "jp.addondev.pref.color.javascript.foreground"; 
	public static final String COLOR_JAVASCRIPT_COMMENT = "jp.addondev.pref.color.javascript.comment"; 
	public static final String COLOR_JAVASCRIPT_KEYWORD = "jp.addondev.pref.color.javascript.keyword"; 
	public static final String COLOR_JAVASCRIPT_STRING = "jp.addondev.pref.color.javascript.string"; 
	
	public static final RGB DEFAULT_COLOR_JAVASCRIPT_COMMENT = new RGB(0, 128, 0);
	public static final RGB DEFAULT_COLOR_JAVASCRIPT_KEYWORD = new RGB(150, 0, 50);
	public static final RGB DEFAULT_COLOR_JAVASCRIPT_STRING = new RGB(0, 0, 170);
}
