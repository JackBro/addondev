package org.addondev.ui.preferences;

import org.addondev.plugin.AddonDevPlugin;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class XULPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private IPreferenceStore fStote;
	private DirectoryFieldEditor fXULRunnerDirectory;
	
	public XULPreferencePage() {
		// TODO Auto-generated constructor stub
		fStote = AddonDevPlugin.getDefault().getPreferenceStore();
		setPreferenceStore(fStote);
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		Composite parent = getFieldEditorParent();
		
		Label dirlabel = new Label(parent, SWT.NONE);
		dirlabel.setText("XULRunner Path");
		createDummyLabel(parent);
		
		fXULRunnerDirectory = new DirectoryFieldEditor("fXULRunnerDirectory",
				"fXULRunnerDirectory", parent);
		addField(fXULRunnerDirectory);
	}
	
	private void createDummyLabel(Composite parent)
	{
		Label dummy = new Label(parent, SWT.NONE);
	}
}
