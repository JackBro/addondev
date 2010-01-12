package org.addondev.editor.xul.preview;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.addondev.editor.xul.XULEditor;
import org.addondev.plugin.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.part.Page;


public class BrowserFormPage extends Page {

	public static final String ID = "browser";
	private Browser fBrowser;
	private final File fPreviewXULFile;
	
	private ScrolledComposite scroll;
	
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

			//setted = false;
			//fBrowser.execute("alert('res');");
			//fXML = text;
			//Control control =getControl();
			//fBrowser.getDisplay().syncExec(new Runnable() {
			//fBrowser.addLocationListener(fLocationListener);
			
			displayArea.getDisplay().syncExec(new Runnable() {
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
						//fBrowser.execute("previewdom('"+ text +"');");
						
						//fBrowser.execute("rep('" + text + "');");
						//boolean re = fBrowser.execute("send();");
						
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					//fBrowser.execute("getDoc();");
					
					//Object ob = fBrowser.getWebBrowser();
					//String uu = fBrowser.getUrl();
					//fBrowser.execute("getDoc();");
					//String tt = fBrowser.getText();
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
	
	private void getEditorPart(int offset)
	{
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (IWorkbenchWindow iWorkbenchWindow : windows) {
			//iWorkbenchWindow.getActivePage().
			//IEditorReference[] editorref = iWorkbenchWindow.getActivePage().getEditorReferences();
			IEditorPart editor = iWorkbenchWindow.getActivePage().getActiveEditor();
			if(editor instanceof XULEditor)
			{
				XULEditor xule = (XULEditor)editor;
				int line = xule.getl(offset);
				int i=0;
				i++;
			}
		}
	}
	
	@Override
	public void createControl(Composite parent) {
		
		//fLocationListener = 
		// TODO Auto-generated method stub
		displayArea = new Composite( parent, SWT.NONE );
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.numColumns = 1;
//		gridLayout.marginWidth = 1;
//		gridLayout.marginHeight = 1;
//		gridLayout.verticalSpacing = 1;
//		displayArea.setLayout(gridLayout);
		displayArea.setLayout(new FillLayout());

		try {
			String path = "D:\\program\\xulrunner";
			//String path = AddonDevPlugin.getDefault().getPreferenceStore().getString(PrefConst.XULRUNNER_PATH);
			if(new File(path).exists())
			{
				createBrowser(displayArea, path);
			}
			else
			{
				Link link = new Link(displayArea, SWT.CENTER);
				link.setText("in the <a>XULRunner Pref</a> preference page.");
				GridData data = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
				link.setLayoutData(data);
				link.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						linkClicked();
					}
				});
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	

	}

	protected void createBrowser(Composite parent, String path) {
		//D:\program\xulrunner
		
		
		System.setProperty("org.eclipse.swt.browser.XULRunnerPath", path);

		//ScrolledComposite 
		scroll = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		//scroll.setLayout(new FillLayout());
		//scroll.setExpandHorizontal(true);
		//scroll.setExpandVertical(true);

		Composite composite = new Composite(scroll, SWT.NONE);
		composite.setLayout(new GridLayout());
		scroll.setContent(composite);
		scroll.setMinSize(400,400);
		scroll.setExpandHorizontal(true);
		scroll.setExpandVertical(true);
		
		fBrowser = new Browser(composite, SWT.MOZILLA);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 1;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		fBrowser.setLayoutData(data);
//		 GridData gd = new GridData();
//		 gd = new GridData(GridData.FILL_BOTH);
//         //gd.heightHint = 30;
//         //gd.widthHint = 30;
//         fBrowser.setLayoutData(gd);
         
		
//		fBrowser.addOpenWindowListener(new OpenWindowListener() {
//			
//			@Override
//			public void open(WindowEvent event) {
//				// TODO Auto-generated method stub
//				System.out.println("open");
//				//if(event.browser != null)
//				//{
//				fBrowser.execute("getnode();");
//				//}
//			}
//		});
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
	                	//fBrowser.execute("getDoc();");
	                	//fBrowser.execute("rep(" + + ");");
	                 	int i=0;
	                 	i++;
					}
				});
            }
		});

		displayArea.getDisplay().asyncExec(new Runnable() {
		//Display.getDefault().syncExec(new Runnable() {
			//control.getDisplay().syncExec(new Runnable() {
			//getSite().getShell().getDisplay().syncExec(new Runnable() {
				public void run() {
					//URL url = getClass().getResource("preview.xul");
					String url="file://"+fPreviewXULFile.toURI().getRawPath();
					fBrowser.setUrl(url);
					//fBrowser.setUrl("about:blank");
				}
			});
//		fBrowser.addLocationListener(new LocationListener() {
//
//			@Override
//			public void changed(LocationEvent event) {
//				// TODO Auto-generated method stub
//				event.display.asyncExec(new Runnable() {
//					
//					@Override
//					public void run() {
//						//String tmp = URLEncoder.encode("h+h", "UTF-8");
//						//fBrowser.execute("output('"+ fXML +"');");
//						try {
//							String tmp;
//
//								tmp = URLEncoder.encode(fXML, "UTF-8");
//								String res = tmp.replaceAll("\\+", " ");
//								//String enc = (String) fBrowser.evaluate("encodeURIComponent(" + fXML +");");
//								//String res = fXML.replaceAll("\"", "'");
//								fBrowser.execute("preview('"+ res +"');");
//							} catch (UnsupportedEncodingException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//
//							
//							//System.out.println("#####changed");
//					}
//				});
//				
//			}
//
//			@Override
//			public void changing(LocationEvent event) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});


	}

	protected void setScrollMinSize(final int width, final int height)
	{
		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				scroll.setMinSize(width, height);
			}
		});
	}
	
	protected void linkClicked() {
		// TODO Auto-generated method stub
		String pageId = "org.addondev.ui.XULPreferencepages";
		PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(
				 null, pageId, new String[]{pageId}, null);
		dialog.open();
	}



	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
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
