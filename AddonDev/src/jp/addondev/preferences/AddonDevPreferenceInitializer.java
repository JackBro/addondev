package jp.addondev.preferences;

import jp.addondev.plugin.AddonDevPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class AddonDevPreferenceInitializer extends
		AbstractPreferenceInitializer {
	private Display display;
	public AddonDevPreferenceInitializer() {
		// TODO Auto-generated constructor stub
		//display = new Display();
		IPreferenceStore store = AddonDevPlugin.getDefault().getPreferenceStore();
		store.setDefault(AddonDevPlugin.PREF_COLOR_JAVASCRIPT_WORD, StringConverter.asString(new RGB(0,0,255)));

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
