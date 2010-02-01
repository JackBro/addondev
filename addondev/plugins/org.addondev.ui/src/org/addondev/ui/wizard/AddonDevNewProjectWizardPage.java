package org.addondev.ui.wizard;


import org.addondev.core.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
		super.setVisible(visible);
	}

	private IPreferenceStore fStote;
	
	private Text fid;
	private Text fversion;
	
	private Text ftargetApplicationid;
	private Text fminVersion;
	private Text fmaxVersion;
	
	//private Text fname;
	private Text fcreator;
	private Text fhomepageURL;
	
	public WizardNewProjectCreationPage page1;

	protected AddonDevNewProjectWizardPage(String pageName) {
		super(pageName);
		//setPageComplete(false);
		// TODO Auto-generated constructor stub
		setTitle("Fire FoxExtension");
		setDescription("Fire FoxExtension install.rdt");
		
		fStote = AddonDevPlugin.getDefault().getPreferenceStore();
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub	
		//String projectname = page1.getProjectName();
		
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		//fid = addText(composite, "ID", projectname + "@dev.org");
        fid = addText(composite, "ID");
      	fversion = addText(composite, "version", fStote.getString(PrefConst.FIREFOX_ADDON_VERSION));
      	
      	ftargetApplicationid = addText(composite, "targetApplication ID", fStote.getString(PrefConst.FIREFOX_ADDON_GUID));
      	fminVersion = addText(composite, "minVersion", fStote.getString(PrefConst.FIREFOX_ADDON_MINVERSION));
      	fmaxVersion = addText(composite, "maxVersion", fStote.getString(PrefConst.FIREFOX_ADDON_MAXVERSION));
      	
      	//fname = addText(composite, "name", projectname);
      	//fname = addText(composite, "name");
      	fcreator = addText(composite, "creator");
      	fhomepageURL = addText(composite, "homepageURL");
				
      	
      	doValidate();
      	
		setControl(composite);
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
	
	private String getPreference(String name)
	{
		return	AddonDevPlugin.getDefault().getPreferenceStore().getString(name);
	}
	
	public String getID()
	{
		return fid.getText();
	}
	public String getVersion()
	{
		return fversion.getText();
	}
	public String getTargetApplicationID()
	{
		return ftargetApplicationid.getText();
	}
	public String getMinVersion()
	{
		return fminVersion.getText();
	}
	public String getMaxVersion()
	{
		return fmaxVersion.getText();
	}
//	public String getName()
//	{
//		return fname.getText();
//	}
	public String getCreator()
	{
		return fcreator.getText();
	}
	public String geHomepageURL()
	{
		return fhomepageURL.getText();
	}

}
