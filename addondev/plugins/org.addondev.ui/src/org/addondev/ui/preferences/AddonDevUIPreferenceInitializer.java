package org.addondev.ui.preferences;


import org.addondev.ui.AddonDevUIPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class AddonDevUIPreferenceInitializer extends AbstractPreferenceInitializer {
	public AddonDevUIPreferenceInitializer() {
		// TODO Auto-generated constructor stub
		IPreferenceStore store = AddonDevUIPlugin.getDefault().getPreferenceStore();
		IPreferenceStore editorstore = EditorsUI.getPreferenceStore();
		
		String bgcolor = editorstore.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT) ? 
				editorstore.getDefaultString(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND):editorstore.getString(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND);
		PreferenceConverter.setDefault(store, AddonDevUIPrefConst.COLOR_JAVASCRIPT_BACKGROUND, 
				StringConverter.asRGB(bgcolor));
		
		String fgcolor = editorstore.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT) ? 
				editorstore.getDefaultString(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND):editorstore.getString(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND);
		PreferenceConverter.setDefault(store, AddonDevUIPrefConst.COLOR_JAVASCRIPT_FOREGROUND, 
				StringConverter.asRGB(fgcolor));
		
		PreferenceConverter.setDefault(store, AddonDevUIPrefConst.COLOR_JAVASCRIPT_KEYWORD, 
				AddonDevUIPrefConst.DEFAULT_COLOR_JAVASCRIPT_KEYWORD);
		store.setDefault(AddonDevUIPrefConst.COLOR_JAVASCRIPT_KEYWORD + AddonDevUIPrefConst.BOLD_SUFFIX
				, true);
		store.setDefault(AddonDevUIPrefConst.COLOR_JAVASCRIPT_KEYWORD + AddonDevUIPrefConst.ITALIC_SUFFIX
				, false);
		
		PreferenceConverter.setDefault(store, AddonDevUIPrefConst.COLOR_JAVASCRIPT_COMMENT, 
				AddonDevUIPrefConst.DEFAULT_COLOR_JAVASCRIPT_COMMENT);
		store.setDefault(AddonDevUIPrefConst.COLOR_JAVASCRIPT_COMMENT + AddonDevUIPrefConst.BOLD_SUFFIX
				, false);
		store.setDefault(AddonDevUIPrefConst.COLOR_JAVASCRIPT_COMMENT + AddonDevUIPrefConst.ITALIC_SUFFIX
				, false);
		
		PreferenceConverter.setDefault(store, AddonDevUIPrefConst.COLOR_JAVASCRIPT_STRING, 
				AddonDevUIPrefConst.DEFAULT_COLOR_JAVASCRIPT_STRING);
		store.setDefault(AddonDevUIPrefConst.COLOR_JAVASCRIPT_STRING + AddonDevUIPrefConst.BOLD_SUFFIX
				, false);
		store.setDefault(AddonDevUIPrefConst.COLOR_JAVASCRIPT_STRING + AddonDevUIPrefConst.ITALIC_SUFFIX
				, false);
		
		//XUL
		PreferenceConverter.setDefault(store, AddonDevUIPrefConst.COLOR_XML_FOREGROUND, 
				StringConverter.asRGB(fgcolor));
		
		PreferenceConverter.setDefault(store, AddonDevUIPrefConst.COLOR_XML_TAG, 
				AddonDevUIPrefConst.DEFAULT_COLOR_XML_TAG);
		store.setDefault(AddonDevUIPrefConst.COLOR_XML_TAG + AddonDevUIPrefConst.BOLD_SUFFIX
				, true);
		store.setDefault(AddonDevUIPrefConst.COLOR_XML_TAG + AddonDevUIPrefConst.ITALIC_SUFFIX
				, false);		
		
		PreferenceConverter.setDefault(store, AddonDevUIPrefConst.COLOR_XML_KEYWORD, 
				AddonDevUIPrefConst.DEFAULT_COLOR_XML_KEYWORD);
		store.setDefault(AddonDevUIPrefConst.COLOR_XML_KEYWORD + AddonDevUIPrefConst.BOLD_SUFFIX
				, true);
		store.setDefault(AddonDevUIPrefConst.COLOR_XML_KEYWORD + AddonDevUIPrefConst.ITALIC_SUFFIX
				, false);
		
		PreferenceConverter.setDefault(store, AddonDevUIPrefConst.COLOR_XML_COMMENT, 
				AddonDevUIPrefConst.DEFAULT_COLOR_XML_COMMENT);
		store.setDefault(AddonDevUIPrefConst.COLOR_XML_COMMENT + AddonDevUIPrefConst.BOLD_SUFFIX
				, false);
		store.setDefault(AddonDevUIPrefConst.COLOR_XML_COMMENT + AddonDevUIPrefConst.ITALIC_SUFFIX
				, false);
		
		PreferenceConverter.setDefault(store, AddonDevUIPrefConst.COLOR_XML_STRING, 
				AddonDevUIPrefConst.DEFAULT_COLOR_XML_STRING);
		store.setDefault(AddonDevUIPrefConst.COLOR_XML_STRING + AddonDevUIPrefConst.BOLD_SUFFIX
				, false);
		store.setDefault(AddonDevUIPrefConst.COLOR_XML_STRING + AddonDevUIPrefConst.ITALIC_SUFFIX
				, false);		
		
		store.setDefault(AddonDevUIPrefConst.FIREFOX_ADDON_GUID, AddonDevUIPrefConst.DEFAULT_FIREFOX_ADDON_GUID);
		store.setDefault(AddonDevUIPrefConst.FIREFOX_ADDON_VERSION, AddonDevUIPrefConst.DEFAULT_FIREFOX_ADDON_VERSION);
		store.setDefault(AddonDevUIPrefConst.FIREFOX_ADDON_MINVERSION, AddonDevUIPrefConst.DEFAULT_FIREFOX_ADDON_MINVERSION);
		store.setDefault(AddonDevUIPrefConst.FIREFOX_ADDON_MAXVERSION, AddonDevUIPrefConst.DEFAULT_FIREFOX_ADDON_MAXVERSION);
		
		store.setDefault(AddonDevUIPrefConst.XULRUNNER_PATH, "");
		store.setDefault(AddonDevUIPrefConst.XULPREVIEW_W, 800);
		store.setDefault(AddonDevUIPrefConst.XULPREVIEW_H, 800);	
		
		store.setDefault(AddonDevUIPrefConst.XULPREVIEW_REFRESH_AUTO, false);
		store.setDefault(AddonDevUIPrefConst.XULPREVIEW_REFRESH_XUL, false);
		store.setDefault(AddonDevUIPrefConst.XULPREVIEW_REFRESH_DTD, false);
		store.setDefault(AddonDevUIPrefConst.XULPREVIEW_REFRESH_CSS, false);
	}

	@Override
	public void initializeDefaultPreferences() {
		// TODO Auto-generated method stub
	}

}
