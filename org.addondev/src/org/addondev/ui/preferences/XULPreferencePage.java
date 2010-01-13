package org.addondev.ui.preferences;

import java.io.File;
import java.io.IOException;

import org.addondev.plugin.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class XULPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private IPreferenceStore fStote;
	private FileFieldEditor fXULRunnerFile;
	private IntegerFieldEditor inteditorH, inteditorW;
	private Button fRegbutton;
	
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
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new FormLayout());
		
		Label dirlabel = new Label(composite, SWT.NONE);
		dirlabel.setText("XULRunner Path");
		FormData dirlabelfd = new FormData();
		dirlabelfd.top = new FormAttachment(0, 1);
		dirlabelfd.left = new FormAttachment(0, 1);
		dirlabel.setLayoutData(dirlabelfd);
	
		Composite dirparent = new Composite(composite, SWT.NONE);
		fXULRunnerFile = new FileFieldEditor(PrefConst.XULRUNNER_PATH,
				"XULRunnerFile", dirparent);
		fXULRunnerFile.setPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// TODO Auto-generated method stub
				File file = new File(fXULRunnerFile.getStringValue());
				fRegbutton.setEnabled(file.exists() && file.canExecute());
			}
		});
		FormData dirfd = new FormData();
		dirfd.top = new FormAttachment(dirlabel, 1);
		dirfd.left = new FormAttachment(0, 1);	
		dirfd.right = new FormAttachment(90, -1);
		dirparent.setLayoutData(dirfd);
		
		fRegbutton = new Button(composite, SWT.NONE);
		fRegbutton.setText("Regster");
		FormData regfd = new FormData();
		regfd.top = new FormAttachment(dirlabel, 1);
		regfd.left = new FormAttachment(dirparent, 1);	
		regfd.right = new FormAttachment(100, -1);	
		fRegbutton.setLayoutData(regfd);
		fRegbutton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				MessageBox msgbox = new MessageBox(composite.getShell(), SWT.ICON_INFORMATION | SWT.YES);
				msgbox.setText("");
				boolean res = register(fXULRunnerFile.getStringValue());
				if(res)
				{
					msgbox.setMessage("OK");
				}
				else
				{
					msgbox.setMessage("Not");
				}
				
				msgbox.open();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Label scrolllabel = new Label(composite, SWT.NONE);
		scrolllabel.setText("scroll");
		FormData scrollfd = new FormData();
		scrollfd.top = new FormAttachment(fRegbutton, 30);
		scrollfd.left = new FormAttachment(0, 1);
		scrolllabel.setLayoutData(scrollfd);

		Composite wparent = new Composite(composite, SWT.NONE);
		inteditorW = new IntegerFieldEditor(PrefConst.XULPREVIEW_W, "Minwidth", wparent);
		FormData wfd = new FormData();
		wfd.top = new FormAttachment(scrolllabel, 1);
		wfd.left = new FormAttachment(0, 1);
		wfd.right = new FormAttachment(50, -10);	
		wparent.setLayoutData(wfd);
		
		Composite hparent = new Composite(composite, SWT.NONE);
		inteditorH = new IntegerFieldEditor(PrefConst.XULPREVIEW_H, "MinHeight", hparent);
		FormData hfd = new FormData();
		hfd.top = new FormAttachment(scrolllabel, 1);
		hfd.left = new FormAttachment(wparent, 10);	
		hfd.right = new FormAttachment(100, -10);	
		hparent.setLayoutData(hfd);
		

		fXULRunnerFile.setStringValue(fStote.getString(PrefConst.XULRUNNER_PATH));
		inteditorW.setStringValue(fStote.getString(PrefConst.XULPREVIEW_W).toString());
		inteditorH.setStringValue(fStote.getString(PrefConst.XULPREVIEW_H).toString());
		
		return parent;
	}
	
	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		
		fStote.setValue(PrefConst.XULRUNNER_PATH, fXULRunnerFile.getStringValue());
		fStote.setValue(PrefConst.XULPREVIEW_W, inteditorH.getIntValue());
		fStote.setValue(PrefConst.XULPREVIEW_H, inteditorW.getIntValue());
		
		return super.performOk();
	}
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return super.isValid();
	}

	private boolean register(String xulpath)
	{
		//--register-global 
		//--unregister-global 
		//--register-user
		//--unregister-user 

		IPath path = new Path(xulpath );
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
