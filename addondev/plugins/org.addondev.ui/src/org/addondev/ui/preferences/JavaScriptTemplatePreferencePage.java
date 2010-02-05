package org.addondev.ui.preferences;

import org.addondev.ui.AddonDevUIPlugin;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

public class JavaScriptTemplatePreferencePage extends TemplatePreferencePage
		implements IWorkbenchPreferencePage {

	public JavaScriptTemplatePreferencePage() {
		// TODO Auto-generated constructor stub
		try {
			setPreferenceStore(AddonDevUIPlugin.getDefault().getPreferenceStore());
			setTemplateStore(AddonDevUIPlugin.getDefault().getTemplateStore());
			setContextTypeRegistry(AddonDevUIPlugin.getDefault().getContextTypeRegistry());
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		AddonDevUIPlugin.getDefault().savePluginPreferences();
		return super.performOk();
	}
}
