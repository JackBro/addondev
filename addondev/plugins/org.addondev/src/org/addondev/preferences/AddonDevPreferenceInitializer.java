package org.addondev.preferences;


import org.addondev.plugin.AddonDevPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class AddonDevPreferenceInitializer extends
		AbstractPreferenceInitializer {
	public AddonDevPreferenceInitializer() {
		// TODO Auto-generated constructor stub
		IPreferenceStore store = AddonDevPlugin.getDefault().getPreferenceStore();
		IPreferenceStore editorstore = EditorsUI.getPreferenceStore();
		
		String bgcolor = editorstore.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT) ? 
				editorstore.getDefaultString(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND):editorstore.getString(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND);
		store.setDefault(PrefConst.COLOR_JAVASCRIPT_BACKGROUND, bgcolor);
		
		String fgcolor = editorstore.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT) ? 
				editorstore.getDefaultString(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND):editorstore.getString(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND);
		store.setDefault(PrefConst.COLOR_JAVASCRIPT_FOREGROUND, fgcolor);
		
		store.setDefault(PrefConst.COLOR_JAVASCRIPT_KEYWORD, 
				StringConverter.asString(PrefConst.DEFAULT_COLOR_JAVASCRIPT_KEYWORD));
		
		store.setDefault(PrefConst.COLOR_JAVASCRIPT_COMMENT, 
				StringConverter.asString(PrefConst.DEFAULT_COLOR_JAVASCRIPT_COMMENT));
		
		store.setDefault(PrefConst.COLOR_JAVASCRIPT_STRING, 
				StringConverter.asString(PrefConst.DEFAULT_COLOR_JAVASCRIPT_STRING));
		
		store.setDefault(PrefConst.DEBUG_PORT_AUTO, PrefConst.DEFAULT_DEBUG_PORT_AUTO);
		store.setDefault(PrefConst.DEBUG_PORT_AUTO_ECLIPSE_START, PrefConst.DEFAULT_DEBUG_PORT_AUTO_ECLIPSE_START);
		store.setDefault(PrefConst.DEBUG_PORT_AUTO_ECLIPSE_END, PrefConst.DEFAULT_DEBUG_PORT_AUTO_ECLIPSE_END);
		store.setDefault(PrefConst.DEBUG_PORT_AUTO_DEBUGGER_START, PrefConst.DEFAULT_DEBUG_PORT_AUTO_DEBUGGER_START);
		store.setDefault(PrefConst.DEBUG_PORT_AUTO_DEBUGGER_END, PrefConst.DEFAULT_DEBUG_PORT_AUTO_DEBUGGER_END);
		store.setDefault(PrefConst.DEBUG_PORT_MANUAL_ECLIPSE, PrefConst.DEFAULT_DEBUG_PORT_MANUAL_ECLIPSE);
		store.setDefault(PrefConst.DEBUG_PORT_MANUAL_DEBUGGER, PrefConst.DEFAULT_DEBUG_PORT_MANUAL_DEBUGGER);
		store.setDefault(PrefConst.DEBUGGER_CONEECT_TIMEOUT, PrefConst.DEFAULT_DEBUGGER_CONEECT_TIMEOUT);
		
		
		store.setDefault(PrefConst.FIREFOX_ADDON_GUID, PrefConst.DEFAULT_FIREFOX_ADDON_GUID);
		store.setDefault(PrefConst.FIREFOX_ADDON_VERSION, PrefConst.DEFAULT_FIREFOX_ADDON_VERSION);
		store.setDefault(PrefConst.FIREFOX_ADDON_MINVERSION, PrefConst.DEFAULT_FIREFOX_ADDON_MINVERSION);
		store.setDefault(PrefConst.FIREFOX_ADDON_MAXVERSION, PrefConst.DEFAULT_FIREFOX_ADDON_MAXVERSION);
		
		store.setDefault(PrefConst.XULRUNNER_PATH, "");
		store.setDefault(PrefConst.XULPREVIEW_W, 400);
		store.setDefault(PrefConst.XULPREVIEW_H, 400);		
		
		//		store.setDefault(AddonDevPlugin.PREF_COLOR_JAVASCRIPT_COMMENT, 
//				StringConverter.asString(display.getSystemColor(SWT.COLOR_DARK_GREEN).getRGB()));
//		store.setDefault(AddonDevPlugin.PREF_COLOR_JAVASCRIPT_WORD, 
//				StringConverter.asString(display.getSystemColor(SWT.COLOR_DARK_MAGENTA).getRGB()));
//		
//		RGB rgb = StringConverter.asRGB( store.getString(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND));
//		store.setDefault(AddonDevPlugin.PREF_COLOR_JAVASCRIPT_BACKGROUND, 
//				StringConverter.asString(rgb));	
	}

	@Override
	public void initializeDefaultPreferences() {
		// TODO Auto-generated method stub
		
	
	}

}
