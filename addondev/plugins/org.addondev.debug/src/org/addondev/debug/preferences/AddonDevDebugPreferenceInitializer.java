package org.addondev.debug.preferences;

import org.addondev.debug.core.AddonDevDebugPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class AddonDevDebugPreferenceInitializer extends
		AbstractPreferenceInitializer {
	public AddonDevDebugPreferenceInitializer() {
		// TODO Auto-generated constructor stub
		IPreferenceStore store = AddonDevDebugPlugin.getDefault().getPreferenceStore();
		//IPreferenceStore editorstore = EditorsUI.getPreferenceStore();
		
		store.setDefault(AddonDevDebugPrefConst.DEBUG_PORT_AUTO, AddonDevDebugPrefConst.DEFAULT_DEBUG_PORT_AUTO);
		store.setDefault(AddonDevDebugPrefConst.DEBUG_PORT_AUTO_ECLIPSE_START, AddonDevDebugPrefConst.DEFAULT_DEBUG_PORT_AUTO_ECLIPSE_START);
		store.setDefault(AddonDevDebugPrefConst.DEBUG_PORT_AUTO_ECLIPSE_END, AddonDevDebugPrefConst.DEFAULT_DEBUG_PORT_AUTO_ECLIPSE_END);
		store.setDefault(AddonDevDebugPrefConst.DEBUG_PORT_AUTO_DEBUGGER_START, AddonDevDebugPrefConst.DEFAULT_DEBUG_PORT_AUTO_DEBUGGER_START);
		store.setDefault(AddonDevDebugPrefConst.DEBUG_PORT_AUTO_DEBUGGER_END, AddonDevDebugPrefConst.DEFAULT_DEBUG_PORT_AUTO_DEBUGGER_END);
		store.setDefault(AddonDevDebugPrefConst.DEBUG_PORT_MANUAL_ECLIPSE, AddonDevDebugPrefConst.DEFAULT_DEBUG_PORT_MANUAL_ECLIPSE);
		store.setDefault(AddonDevDebugPrefConst.DEBUG_PORT_MANUAL_DEBUGGER, AddonDevDebugPrefConst.DEFAULT_DEBUG_PORT_MANUAL_DEBUGGER);
		//store.setDefault(AddonDevDebugPrefConst.DEBUGGER_CONEECT_TIMEOUT, AddonDevDebugPrefConst.DEFAULT_DEBUGGER_CONEECT_TIMEOUT);
	}

	@Override
	public void initializeDefaultPreferences() {
		// TODO Auto-generated method stub
		
	
	}

}
