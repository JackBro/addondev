package org.addondev.ui.wizard;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.addondev.util.Locale;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class AddonDevNewProjectWizardPage extends WizardPage {

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		fid.setText(page1.getProjectName() + "@dev.org");
		//fname.setText(page1.getProjectName());
        //fCheckboxViewer.setInput(Locale.values());
		super.setVisible(visible);
	}

	private IPreferenceStore fStote;
	
	private Text fid;
	private Text fversion;
	
	private Text fApplicationID;
	private Text fminVersion;
	private Text fmaxVersion;
	
	//private Text fname;
	private Text fcreator;
	private Text fhomepageURL;
	
	private CheckboxTableViewer fCheckboxViewer;
	private Combo creator;
	
	private Button fPrefCheck, fToolbarCheck, fToolbarButtonCheck, 
	fMenuCheck, fContextMenuCheck, fSidebarCheck;
	
	private ArrayList<Locale> fLocales;
	
	public WizardNewProjectCreationPage page1;

	protected AddonDevNewProjectWizardPage(String pageName) {
		super(pageName);
		//setPageComplete(false);
		// TODO Auto-generated constructor stub
		setTitle("Fire FoxExtension");
		setDescription("Fire FoxExtension install.rdt");
		
		fStote = AddonDevUIPlugin.getDefault().getPreferenceStore();
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub	
		//String projectname = page1.getProjectName();
		
        Composite pcomposite = new Composite(parent, SWT.NONE);
        GridLayout playout = new GridLayout();
        playout.numColumns = 1;
        pcomposite.setLayout(playout);
        //pcomposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Group installgroup = new Group(pcomposite, SWT.NONE);
		installgroup.setText("install");
        GridLayout installlayout = new GridLayout();
        installlayout.numColumns = 1;
        installgroup.setLayout(installlayout);
        installgroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
        Composite composite = new Composite(installgroup, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		//fid = addText(composite, "ID", projectname + "@dev.org");
        fid = addText(composite, "ID");
      	fversion = addText(composite, "version", fStote.getString(AddonDevUIPrefConst.FIREFOX_ADDON_VERSION));
      	
      	fApplicationID = addText(composite, "targetApplication ID", fStote.getString(AddonDevUIPrefConst.FIREFOX_ADDON_GUID));
      	fminVersion = addText(composite, "minVersion", fStote.getString(AddonDevUIPrefConst.FIREFOX_ADDON_MINVERSION));
      	fmaxVersion = addText(composite, "maxVersion", fStote.getString(AddonDevUIPrefConst.FIREFOX_ADDON_MAXVERSION));
      	
      	//fname = addText(composite, "name", projectname);
      	//fname = addText(composite, "name");
      	fcreator = addText(composite, "creator");
      	fhomepageURL = addText(composite, "homepageURL");
      	
      	
//		Group localegroup = new Group(pcomposite, SWT.NONE);
//		localegroup.setText("Locale");     	
//        GridLayout localelayout = new GridLayout();
//        //localelayout.numColumns = 1;
//		localegroup.setLayout(localelayout);
////		localegroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));  
//	    GridData data = new GridData(GridData.FILL_BOTH);
//	    localegroup.setLayoutData(data);
//		
//      	//CheckboxTableViewer view = CheckboxTableViewer.newCheckList(parent, SWT.NONE);
//        // Checkbox table viewer of decorators
//	    fCheckboxViewer = CheckboxTableViewer.newCheckList(localegroup,
//                SWT.BORDER  | SWT.V_SCROLL);
//      	//checkboxViewer.getTable().getVerticalBar().setVisible(true);
//        //checkboxViewer.getTable().setLayoutData(
//        //        new GridData(GridData.FILL_HORIZONTAL));
//        //checkboxViewer.getTable().setFont(decoratorsComposite.getFont());
//	    fCheckboxViewer.setLabelProvider(new LabelProvider() {
//            public String getText(Object element) {
//                return ((Locale) element).getName();
//            }
//        });
//	    
//	    fCheckboxViewer.addCheckStateListener(new ICheckStateListener() {
//			
//			@Override
//			public void checkStateChanged(CheckStateChangedEvent event) {
//				// TODO Auto-generated method stub
//				if(event.getElement() instanceof Locale)
//				{
//					String locale = ((Locale)event.getElement()).getName();
//					if(event.getChecked())
//					{
//						creator.add(locale);
//					}
//					else
//					{
//						creator.remove(locale);
//					}
//				}
//			}
//		});
//
//        GridData tabledata = new GridData(GridData.FILL_BOTH);
//        fCheckboxViewer.getTable().setLayoutData(tabledata);
//        fCheckboxViewer.setContentProvider(new ArrayContentProvider());
//        //fCheckboxViewer.setInput(Locale.values());
//      	
//        Label label = new Label(localegroup, SWT.NONE);
//        label.setText("：");
//        
//        creator = new Combo(localegroup, SWT.READ_ONLY);
//        creator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));  
        
        createOption(pcomposite);
        
      	doValidate();
      	
		setControl(pcomposite);
	}
	
	private Text addText(Composite parent, String label, String defaultValue) {
        Label L = new Label(parent, SWT.NONE);
        L.setText(label);

        Text text = new Text(parent, SWT.BORDER);
        text.setText(defaultValue);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        text.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				doValidate();
			}
        	
        });
        
        return text;		
	}
	
	private Text addText(Composite parent, String label) {
		return addText(parent, label, "");
	}
	
	private void createOption(Composite parent)
	{
		Group group = new Group(parent, SWT.NONE);
		group.setText("Option");     	
        GridLayout localelayout = new GridLayout();
        group.setLayout(localelayout);
	    GridData data = new GridData(GridData.FILL_BOTH);
	    group.setLayoutData(data);
	    
        fPrefCheck = new Button(group, SWT.CHECK);
        fPrefCheck.setText("pref");
 
        fToolbarCheck = new Button(group, SWT.CHECK);
        fToolbarCheck.setText("Toolbar");
        
        fToolbarButtonCheck = new Button(group, SWT.CHECK);
        fToolbarButtonCheck.setText("ToolbarButton");
        
        fMenuCheck = new Button(group, SWT.CHECK);
        fMenuCheck.setText("Menu");
        
        fContextMenuCheck = new Button(group, SWT.CHECK);
        fContextMenuCheck.setText("ContextMenu");
        
        fSidebarCheck = new Button(group, SWT.CHECK);
        fSidebarCheck.setText("Sidebar");
        		
	}
	
	private void doValidate()
	{
		if(fid.getText().length() ==0)
		{
			setErrorMessage("");
			setPageComplete(false);			
			return;
		}
		setErrorMessage(null);
		setPageComplete(true);
	}
	
	public Map<String, String> getInstallParam()
	{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("name", page1.getProjectName());
		param.put("id", fid.getText());
		param.put("version", fversion.getText());
		param.put("appid", fApplicationID.getText());
		param.put("minversion", fminVersion.getText());
		param.put("maxversion", fmaxVersion.getText());
		param.put("description", "");
		param.put("creator", fcreator.getText());
		param.put("homepageurl", fhomepageURL.getText());	
		
		param.put("locale", "en-US");	
		
		return param;
	}
	
	public Map<String, String> getFileParam()
	{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("install.rdf", "install.rdf");
		param.put("chrome.manifest", "chrome.manifest");
		param.put("main.js", "chrome/content/main.js");
		param.put("overlay.xul", "chrome/content/overlay.xul");
		param.put("prefs.js", "defaults/preferences/prefs.js");
		
		if(fPrefCheck.getSelection())  
			param.put("options.xul", "chrome/content/options.xul");
		
		if(fSidebarCheck.getSelection()) 
		{
			param.put("sidebar.xul", "chrome/content/sidebar.xul");
			param.put("sidebar.js", "chrome/content/sidebar.js");
		}

		param.put("overlay.dtd", "chrome/locale/en-US/overlay.dtd");
		param.put("overlay.css", "chrome/skin/overlay.css");	
		
		return param;
	}
	
	public Map<String, String> getOptionParam()
	{
		HashMap<String, String> param = new HashMap<String, String>();
		if(fContextMenuCheck.getSelection()) 	param.put("context", "overlay.xul");
		if(fMenuCheck.getSelection()) 			param.put("menu", "overlay.xul");
		if(fSidebarCheck.getSelection()) 		param.put("sidebar", "overlay.xul");
		if(fToolbarButtonCheck.getSelection()) 	param.put("toolbarbutton", "overlay.xul");
		if(fToolbarCheck.getSelection()) 		param.put("toolbar", "overlay.xul");
		
		return param;
	}

	public Locale[] getLocals()
	{
		
		ArrayList<Locale> locales = new ArrayList<Locale>();
		Object[] objs = fCheckboxViewer.getCheckedElements();
		for (Object obj : objs) {
			if(obj instanceof Locale)
			{
				locales.add((Locale) obj);
			}
		}

		return locales.toArray(new Locale[locales.size()]);
	}
}
