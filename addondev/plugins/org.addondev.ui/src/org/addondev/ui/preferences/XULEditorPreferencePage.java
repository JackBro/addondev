package org.addondev.ui.preferences;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.editor.PropertyChangeSourceViewerConfiguration;
import org.addondev.ui.editor.javascript.JavaScriptConfiguration;
import org.addondev.ui.editor.javascript.JavaScriptPartitionScanner;
import org.addondev.ui.editor.xml.XMLConfiguration;
import org.addondev.ui.editor.xml.XMLPartitionScanner;
import org.addondev.util.FileUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class XULEditorPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private static class IntegerVerifyListener implements VerifyListener
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
	
	class XULSyntaxColorPage extends SyntaxColorPage
	{
		private String[][] COLOR_STRINGS = new String[][] {
				{"tag", AddonDevUIPrefConst.COLOR_XML_TAG},
				{"keyword", AddonDevUIPrefConst.COLOR_XML_KEYWORD}, 
				{"commnet", AddonDevUIPrefConst.COLOR_XML_COMMENT},
				{"string", AddonDevUIPrefConst.COLOR_XML_STRING}
			};
		
		@Override
		protected String[][] getColorStrings() {
			// TODO Auto-generated method stub
			return COLOR_STRINGS;
		}

		@Override
		protected IDocument getDocument() {
			// TODO Auto-generated method stub
			InputStream in = getClass().getResourceAsStream("xmlpreview.xml");
			String text = "";
			try {
				text = FileUtil.getContent(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			IDocument document = new Document(text);
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new XMLPartitionScanner(),
					new String[] {
						IDocument.DEFAULT_CONTENT_TYPE,
						XMLPartitionScanner.XML_CDATA,
						XMLPartitionScanner.XML_COMMENT,
						XMLPartitionScanner.XML_TAG});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
			
			return document;
		}

		@Override
		protected TextSourceViewerConfiguration getSourceViewerConfiguration() {
			// TODO Auto-generated method stub
			PropertyChangeSourceViewerConfiguration conf = new XMLConfiguration();
			return (TextSourceViewerConfiguration)conf;
		}
		
	}
	
	private IPreferenceStore fStote;
	
	private XULSyntaxColorPage fXULSyntaxColorPage;
	
	private FileFieldEditor fXULRunnerFile;
	private IntegerFieldEditor fInteditorH, fInteditorW;
	private Button fRegbutton;
	private IntegerVerifyListener fIntegerVerifyListener = new IntegerVerifyListener();
	
	private Button autob, xulb, dtdb, cssb;
	
	public XULEditorPreferencePage() {
		// TODO Auto-generated constructor stub
		fStote = AddonDevUIPlugin.getDefault().getPreferenceStore();
		setPreferenceStore(fStote);
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		TabFolder tabFolder = new TabFolder(composite,SWT.NULL);
		
		TabItem item1 = new TabItem(tabFolder,SWT.NULL);
		item1.setText("Editor");
		item1.setControl(createEditor(tabFolder));
		
		TabItem item2 = new TabItem(tabFolder,SWT.NULL);
		item2.setText("Preview");
		item2.setControl(createPreview(tabFolder));		
		
		return parent;
	}
	
	private Control createEditor(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));	
		
		fXULSyntaxColorPage = new XULSyntaxColorPage();
		fXULSyntaxColorPage.createControl(composite);
		
		return composite;
	}
	
	private Control createPreview(Composite parent)
	{
		final Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));		
		
		Group xulrunnergroup= new Group(composite, SWT.NONE);
        GridLayout xullayout = new GridLayout();
        xulrunnergroup.setLayout(xullayout);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        xulrunnergroup.setLayoutData(data);	
        xulrunnergroup.setText("XULRunner Path");
	
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
		
		fRegbutton = new Button(xulrunnergroup, SWT.NONE);
		fRegbutton.setText("Regster");
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
        
        scrollgroup.setLayout(scrolllayout);
        GridData scrolldata = new GridData(GridData.FILL_HORIZONTAL);//GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        scrollgroup.setLayoutData(scrolldata);      
        scrollgroup.setText("XULRunner Path");
		

		Composite sparent = new Composite(scrollgroup, SWT.NONE);
        GridData scrolldata2 = new GridData(GridData.FILL_HORIZONTAL);
        scrolldata2.horizontalSpan = 5;
        sparent.setLayoutData(scrolldata2);
        GridLayout scrolllayout2 = new GridLayout();
        scrolllayout2.numColumns = 2;
        sparent.setLayout(scrolllayout2);
        
		fInteditorW = new IntegerFieldEditor(AddonDevUIPrefConst.XULPREVIEW_W, "Minwidth", sparent);
		fInteditorW.getTextControl(sparent).addVerifyListener(fIntegerVerifyListener);
		
		fInteditorH = new IntegerFieldEditor(AddonDevUIPrefConst.XULPREVIEW_H, "MinHeight", sparent);
		fInteditorH.getTextControl(sparent).addVerifyListener(fIntegerVerifyListener);
		
		
//auto
		Group autogroup= new Group(composite, SWT.NONE);
        GridLayout autolayput = new GridLayout();
        autogroup.setLayout(autolayput);
        autogroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));	
        autogroup.setText("auto"); 
        
        autob = new Button(autogroup, SWT.CHECK);
        autob.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				boolean enabled = autob.getSelection();
				xulb.setEnabled(enabled);
				dtdb.setEnabled(enabled);
				cssb.setEnabled(enabled);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
        GridData bdata = new GridData(GridData.FILL_HORIZONTAL);
        bdata.horizontalIndent = 10;

        xulb = new Button(autogroup, SWT.CHECK);
        xulb.setText("XUL");
        xulb.setLayoutData(bdata);

        dtdb = new Button(autogroup, SWT.CHECK);
        dtdb.setText("dtdb");
        dtdb.setLayoutData(bdata);
        
        cssb = new Button(autogroup, SWT.CHECK);
        cssb.setText("cssb");
        cssb.setLayoutData(bdata);
		
        setValues();
        
        return composite;
	}
	
	
	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		fStote.setValue(AddonDevUIPrefConst.XULRUNNER_PATH, fXULRunnerFile.getStringValue());
		fStote.setValue(AddonDevUIPrefConst.XULPREVIEW_W, fInteditorH.getIntValue());
		fStote.setValue(AddonDevUIPrefConst.XULPREVIEW_H, fInteditorW.getIntValue());
	
		return super.performOk();
	}

	protected void setValues()
	{
		fXULRunnerFile.setStringValue(fStote.getString(AddonDevUIPrefConst.XULRUNNER_PATH));
		fInteditorW.setStringValue(fStote.getString(AddonDevUIPrefConst.XULPREVIEW_W).toString());
		fInteditorH.setStringValue(fStote.getString(AddonDevUIPrefConst.XULPREVIEW_H).toString());		
		
		autob.setSelection(fStote.getBoolean(AddonDevUIPrefConst.XULPREVIEW_REFRESH_AUTO));	
		xulb.setSelection(fStote.getBoolean(AddonDevUIPrefConst.XULPREVIEW_REFRESH_XUL));	
		dtdb.setSelection(fStote.getBoolean(AddonDevUIPrefConst.XULPREVIEW_REFRESH_DTD));	
		cssb.setSelection(fStote.getBoolean(AddonDevUIPrefConst.XULPREVIEW_REFRESH_CSS));	
		
		boolean enabled = autob.getSelection();
		xulb.setEnabled(enabled);
		dtdb.setEnabled(enabled);
		cssb.setEnabled(enabled);
		
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
