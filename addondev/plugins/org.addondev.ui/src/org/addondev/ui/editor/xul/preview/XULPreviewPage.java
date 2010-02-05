package org.addondev.ui.editor.xul.preview;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.part.Page;

public class XULPreviewPage extends Page{

	private class PreviewBrowserProgressListener implements ProgressListener
	{

		private Browser fBrowser;
		private String fXUL;
		
		public PreviewBrowserProgressListener(Browser browser, String xul) {
			super();
			fBrowser = browser;
			fXUL = xul;
		}

		@Override
		public void changed(ProgressEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void completed(ProgressEvent event) {
			// TODO Auto-generated method stub
			fBrowser.removeProgressListener(this);
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//boolean re = fBrowser.execute("preview('"+ fXUL +"');");
					boolean re = fBrowser.execute("rep('"+ fXUL +"');");
					//fBrowser.refresh();
					int i=0;
					i++;
				}
				
			});
		}
		
	}
	
	private TabFolder fTabFolder ;
	private Composite fStackComposite;
	private StackLayout fStackLayout;
	private Composite fLinkComposite;
	private ArrayList<Browser> fBrowserList = new ArrayList<Browser>();
	private ArrayList<String> fXULList = new ArrayList<String>();
	private String fPreviewXULURL;
	
	public XULPreviewPage(File file) {
		super();
		fPreviewXULURL = "file://"+file.toURI().getRawPath();
	}

	@Override
	public void createControl(Composite parent) {
		fStackComposite = new Composite(parent, SWT.NONE);
		fStackComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		fStackLayout = new StackLayout();
		fStackComposite.setLayout(fStackLayout);
		
		fLinkComposite = createLinkControl(fStackComposite);		
		
		fTabFolder = new TabFolder(fStackComposite, SWT.NONE);
		
		String path = AddonDevUIPlugin.getDefault().getPreferenceStore().getString(AddonDevUIPrefConst.XULRUNNER_PATH);
		if(!new File(path).exists())
		{
			fStackLayout.topControl = fLinkComposite;
			//fLinkComposite.layout();
		}
		else
		{
			fStackLayout.topControl = fTabFolder;
			//fTabFolder.layout();
		}
	}

	@Override
	public Control getControl() {
		return fStackComposite;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	int cnt=0;
	public void setDocument(List<String> xuls) {
		if(xuls.size() == 0) return;
		
		String path = AddonDevUIPlugin.getDefault().getPreferenceStore().getString(AddonDevUIPrefConst.XULRUNNER_PATH);
		if(!new File(path).exists()) return;
		
		if(fStackLayout.topControl != fTabFolder)
		{
			fStackLayout.topControl = fTabFolder;
			fTabFolder.layout();
		}
		
//		TabItem[] ii = fTabFolder.getItems();
//		for (TabItem tabItem : ii) {
//			tabItem.dispose();
//		}
//		fBrowserList.clear();
		
		int diff = xuls.size() - fTabFolder.getItems().length;
		if(diff > 0)
		{
			for (int i = 0; i < diff; i++) {
				Composite composite = null;
				try
				{
					composite = createBrowser(fTabFolder);
					
				}
				catch (Exception e) {
					// TODO: handle exception
					if(fStackLayout.topControl != fLinkComposite)
					{
						fStackLayout.topControl = fLinkComposite;
						fLinkComposite.layout();
					}
					return;
				}
				
				TabItem item = new TabItem(fTabFolder, SWT.NONE);
				item.setText("--");
				item.setControl(composite);
			}
		}
		else if(diff < 0)
		{
			diff = Math.abs(diff);
			TabItem[] items = fTabFolder.getItems();
			for (int i = 0; i < diff; i++) {
				fBrowserList.remove(fBrowserList.size()-1);
				items[items.length-1].dispose();
			}
		}
		
		for (int i = 0; i < xuls.size(); i++) {
			final String xul = xuls.get(i);
			final Browser browser = fBrowserList.get(i);
			
//			if(browser.getUrl().equals(fPreviewXULURL))
//			{
//				Display.getDefault().asyncExec(new Runnable() {
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						boolean re = browser.execute("preview('"+ xul +"');");
//						
//					}
//					
//				});				
//			}
//			else
//			{
				browser.addProgressListener(new PreviewBrowserProgressListener(browser, xul));		
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
							//URL url = getClass().getResource("preview.xul");
//							String url="file:///D:/data/src/PDE/workrepository/plugins/addondev/plugins/org.addondev.ui/preview.xul";
//							String url2="file:///D:/data/src/PDE/workrepository/plugins/addondev/plugins/org.addondev.ui/preview2.xul";
//							//browser.refresh();
//							if(cnt%2==0)
//							{
//								browser.setUrl(url);
//							}
//							else
//							{
//								browser.setUrl(url2);
//							}
						//Browser.clearSessions();
							browser.setUrl(fPreviewXULURL);
							//browser.redraw();
							//browser.refresh();
							//fBrowser.setUrl("about:blank");
							cnt++;
						}
				});								
