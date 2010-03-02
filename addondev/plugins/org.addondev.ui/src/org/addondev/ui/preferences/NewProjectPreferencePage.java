package org.addondev.ui.preferences;


import org.addondev.ui.AddonDevUIPlugin;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class NewProjectPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	
	private IPreferenceStore fStote;
	
	private StringFieldEditor fGUID;
	private StringFieldEditor fVersion;
	private StringFieldEditor fMinVersion;
	private StringFieldEditor fMaxVersion;
	
	public NewProjectPreferencePage() {
		// TODO Auto-generated constructor stub
		fStote = AddonDevUIPlugin.getDefault().getPreferenceStore();
		setPreferenceStore(fStote);
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		setTitle("Add");
	}
	
	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		fStote.setValue(AddonDevUIPrefConst.FIREFOX_ADDON_GUID, fGUID.getStringValue());
		fStote.setValue(AddonDevUIPrefConst.FIREFOX_ADDON_VERSION, fVersion.getStringValue());
		fStote.setValue(AddonDevUIPrefConst.FIREFOX_ADDON_MINVERSION, fMinVersion.getStringValue());
		fStote.setValue(AddonDevUIPrefConst.FIREFOX_ADDON_MAXVERSION, fMaxVersion.getStringValue());
		
		return super.performOk();
	}	

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		Composite parent = getFieldEditorParent();
		
		Label projectlabel = new Label(parent, SWT.NONE);
		projectlabel.setText("project");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		projectlabel.setLayoutData(gd);
		
		fGUID = createStringFieldEditor(AddonDevUIPrefConst.FIREFOX_ADDON_GUID, "ID", parent, fStote.getString(AddonDevUIPrefConst.FIREFOX_ADDON_GUID));
		fVersion = createStringFieldEditor(AddonDevUIPrefConst.FIREFOX_ADDON_VERSION, "Version", parent, fStote.getString(AddonDevUIPrefConst.FIREFOX_ADDON_VERSION));   
		fMinVersion = createStringFieldEditor(AddonDevUIPrefConst.FIREFOX_ADDON_MINVERSION, "MinVersion", parent, fStote.getString(AddonDevUIPrefConst.FIREFOX_ADDON_MINVERSION));
		fMaxVersion = createStringFieldEditor(AddonDevUIPrefConst.FIREFOX_ADDON_MAXVERSION, "MaxVersio", parent, fStote.getString(AddonDevUIPrefConst.FIREFOX_ADDON_MAXVERSION));
	}
	
	private StringFieldEditor createStringFieldEditor(String name, String label, Composite parent, String value)
	{
		StringFieldEditor field = new StringFieldEditor(name, label, parent);
		field.setStringValue(value);
		return field;
	}
}
