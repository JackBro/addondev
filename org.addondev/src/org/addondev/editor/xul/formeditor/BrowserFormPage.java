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
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.Page;
//import org.mozilla.interfaces.nsIDOMDocument;
//import org.mozilla.interfaces.nsIDOMWindow;
//import org.mozilla.interfaces.nsIWebBrowser;
//import org.mozilla.xpcom.Mozilla;

public class BrowserFormPage extends Page {

	
	
	public static final String ID = "browser";
	private Browser fBrowser;

	private boolean fFileLoaded = false;
	private Composite composite;
	private final File fPreviewXULFile;
	
	LocationListener fLocationListener;
	
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		//fBrowser.stop();
		//fBrowser.
		//fBrowser.removeLocationListener(listener);
		//fBrowser.dispose();
		//fBrowser = null;
//		displayArea.getDisplay().syncExec(new Runnable() {
//					public void run() {
//
//						fBrowser.setUrl("about:blank");
//					}
//				});		
		super.dispose();
		

	}



	public BrowserFormPage(File file) {
		super();
		// TODO Auto-generated constructor stub
		fPreviewXULFile = file;
		//fLocationListener = new PreviewBrowserLocationListener(fBrowser);
		
	}



	public void setDocument(final String text)
	{
		if(fBrowser != null)
		{


			//String enc = (String) fBrowser.evaluate("return encodeURIComponent(" + "fXML" +");");
//			//String res = fXML.replaceAll("\"", "'");
//			
			
			
			setted = false;
			//fBrowser.execute("alert('res');");
			//fXML = text;
			//Control control =getControl();
			//fBrowser.getDisplay().syncExec(new Runnable() {
			//fBrowser.addLocationListener(fLocationListener);
			
			displayArea.getDisplay().asyncExec(new Runnable() {
			//Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {

//					String encdata = null;
//					try {
//						encdata = URLEncoder.encode(text, "UTF-8");
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					String res = encdata.replaceAll("\\+", " ");

					// TODO Auto-generated method stub
					
					try {
						boolean re = fBrowser.execute("preview('"+ text +"');");
						//boolean re = fBrowser.execute("preview('texttest2');");
						
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					//fBrowser.execute("getDoc();");
					
					//Object ob = fBrowser.getWebBrowser();
					//String uu = fBrowser.getUrl();
					//fBrowser.execute("getDoc();");
					//String tt = fBrowser.getText();

					

					int i=0;
					i++;					
					//fBrowser.execute("alert('res');");
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
		
		//fLocationListener = 
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
			//Mozilla.getInstance().initialize(new File(path));
			
			
			//D:\program\xulrunner
			System.setProperty("org.eclipse.swt.browser.XULRunnerPath", path);
			fBrowser = new Browser(displayArea, SWT.MOZILLA);
			
//			fBrowser.addOpenWindowListener(new OpenWindowListener() {
//				
//				@Override
//				public void open(WindowEvent event) {
//					// TODO Auto-generated method stub
//					System.out.println("open");
//					//if(event.browser != null)
//					//{
//					fBrowser.execute("getnode();");
//					//}
//				}
//			});
			fBrowser.addStatusTextListener(new StatusTextListener() {
				
				@Override
				public void changed(StatusTextEvent event) {
					// TODO Auto-generated method stub
					if(event.text != null && event.text.length() > 0)
					{
						String sss = event.text;
						
					}
					int i=0;
					i++;
				}
			});
			fBrowser.addProgressListener(new ProgressListener() {
                public void changed(ProgressEvent event) {
                 	//Browser browser = (Browser)event.getSource();
                	//nsIWebBrowser domBrowser = (nsIWebBrowser)browser.getWebBrowser(); 
                    //nsIWebBrowser webBrowser = (nsIWebBrowser)fBrowser.getWebBrowser();
                    //nsIDOMWindow window = webBrowser.getContentDOMWindow();
                    //nsIDOMDocument document = window.getDocument();
                	//fBrowser.execute("getDoc();");
                    //System.out.println("document");
                }

                public void completed(ProgressEvent event) {
                	Display.getDefault().syncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
		                	//String lat = ((String) fBrowser.evaluate("return rep();"));
		                	fBrowser.execute("getDoc();");
		                 	int i=0;
		                 	i++;
						}
					});
                	

                }
			});
			//nsIWebBrowser webBrowser = (nsIWebBrowser)fBrowser.getWebBrowser();
			
//			fBrowser.setUrl("about:blank");
			displayArea.getDisplay().asyncExec(new Runnable() {
			//Display.getDefault().syncExec(new Runnable() {
				//control.getDisplay().syncExec(new Runnable() {
				//getSite().getShell().getDisplay().syncExec(new Runnable() {
					public void run() {
						//URL url = getClass().getResource("preview.xul");
						String url="file://"+fPreviewXULFile.toURI().getRawPath();
						fBrowser.setUrl(url);
						//fBrowser.setUrl("about:blank");
						//fBrowser.setUrl("file:///D:/data/src/PDE/xpi/xuledit/xuledit.xul");
						//fBrowser.setUrl("file:///D:/data/src/PDE/workrepository/plugins/AddonDev/tmp.xul");
					}
				});
//			fBrowser.addLocationListener(new LocationListener() {
//
//				@Override
//				public void changed(LocationEvent event) {
//					// TODO Auto-generated method stub
//					event.display.asyncExec(new Runnable() {
//						
//						@Override
//						public void run() {
//							//String tmp = URLEncoder.encode("h+h", "UTF-8");
//							//fBrowser.execute("output('"+ fXML +"');");
//							try {
//								String tmp;
//
//									tmp = URLEncoder.encode(fXML, "UTF-8");
//									String res = tmp.replaceAll("\\+", " ");
//									//String enc = (String) fBrowser.evaluate("encodeURIComponent(" + fXML +");");
//									//String res = fXML.replaceAll("\"", "'");
//									fBrowser.execute("preview('"+ res +"');");
//								} catch (UnsupportedEncodingException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//
//								
//								//System.out.println("#####changed");
//						}
//					});
//					
//				}
//
//				@Override
//				public void changing(LocationEvent event) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//			});

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
	
	class PreviewBrowserLocationListener implements LocationListener
	{

		//private Browser fBrowser;
		public PreviewBrowserLocationListener(Browser browser) {
			super();
			//this.fBrowser = browser;
		}

		@Override
		public void changed(LocationEvent event) {
			// TODO Auto-generated method stub
			fBrowser.removeLocationListener(this);
			
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
				}
			});			
		}

		@Override
		public void changing(LocationEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
