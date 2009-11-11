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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class AddonDevPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private IPreferenceStore prefstote;
	private Text feclipsePort;
	private Text fdebuggerPort;
	
	private Text fguid;
	private Text fversion;
	private Text fminVersion;
	private Text fmaxVersion;
	
	public AddonDevPreferencePage() {
		// TODO Auto-generated constructor stub
		setPreferenceStore(AddonDevPlugin.getDefault().getPreferenceStore());
	}

	public AddonDevPreferencePage(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public AddonDevPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createContents(Composite parent) {
		
		 prefstote = getPreferenceStore();
		
		// TODO Auto-generated method stub
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
//        Composite composite = new Composite(parent, SWT.NONE);
//        setControl(composite);
//        GridLayout gridLayout = new GridLayout();        
//        composite.setLayout(gridLayout); 	
		
		createDebugPrefControl(composite);
		createDefaultPrefControl(composite);
		
		return parent;
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		
		prefstote.setValue(AddonDevPlugin.DEBUG_ECLIPSEPORT, feclipsePort.getText());
		prefstote.setValue(AddonDevPlugin.DEBUG_DEBUGGERPORT, fdebuggerPort.getText());
		prefstote.setValue(AddonDevPlugin.DEFAULT_GUID, fguid.getText());
		prefstote.setValue(AddonDevPlugin.DEFAULT_VERSION, fguid.getText());
		prefstote.setValue(AddonDevPlugin.DEFAULT_MINVERSION, fminVersion.getText());
		prefstote.setValue(AddonDevPlugin.DEFAULT_MAXVERSION, fmaxVersion.getText());
		
		return super.performOk();
	}	
	
	public void createDebugPrefControl(Composite parent) {
        Group group= new Group(parent, SWT.NONE);
        group.setText("Debug"); 
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        
//	    Composite composite5 = new Composite(group, SWT.NONE);
//		GridLayout layout5 = new GridLayout();
//		layout5.numColumns = 2;
//		composite5.setLayout(layout5);
//		composite5.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        addText(group, "&Eclipse Port", feclipsePort, AddonDevPlugin.DEBUG_ECLIPSEPORT, "8083");
        addText(group, "&Debugger Port", fdebuggerPort, AddonDevPlugin.DEBUG_DEBUGGERPORT, "8084");       
	}
	
	public void createDefaultPrefControl(Composite parent) {
        Group group= new Group(parent, SWT.NONE);
        group.setText("Default"); 
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        
        addText(group, "&ID", fguid, AddonDevPlugin.DEFAULT_GUID, "{ec8030f7-c20a-464f-9b0e-13a3a9e97384}");
        addText(group, "&Version", fversion, AddonDevPlugin.DEFAULT_VERSION, "0.1.0");    
        addText(group, "&MinVersion", fminVersion, AddonDevPlugin.DEFAULT_MINVERSION, "3.0");
        addText(group, "&MaxVersion", fmaxVersion, AddonDevPlugin.DEFAULT_MAXVERSION, "3.5.*");
	}

	private void addText(Composite parent, String label, Text text, String ID, String defaultPref) {
        Label L = new Label(parent, SWT.NONE);
        L.setText(label);

        text = new Text(parent, SWT.SINGLE | SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.setText(getStoteValue(ID, defaultPref));
	}
	
	private String getStoteValue(String ID, String defaultvalue)
	{
		//String res="";
		String res = prefstote.getString(ID);
		if(res.equals(""))
		{
			res = defaultvalue;
		}	
		return res;
	}
}
