package jp.addondev.preferences;

import jp.addondev.plugin.AddonDevPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class AddonDevPreferenceInitializer extends
		AbstractPreferenceInitializer {
	public AddonDevPreferenceInitializer() {
		// TODO Auto-generated constructor stub
		IPreferenceStore store = AddonDevPlugin.getDefault().getPreferenceStore();
		
		store.setDefault(PrefConst.COLOR_JAVASCRIPT_BACKGROUND, 
				AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND);
		
		store.setDefault(PrefConst.COLOR_JAVASCRIPT_FOREGROUND, 
				AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND);
		
		store.setDefault(PrefConst.COLOR_JAVASCRIPT_KEYWORD, 
				StringConverter.asString(PrefConst.DEFAULT_COLOR_JAVASCRIPT_KEYWORD));
		
		store.setDefault(PrefConst.COLOR_JAVASCRIPT_COMMENT, 
				StringConverter.asString(PrefConst.DEFAULT_COLOR_JAVASCRIPT_COMMENT));
		
		store.setDefault(PrefConst.COLOR_JAVASCRIPT_STRING, 
				StringConverter.asString(PrefConst.DEFAULT_COLOR_JAVASCRIPT_STRING));
		
		store.setDefault(PrefConst.COLOR_JAVASCRIPT_STRING, 
				StringConverter.asString(PrefConst.DEFAULT_COLOR_JAVASCRIPT_STRING));
		
		store.setDefault(PrefConst.DEBUGGER_PORT, PrefConst.DEFAULT_DEBUGGER_PORT);
		store.setDefault(PrefConst.ECLIPSE_PORT, PrefConst.DEFAULT_ECLIPSE_PORT);
		store.setDefault(PrefConst.DEBUGGER_CONEECT_TIMEOUT, PrefConst.DEFAULT_DEBUGGER_CONEECT_TIMEOUT);
		
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