//			}
			
		}
	}
	
	public void refresf()
	{
		for (Browser browser : fBrowserList) {
			browser.refresh();
		}
	}
	
	public void reLoad()
	{
		for (Browser browser : fBrowserList) {
			browser.refresh();
		}
	}
	
	private Composite createLinkControl(Composite parent)
	{
		Composite form = new Composite(parent, SWT.NONE);
		form.setLayout(new FormLayout());
		
		Link link = new Link(form, SWT.CENTER);
		link.setText("in the <a>XULRunner Pref</a> preference page.");
		
		FormData linkfd = new FormData();
		linkfd.top = new FormAttachment(50, 1);
		linkfd.left = new FormAttachment(50, 1);	
		linkfd.right = new FormAttachment(100, -1);
		linkfd.bottom = new FormAttachment(100, -1);
		link.setLayoutData(linkfd);
		link.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				linkClicked();
			}
		});	
		
		return form;
	}

	protected void linkClicked() {
		// TODO Auto-generated method stub
		String pageId = "org.addondev.ui.XULPreferencepages";
		PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(
				 null, pageId, new String[]{pageId}, null);
		dialog.open();
	}

	protected Composite createBrowser(Composite parent) {
		Composite form = new Composite(parent, SWT.NONE);
		form.setLayoutData(new GridData(GridData.FILL_BOTH));
		form.setLayout(new FormLayout());

		ScrolledComposite scroll = new ScrolledComposite(form, SWT.H_SCROLL | SWT.V_SCROLL);
		//scroll.setLayout(new FillLayout());
		//scroll.setExpandHorizontal(true);
		//scroll.setExpandVertical(true);
		FormData linkfd = new FormData();
		linkfd.top = new FormAttachment(0, 1);
		linkfd.left = new FormAttachment(0, 1);	
		linkfd.right = new FormAttachment(100, -1);
		linkfd.bottom = new FormAttachment(100, -1);
		scroll.setLayoutData(linkfd);
		
		Composite composite = new Composite(scroll, SWT.NONE);
		composite.setLayout(new GridLayout());
		scroll.setContent(composite);
		scroll.setMinSize(400,400);
		//scroll.setMinSize(0,0);
		scroll.setExpandHorizontal(true);
		scroll.setExpandVertical(true);

		String path = AddonDevUIPlugin.getDefault().getPreferenceStore().getString(AddonDevUIPrefConst.XULRUNNER_PATH);
		IPath dir = new Path(path).removeLastSegments(1);
		System.setProperty("org.eclipse.swt.browser.XULRunnerPath", dir.toOSString());
		final Browser browser = new Browser(composite, SWT.MOZILLA);
		
		fBrowserList.add(browser);
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));

//		browser.addStatusTextListener(new StatusTextListener() {
//			
//			@Override
//			public void changed(StatusTextEvent event) {
//				// TODO Auto-generated method stub
//				if(event.text != null && event.text.length() > 0)
//				{
//					String sss = event.text;
//					
//				}
//			}
//		});
//		browser.addProgressListener(new ProgressListener() {
//            public void changed(ProgressEvent event) {
//            }
//
//            public void completed(ProgressEvent event) {
//            	Display.getDefault().syncExec(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//	                	//String lat = ((String) fBrowser.evaluate("return rep();"));
//	                	//fBrowser.execute("getDoc();");
//	                	//fBrowser.execute("rep(" + + ");");
//	                 	int i=0;
//	                 	i++;
//					}
//				});
//            }
//		});
//
//		Display.getDefault().asyncExec(new Runnable() {
//			public void run() {
//					//URL url = getClass().getResource("preview.xul");
//					//String url="file://"+fPreviewXULFile.toURI().getRawPath();
//					browser.setUrl(fPreviewXULURL);
//					//fBrowser.setUrl("about:blank");
//				}
//			});
		return form;
	}
}
