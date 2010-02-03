package org.addondev.debug.preferences;

public class AddonDevDebugPrefConst {
	
	public static final String DEBUG_PORT_AUTO = "org.addondev.debug.pref.port.auto";
	public static final String DEBUG_PORT_AUTO_ECLIPSE_START = "org.addondev.debug.pref.port.auto.eclipse.start";
	public static final String DEBUG_PORT_AUTO_ECLIPSE_END = "org.addondev.debug.pref.port.auto.eclipse.end";
	public static final String DEBUG_PORT_AUTO_DEBUGGER_START = "org.addondev.debug.pref.port.auto.debugger.start";
	public static final String DEBUG_PORT_AUTO_DEBUGGER_END = "org.addondev.debug.pref.port.auto.debugger.end";
	
	public static final String DEBUG_PORT_MANUAL_ECLIPSE ="org.addondev.debug.pref.port.manual.eclipse.port";
	public static final String DEBUG_PORT_MANUAL_DEBUGGER = "org.addondev.debug.pref.port.manual.debugger.port";
	//public static final String DEBUGGER_CONEECT_TIMEOUT = "org.addondev.pref.debugger.timeout";
	
	public static final boolean DEFAULT_DEBUG_PORT_AUTO = true;
	public static final int DEFAULT_DEBUG_PORT_AUTO_ECLIPSE_START = 49200;
	public static final int DEFAULT_DEBUG_PORT_AUTO_ECLIPSE_END = 49300;
	public static final int DEFAULT_DEBUG_PORT_AUTO_DEBUGGER_START = 49500;
	public static final int DEFAULT_DEBUG_PORT_AUTO_DEBUGGER_END = 49700;
	public static final int DEFAULT_DEBUG_PORT_MANUAL_ECLIPSE = 49200;
	public static final int DEFAULT_DEBUG_PORT_MANUAL_DEBUGGER = 49500;
	public static final int DEFAULT_DEBUGGER_CONEECT_TIMEOUT = 20000;
}
