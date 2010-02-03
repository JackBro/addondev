package org.addondev.ui.preferences;


import org.addondev.core.AddonDevPlugin;
import org.addondev.ui.AddonDevUIPlugin;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class JavaScriptEditorPreferencePage extends
		FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	//private IPreferenceStore prefstote;
	public static final String JAVASCRIPT_COLOR_COMMENT = "org.addondev.preferences.javascript.color.comment";

	public JavaScriptEditorPreferencePage() {
		// TODO Auto-generated constructor stub
		setPreferenceStore(AddonDevUIPlugin.getDefault().getPreferenceStore());
	}

	public JavaScriptEditorPreferencePage(int style) {
		super(style);
		// TODO Auto-generated constructor stub
	}

	public JavaScriptEditorPreferencePage(String title, int style) {
		super(title, style);
		// TODO Auto-generated constructor stub
	}

	public JavaScriptEditorPreferencePage(String title,
			ImageDescriptor image, int style) {
		super(title, image, style);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		//prefstote = getPreferenceStore();
		
		Composite p = getFieldEditorParent();
		addField(new ColorFieldEditor(
				JAVASCRIPT_COLOR_COMMENT, "Timeout to connect to shell (secs).", p));
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	
}
