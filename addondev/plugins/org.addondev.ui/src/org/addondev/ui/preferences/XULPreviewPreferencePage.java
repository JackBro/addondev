package org.addondev.ui.preferences;

import java.io.File;
import java.io.IOException;

import org.addondev.core.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class XULPreviewPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private class IntegerVerifyListener implements VerifyListener
	{
		@Override
		public void verifyText(VerifyEvent e) {
			try
			{
				int s = Integer.parseInt(e.text);
			}
			catch (NumberFormatException ex) {
				e.doit = false;
			}
		}
	}	
	
	private IPreferenceStore fStote;
	private FileFieldEditor fXULRunnerFile;
	private IntegerFieldEditor fInteditorH, fInteditorW;
	private Button fRegbutton;
	private IntegerVerifyListener fIntegerVerifyListener = new IntegerVerifyListener();
	
	public XULPreviewPreferencePage() {
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
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
//		composite.setLayout(new FormLayout());
		
		Group xulrunnergroup= new Group(composite, SWT.NONE);
        GridLayout xullayout = new GridLayout();
        xulrunnergroup.setLayout(xullayout);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        xulrunnergroup.setLayoutData(data);	
        xulrunnergroup.setText("XULRunner Path");
		
//		Label dirlabel = new Label(composite, SWT.NONE);
//		dirlabel.setText("XULRunner Path");
//		FormData dirlabelfd = new FormData();
//		dirlabelfd.top = new FormAttachment(0, 1);
//		dirlabelfd.left = new FormAttachment(0, 1);
//		dirlabel.setLayoutData(dirlabelfd);
	
		Composite xulfileparent = new Composite(xulrunnergroup, SWT.NONE);
		xulfileparent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fXULRunnerFile = new FileFieldEditor(AddonDevUIPrefConst.XULRUNNER_PATH,
				"XULRunnerFile", xulfileparent);
		fXULRunnerFile.setStringValue("dummy");
		fXULRunnerFile.setPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// TODO Auto-generated method stub
				File file = new File(fXULRunnerFile.getStringValue());
				boolean enabled = file.exists() && file.canExecute() && file.getName().contains("xulrunner");
				fRegbutton.setEnabled(enabled);
				//setValid(enabled);
			}
		});
				
//		FormData dirfd = new FormData();
//		dirfd.top = new FormAttachment(dirlabel, 1);
//		dirfd.left = new FormAttachment(0, 1);	
//		dirfd.right = new FormAttachment(100, -1);
//		xulfileparent.setLayoutData(dirfd);
		
		fRegbutton = new Button(xulrunnergroup, SWT.NONE);
		fRegbutton.setText("Regster");
//		FormData regfd = new FormData();
//		regfd.top = new FormAttachment(dirlabel, 1);
//		regfd.left = new FormAttachment(xulfileparent, 1);	
//		regfd.right = new FormAttachment(100, -1);	
//		fRegbutton.setLayoutData(regfd);
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
		
		Group scrollgroup= new Group(composite, SWT.NONE);
        GridLayout scrolllayout = new GridLayout();
        
        //scrolllayout.numColumns = 2;
        scrollgroup.setLayout(scrolllayout);
        GridData scrolldata = new GridData(GridData.FILL_HORIZONTAL);//GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        //scrolldata.horizontalSpan = 5;
        scrollgroup.setLayoutData(scrolldata);      
        scrollgroup.setText("XULRunner Path");
		
//		Label scrolllabel = new Label(parent, SWT.NONE);
//		scrolllabel.setText("scroll");
//		FormData scrollfd = new FormData();
//		scrollfd.top = new FormAttachment(xulfileparent, 30);
//		scrollfd.left = new FormAttachment(0, 1);
//		scrolllabel.setLayoutData(scrollfd);

		Composite sparent = new Composite(scrollgroup, SWT.NONE);
        GridData scrolldata2 = new GridData(GridData.FILL_HORIZONTAL);
        scrolldata2.horizontalSpan = 5;
        sparent.setLayoutData(scrolldata2);
        GridLayout scrolllayout2 = new GridLayout();
        scrolllayout2.numColumns = 2;
        sparent.setLayout(scrolllayout2);
        
		//Composite wparent = new Composite(scrollgroup, SWT.NONE);
        //GridData scrolldata2 = new GridData(GridData.FILL_HORIZONTAL);
        //scrolldata2.horizontalSpan = 5;
		//wparent.setLayoutData(scrolldata2);
		fInteditorW = new IntegerFieldEditor(AddonDevUIPrefConst.XULPREVIEW_W, "Minwidth", sparent);
		fInteditorW.getTextControl(sparent).addVerifyListener(fIntegerVerifyListener);
//		FormData wfd = new FormData();
//		wfd.top = new FormAttachment(scrolllabel, 1);
//		wfd.left = new FormAttachment(0, 1);
//		wfd.right = new FormAttachment(50, -10);	
//		wparent.setLayoutData(wfd);
		
		//Composite hparent = new Composite(scrollgroup, SWT.NONE);
		//hparent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//Composite hparent = new Composite(scrollgroup, SWT.NONE);
        //GridData scrolldata2 = new GridData(GridData.FILL_HORIZONTAL);
       // scrolldata2.horizontalSpan = 5;
        //hparent.setLayoutData(scrolldata2);
		fInteditorH = new IntegerFieldEditor(AddonDevUIPrefConst.XULPREVIEW_H, "MinHeight", sparent);
		fInteditorH.getTextControl(sparent).addVerifyListener(fIntegerVerifyListener);
//		FormData hfd = new FormData();
//		hfd.top = new FormAttachment(scrolllabel, 1);
//		hfd.left = new FormAttachment(wparent, 10);	
//		hfd.right = new FormAttachment(100, -10);	
//		hparent.setLayoutData(hfd);
		
		fXULRunnerFile.setStringValue(fStote.getString(AddonDevUIPrefConst.XULRUNNER_PATH));
		fInteditorW.setStringValue(fStote.getString(AddonDevUIPrefConst.XULPREVIEW_W).toString());
		fInteditorH.setStringValue(fStote.getString(AddonDevUIPrefConst.XULPREVIEW_H).toString());
		
		return parent;
	}
	
	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		fStote.setValue(AddonDevUIPrefConst.XULRUNNER_PATH, fXULRunnerFile.getStringValue());
		fStote.setValue(AddonDevUIPrefConst.XULPREVIEW_W, fInteditorH.getIntValue());
		fStote.setValue(AddonDevUIPrefConst.XULPREVIEW_H, fInteditorW.getIntValue());
	
		return super.performOk();
	}

	private boolean register(String xulpath)
	{
		//--register-global 
		//--unregister-global
		
		//--register-user
		//--unregister-user
		//user.reginfo

		boolean res = false;
		
		IPath path = new Path(xulpath );
		String fullpath = path.toOSString();

		ProcessBuilder pb = new ProcessBuilder(fullpath, "--register-user");	
		try {
			Process p = pb.start();
			int ret = p.waitFor();
			res = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
}
