package jp.addondev.preferences;

import jp.addondev.AddonDevPlugin;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class DefaultFilesPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private final String DEFAULT_GUID = "ec8030f7-c20a-464f-9b0e-13a3a9e97384";
	private final String DEFAULT_VERSION = "0.1.0.0";
	private final String DEFAULT_MINVERSION = "3.0";
	private final String DEFAULT_MAXVERSION = "3.5.*";
	
	
	private Text fguid;
	private Text fversion;
	private Text fminVersion;
	private Text fmaxVersion;
	
	public DefaultFilesPreferencePage() {
		// TODO Auto-generated constructor stub
		setPreferenceStore(AddonDevPlugin.getDefault().getPreferenceStore());
	}

	public DefaultFilesPreferencePage(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public DefaultFilesPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		IPreferenceStore stote = getPreferenceStore();
		
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
        fguid = addText(composite, "ID");
        fguid.setText(getValueFromStote(stote,AddonDevPlugin.DEFAULT_GUID, DEFAULT_GUID));
        
        fversion = addText(composite, "version");
        fversion.setText(getValueFromStote(stote,AddonDevPlugin.DEFAULT_VERSION, DEFAULT_VERSION));
        
        fminVersion = addText(composite, "minVersion");
        fminVersion.setText(getValueFromStote(stote,AddonDevPlugin.DEFAULT_MINVERSION, DEFAULT_MINVERSION));
        
        fmaxVersion = addText(composite, "maxVersion");
        fmaxVersion.setText(getValueFromStote(stote,AddonDevPlugin.DEFAULT_MAXVERSION, DEFAULT_MAXVERSION));
        
		return parent;
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}
	
	private Text addText(Composite parent, String label) {
        Label L = new Label(parent, SWT.NONE);
        L.setText(label);

        Text text = new Text(parent, SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        return text;		
	}
	@Override
	protected void performDefaults() {
		// TODO Auto-generated method stub
		super.performDefaults();
	}

	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		IPreferenceStore stote = getPreferenceStore();
		stote.setValue(AddonDevPlugin.DEFAULT_GUID, fguid.getText());		
		stote.setValue(AddonDevPlugin.DEFAULT_VERSION, fversion.getText());
		stote.setValue(AddonDevPlugin.DEFAULT_MINVERSION, fminVersion.getText());
		stote.setValue(AddonDevPlugin.DEFAULT_MAXVERSION, fmaxVersion.getText());
			
		return super.performOk();
	}

	private String getValueFromStote(IPreferenceStore stote, String name, String defaultvalue)
	{
		String res="";
		String val = stote.getString(name);
		if(val.equals(""))
		{
			res = defaultvalue;
		}
		
		return res;
	}

}
