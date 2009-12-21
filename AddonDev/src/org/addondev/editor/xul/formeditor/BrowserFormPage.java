package org.addondev.editor.xul.formeditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.Page;

public class BrowserFormPage extends Page {

	public static final String ID = "browser";
	private Browser fBrowser;

	private boolean fFileLoaded = false;
	private Composite composite;
	
	
	
//	@Override
//	protected void createFormContent(IManagedForm managedForm) {
//		// TODO Auto-generated method stub
//		//super.createFormContent(managedForm);
//		ScrolledForm form = managedForm.getForm();
//		FormToolkit toolkit = managedForm.getToolkit();
//		Section section = toolkit.createSection(form.getBody(), 
//	            Section.TITLE_BAR | Section.DESCRIPTION);
//		//Composite composite = toolkit.createComposite(section);
//		section.setLayout(new FillLayout());
//		composite = toolkit.createComposite(section);
//		composite.setLayout(new FillLayout());
//		try {
//			String path = "D:\\program\\xulrunner";
//			//D:\program\xulrunner
//			System.setProperty("org.eclipse.swt.browser.XULRunnerPath", path);
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		section.setClient(composite);
//		//section.setLayoutData(new FillLayout());
//	}
	

	public void setDocument(String text)
	{
		if(fBrowser != null)
			fBrowser.setText(text);
	}
	
	public void setFile(IFile file)
	{
		String url="file:///"+file.getRawLocation().toString();
		
		if(fBrowser != null)
		{
			fBrowser.setUrl(url);
		}
	}


	protected Composite displayArea = null;
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		displayArea = new Composite( parent, SWT.NONE );
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginWidth = 1;
		gridLayout.marginHeight = 1;
		gridLayout.verticalSpacing = 1;
		displayArea.setLayout(gridLayout);
		
		GridData data;
		try {
			String path = "D:\\program\\xulrunner";
			//D:\program\xulrunner
			System.setProperty("org.eclipse.swt.browser.XULRunnerPath", path);
			fBrowser = new Browser(displayArea, SWT.MOZILLA);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
		
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 1;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		fBrowser.setLayoutData(data);
	}



	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
		//return null;
		return displayArea;
	}



	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
