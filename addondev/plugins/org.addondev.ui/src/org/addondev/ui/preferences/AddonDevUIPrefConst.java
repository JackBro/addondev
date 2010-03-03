package org.addondev.ui.preferences;

import org.eclipse.swt.graphics.RGB;

public class AddonDevUIPrefConst {
	public static final String IMPORT_PATH = "org.addondev.import.path";
	public static final String LOCALE = "org.addondev.ui.locale";
	
	public static final String XULRUNNER_PATH = "org.addondev.pref.xulrunner.path";
	public static final String XULPREVIEW_W = "org.addondev.pref.xulpreview.width";
	public static final String XULPREVIEW_H = "org.addondev.pref.xulpreview.height";
	
	public static final String XULPREVIEW_REFRESH_AUTO = "org.addondev.pref.xulpreview.refresh.auto";
	public static final String XULPREVIEW_REFRESH_XUL = "org.addondev.pref.xulpreview.refresh.xul";
	public static final String XULPREVIEW_REFRESH_DTD = "org.addondev.pref.xulpreview.refresh.dtd";
	public static final String XULPREVIEW_REFRESH_CSS = "org.addondev.pref.xulpreview.refresh.css";
	
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
	
	public static final String COLOR_XML_FOREGROUND = "org.addondev.pref.color.xml.foreground"; 
	public static final String COLOR_XML_TAG = "org.addondev.pref.color.xml.tag"; 
	public static final String COLOR_XML_COMMENT = "org.addondev.pref.color.xml.comment"; 
	public static final String COLOR_XML_KEYWORD = "org.addondev.pref.color.xml.keyword"; 
	public static final String COLOR_XML_STRING = "org.addondev.pref.color.xml.string";	
	
	public static final String BOLD_SUFFIX = ".bold";
	public static final String ITALIC_SUFFIX = ".italic";
	
	
	public static final RGB DEFAULT_COLOR_JAVASCRIPT_COMMENT = new RGB(0, 128, 0);
	public static final RGB DEFAULT_COLOR_JAVASCRIPT_KEYWORD = new RGB(150, 0, 50);
	public static final RGB DEFAULT_COLOR_JAVASCRIPT_STRING = new RGB(0, 0, 170);
	
	public static final RGB DEFAULT_COLOR_XML_TAG = new RGB(0, 0, 128);
	public static final RGB DEFAULT_COLOR_XML_COMMENT = new RGB(204, 204, 204);
	public static final RGB DEFAULT_COLOR_XML_KEYWORD = new RGB(150, 0, 50);
	public static final RGB DEFAULT_COLOR_XML_STRING = new RGB(0, 170, 0);
	
//	RGB XML_COMMENT = new RGB(255, 0, 0);
//	RGB PROC_INSTR = new RGB(128, 128, 128);
//	RGB STRING = new RGB(0, 128, 0);
//	RGB DEFAULT = new RGB(0, 0, 0);
//	RGB TAG = new RGB(0, 0, 128);
}
