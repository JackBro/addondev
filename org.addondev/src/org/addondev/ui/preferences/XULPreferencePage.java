package org.addondev.ui.preferences;

import java.io.File;
import java.io.IOException;

import org.addondev.plugin.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
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
	private IntegerFieldEditor inteditorH, inteditorW;
	
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
		//createDummyLabel(parent);
		
		fXULRunnerDirectory = new DirectoryFieldEditor(PrefConst.XULRUNNER_PATH,
				"XULRunnerDirectory", parent);
		addField(fXULRunnerDirectory);
		//fXULRunnerDirectory.fillIntoGrid(parent, 2);
		fXULRunnerDirectory.setStringValue(fStote.getString(PrefConst.XULRUNNER_PATH));
		fXULRunnerDirectory.setPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button regbutton = new Button(parent, SWT.NONE);
		regbutton.setText("register-global");
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
		//createDummyLabel(parent);
		//createDummyLabel(parent);
		
		Button unregbutton = new Button(parent, SWT.NONE);
		unregbutton.setText("unregister-global");
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
		
		//createbrankl(parent);
		Label scrolllabel = new Label(parent, SWT.NONE);
		scrolllabel.setText("scroll");
		createDummyLabel(parent);
		createDummyLabel(parent);
		inteditorH = new IntegerFieldEditor("h", "MinHeight", parent);
		createDummyLabel(parent);
		inteditorW = new IntegerFieldEditor("w", "Minwidth", parent);
		//createDummyLabel(parent);
		addField(inteditorH);
		addField(inteditorW);
		
	}
	
	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		
		fStote.setValue(PrefConst.XULRUNNER_PATH, fXULRunnerDirectory.getStringValue());
		fStote.setValue(PrefConst.XULRUNNER_PATH, inteditorH.getStringValue());
		fStote.setValue(PrefConst.XULRUNNER_PATH, inteditorW.getStringValue());
		
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
	
	private void createbrankl(Composite parent)
	{
		createDummyLabel(parent);
		createDummyLabel(parent);
		createDummyLabel(parent);
	}	
	
	private boolean register(String xulpath)
	{
		//--register-global 
		//--unregister-global 
		//--register-user
		//--unregister-user 

		IPath path = new Path(xulpath ).append("xulrunner");
		String fullpath = path.toOSString();

		ProcessBuilder pb = new ProcessBuilder(fullpath,"--register-global");	
		try {
			Process p = pb.start();
			int ret = p.waitFor();
			
			IPath filepath = new Path(xulpath ).append("global.reginfo");
			File file = filepath.toFile();
			if(file.exists())
			{
				System.out.println("exists");
				return true;
			}
			else
			{
				System.out.println("not exists");
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
