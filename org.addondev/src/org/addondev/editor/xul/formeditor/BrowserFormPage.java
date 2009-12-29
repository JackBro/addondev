package org.addondev.editor.xul.formeditor;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
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
	
	public void setDocument(final File file, String text)
	{
		if(fBrowser != null)
		{
			setted = false;
			fXML = text;
			Control control =getControl();
			control.getDisplay().syncExec(new Runnable() {
			//getSite().getShell().getDisplay().syncExec(new Runnable() {
				public void run() {
					//URL url = getClass().getResource("preview.xul");
					String url="file://"+file.toURI().getRawPath();
					fBrowser.setUrl(url);
					//fBrowser.setUrl("file:///D:/data/src/PDE/xpi/xuledit/xuledit.xul");
					//fBrowser.setUrl("file:///D:/data/src/PDE/workrepository/plugins/AddonDev/tmp.xul");
				}
			});
		}
	}
	
//	public void setFile(IFile file)
//	{
//		String url="file:///"+file.getRawLocation().toString();
//		
//		if(fBrowser != null && !fFileLoaded)
//		{
//			fFileLoaded = true;
//			fBrowser.setUrl(url);
//		}
//	}
	
	String fXML;
//	public void setFile(File file, String xml)
//	{
//		Path path = new Path(file.getAbsolutePath());
//		String url="file:///"+path.toPortableString();
//		
//		if(fBrowser != null)
//		{
//			fXML = xml;
//			fFileLoaded = true;
//			fBrowser.setUrl(url);
//		}
//	}


	protected Composite displayArea = null;
	private boolean setted = false;
	
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
			
			fBrowser.addLocationListener(new LocationListener() {

				@Override
				public void changed(LocationEvent event) {
					// TODO Auto-generated method stub
					event.display.asyncExec(new Runnable() {
						
						@Override
						public void run() {
							//String tmp = URLEncoder.encode("h+h", "UTF-8");
							//fBrowser.execute("output('"+ fXML +"');");
							try {
								String tmp;

									tmp = URLEncoder.encode(fXML, "UTF-8");
									String res = tmp.replaceAll("\\+", " ");
									//String enc = (String) fBrowser.evaluate("encodeURIComponent(" + fXML +");");
									//String res = fXML.replaceAll("\"", "'");
									fBrowser.execute("preview('"+ res +"');");
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								
								System.out.println("#####changed");
						}
					});
					
				}

				@Override
				public void changing(LocationEvent event) {
					// TODO Auto-generated method stub
					
				}
				
			});

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
