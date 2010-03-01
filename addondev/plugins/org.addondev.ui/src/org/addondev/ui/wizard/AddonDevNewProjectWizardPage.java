package org.addondev.ui.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
		//fid.setText(page1.getProjectName() + "@dev.org");
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
	
	private Text fcreator;
	private Text fhomepageURL;
	
	
	private Button fPrefCheck, fPreferenceCheck, fToolbarButtonCheck, 
	fMenuCheck, fContextMenuCheck, fSidebarCheck;
	
	public WizardNewProjectCreationPage page1;

	protected AddonDevNewProjectWizardPage(String pageName) {
		super(pageName);
		setTitle("Fire FoxExtension");
		setDescription("Fire FoxExtension install.rdt");
		
		fStote = AddonDevUIPlugin.getDefault().getPreferenceStore();
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
	
		createInstallInfo(container);
        createOption(container);
        
      	doValidate();
      	
		setControl(container);
	}
	
	private Text addText(Composite parent, String label, String defaultValue) {
        Label L = new Label(parent, SWT.NONE);
        L.setText(label);

        Text text = new Text(parent, SWT.BORDER );
        text.setText(defaultValue);
        
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		text.setLayoutData(gd);

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
	
	private void createInstallInfo(Composite parent)
	{
		Group installgroup = new Group(parent, SWT.NONE);
		installgroup.setText("install");
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = layout.marginHeight = 3;
		installgroup.setLayout(layout);
		installgroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label label = new Label(installgroup, SWT.NONE);
		label.setText("ID");

        fid = new Text(installgroup, SWT.BORDER );
        fid.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fid.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				doValidate();
			}
        	
        });

        Button uuidbutton = new Button(installgroup, SWT.NONE);
        uuidbutton.setText("Generate");
        uuidbutton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				fid.setText("{"+ UUID.randomUUID().toString() +"}");
			}
		});
        

      	fversion = addText(installgroup, "version", fStote.getString(AddonDevUIPrefConst.FIREFOX_ADDON_VERSION));
      	fApplicationID = addText(installgroup, "Application ID", fStote.getString(AddonDevUIPrefConst.FIREFOX_ADDON_GUID));
      	fminVersion = addText(installgroup, "minVersion", fStote.getString(AddonDevUIPrefConst.FIREFOX_ADDON_MINVERSION));
      	fmaxVersion = addText(installgroup, "maxVersion", fStote.getString(AddonDevUIPrefConst.FIREFOX_ADDON_MAXVERSION));
      	fcreator = addText(installgroup, "creator");
      	fhomepageURL = addText(installgroup, "homepageURL");		
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
 
        fPreferenceCheck = new Button(group, SWT.CHECK);
        fPreferenceCheck.setText("Preference");
        
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
	
	public List<String> getFilesParam()
	{
		ArrayList<String> param = new ArrayList<String>();
		param.add("templates/project/firefox/base/templates.xml");
		
		if(fContextMenuCheck.getSelection()) 	param.add("templates/project/firefox/option/context/templates.xml");
		if(fMenuCheck.getSelection()) 			param.add("templates/project/firefox/option/menu/templates.xml");
		if(fSidebarCheck.getSelection()) 		param.add("templates/project/firefox/option/sidebar/templates.xml");
		if(fToolbarButtonCheck.getSelection()) 	param.add("templates/project/firefox/option/toolbarbutton/templates.xml");
		if(fPreferenceCheck.getSelection()) 	param.add("templates/project/firefox/option/preference/templates.xml");
		
		return param;
	}
}
