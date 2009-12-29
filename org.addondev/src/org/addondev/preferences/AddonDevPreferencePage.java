package org.addondev.preferences;

import org.addondev.plugin.AddonDevPlugin;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class AddonDevPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
//	if (!getPreferenceStore().contains(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND)) {
//		RGB rgb= getControl().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB();
//		PreferenceConverter.setDefault(fOverlayStore, AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND, rgb);
//		PreferenceConverter.setDefault(getPreferenceStore(), AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND, rgb);
//	}
//	if (!getPreferenceStore().contains(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND)) {
//		RGB rgb= getControl().getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND).getRGB();
//		PreferenceConverter.setDefault(fOverlayStore, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, rgb);
//		PreferenceConverter.setDefault(getPreferenceStore(), AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, rgb);
//	}
	

	private IPreferenceStore fStote;
	private StringFieldEditor fEclipsePort;
	private StringFieldEditor fDebuggerPort;
	
	private StringFieldEditor fguid;
	private StringFieldEditor fversion;
	private StringFieldEditor fminVersion;
	private StringFieldEditor fmaxVersion;
	
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
		
		fStote.setValue(PrefConst.ECLIPSE_PORT, fEclipsePort.getStringValue());
		fStote.setValue(PrefConst.DEBUGGER_PORT, fDebuggerPort.getStringValue());
		fStote.setValue(PrefConst.DEFAULT_FIREFOX_ADDON_GUID, fguid.getStringValue());
		fStote.setValue(PrefConst.FIREFOX_ADDON_VERSION, fguid.getStringValue());
		fStote.setValue(PrefConst.FIREFOX_ADDON_MINVERSION, fminVersion.getStringValue());
		fStote.setValue(PrefConst.FIREFOX_ADDON_MAXVERSION, fmaxVersion.getStringValue());
		
		return super.performOk();
	}	

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		Composite parent = getFieldEditorParent();
		
		Label debuglabel = new Label(parent, SWT.NONE);
		debuglabel.setText("debug");
		createDummyLabel(parent);

		fEclipsePort = createStringFieldEditor("name", "labelText", parent, fStote.getString(PrefConst.ECLIPSE_PORT));
		fDebuggerPort = createStringFieldEditor("name", "labelText", parent, fStote.getString(PrefConst.DEBUGGER_PORT));
		
		createDummyLabel(parent);
		createDummyLabel(parent);
		
		Label projectlabel = new Label(parent, SWT.NONE);
		projectlabel.setText("project");
		createDummyLabel(parent);
		
		fguid = createStringFieldEditor("name", "labelText", parent, fStote.getString(PrefConst.FIREFOX_ADDON_GUID));
		fversion = createStringFieldEditor("name", "labelText", parent, fStote.getString(PrefConst.FIREFOX_ADDON_VERSION));   
		fminVersion = createStringFieldEditor("name", "labelText", parent, fStote.getString(PrefConst.FIREFOX_ADDON_MINVERSION));
		fmaxVersion = createStringFieldEditor("name", "labelText", parent, fStote.getString(PrefConst.FIREFOX_ADDON_MAXVERSION));
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
