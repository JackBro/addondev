package org.addondev.ui.preferences;

import org.addondev.core.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class AddonDevPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private IPreferenceStore fStote;
	
	private StringFieldEditor fGUID;
	private StringFieldEditor fVersion;
	private StringFieldEditor fMinVersion;
	private StringFieldEditor fMaxVersion;
	
	public AddonDevPreferencePage() {
		// TODO Auto-generated constructor stub
		fStote = AddonDevPlugin.getDefault().getPreferenceStore();
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
		fStote.setValue(PrefConst.FIREFOX_ADDON_GUID, fGUID.getStringValue());
		fStote.setValue(PrefConst.FIREFOX_ADDON_VERSION, fVersion.getStringValue());
		fStote.setValue(PrefConst.FIREFOX_ADDON_MINVERSION, fMinVersion.getStringValue());
		fStote.setValue(PrefConst.FIREFOX_ADDON_MAXVERSION, fMaxVersion.getStringValue());
		
		return super.performOk();
	}	

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		Composite parent = getFieldEditorParent();
		
//		Label debuglabel = new Label(parent, SWT.NONE);
//		debuglabel.setText("debug");
//		createDummyLabel(parent);
//
//		//fEclipsePort = createStringFieldEditor("name", "labelText", parent, fStote.getString(PrefConst.ECLIPSE_PORT));
//		//fDebuggerPort = createStringFieldEditor("name", "labelText", parent, fStote.getString(PrefConst.DEBUGGER_PORT));
//		
//		createDummyLabel(parent);
//		createDummyLabel(parent);
		
		Label projectlabel = new Label(parent, SWT.NONE);
		projectlabel.setText("project");
		createDummyLabel(parent);
		
		fGUID = createStringFieldEditor(PrefConst.FIREFOX_ADDON_GUID, "labelText", parent, fStote.getString(PrefConst.FIREFOX_ADDON_GUID));
		fVersion = createStringFieldEditor(PrefConst.FIREFOX_ADDON_VERSION, "labelText", parent, fStote.getString(PrefConst.FIREFOX_ADDON_VERSION));   
		fMinVersion = createStringFieldEditor(PrefConst.FIREFOX_ADDON_MINVERSION, "labelText", parent, fStote.getString(PrefConst.FIREFOX_ADDON_MINVERSION));
		fMaxVersion = createStringFieldEditor(PrefConst.FIREFOX_ADDON_MAXVERSION, "labelText", parent, fStote.getString(PrefConst.FIREFOX_ADDON_MAXVERSION));
	}
	
	private StringFieldEditor createStringFieldEditor(String name, String label, Composite parent, String value)
	{
		StringFieldEditor field = new StringFieldEditor(name, label, parent);
		field.setStringValue(value);
		return field;
	}
	
	private void createDummyLabel(Composite parent)
	{
		Label dummy = new Label(parent, SWT.NONE);
	}
}
