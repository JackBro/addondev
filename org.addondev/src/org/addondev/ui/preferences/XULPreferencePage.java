package org.addondev.ui.preferences;

import org.addondev.plugin.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
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
		createDummyLabel(parent);
		
		fXULRunnerDirectory = new DirectoryFieldEditor("fXULRunnerDirectory",
				"fXULRunnerDirectory", parent);
		addField(fXULRunnerDirectory);
		fXULRunnerDirectory.setPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button regbutton = new Button(parent, SWT.NONE);
		regbutton.setText("reg");
		regbutton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		createDummyLabel(parent);
		createDummyLabel(parent);
		
		Button unregbutton = new Button(parent, SWT.NONE);
		unregbutton.setText("unreg");
		unregbutton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		createDummyLabel(parent);
		createDummyLabel(parent);		
		
	}
	
	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		
		fStote.setValue(PrefConst.XULRUNNER_PATH, fXULRunnerDirectory.getStringValue());
		
		return super.performOk();
	}
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return super.isValid();
	}

	private void createDummyLabel(Composite parent)
	{
		Label dummy = new Label(parent, SWT.NONE);
	}
}
