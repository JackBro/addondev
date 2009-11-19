package jp.addondev.preferences;

import jp.addondev.plugin.AddonDevPlugin;

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
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

public class DebugPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	//private FileFieldEditor firefoxPath;
	private Text eclipsePort;
	private Text debuggerPort;
	
	private Text firebugServerConnectTimeout;

	public DebugPreferencePage() {
		// TODO Auto-generated constructor stub
		super("Debug");
		setPreferenceStore(AddonDevPlugin.getDefault().getPreferenceStore());	
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
	}
	 
	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		IPreferenceStore stote = getPreferenceStore();
			

		
//        Group group = new Group(parent, SWT.FILL);
//        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//        group.setText("FireFox");
        
        Composite composite1 = new Composite(parent, SWT.NONE);
        GridLayout layout1 = new GridLayout();
        layout1.numColumns = 3;
        composite1.setLayout(layout1);
        composite1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        
        //firefoxPath = new FileFieldEditor("firefoxpath","Path", composite1);	
        //firefoxPath.setStringValue(stote.getString(AddonDevPlugin.DEBUG_APP_PATH));   
		
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
		eclipsePort = addText(composite, "&Eclipse Port");
		eclipsePort.setText(stote.getString(AddonDevPlugin.DEBUG_ECLIPSEPORT));
		
		debuggerPort = addText(composite, "&Debugger Port");
		debuggerPort.setText(stote.getString(AddonDevPlugin.DEBUG_DEBUGGERPORT));
					
		return parent;	
	}

	private Text addText(Composite parent, String label) {
        Label L = new Label(parent, SWT.NONE);
        L.setText(label);

        Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        return text;
	}
	
	 private Text createGroup(Composite parent, String label) {
	        GridLayout layout = new GridLayout(2, false);
	        
	        Composite composite = new Composite(parent, SWT.FILL);
	        GridData data = new GridData(GridData.FILL_HORIZONTAL);
	        composite.setLayoutData(data);
	        composite.setLayout(layout);

	        Label L = new Label(composite, SWT.NONE);
	        L.setText(label);

	        Text text = new Text(composite, SWT.SINGLE | SWT.BORDER);
	        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        
	        return text;
	 }
	
	@Override
	protected void performDefaults() {
		IPreferenceStore stote = getPreferenceStore();
		//firefoxPath.setStringValue(stote.getDefaultString(AddonDevPlugin.DEBUG_APP_PATH));			
		debuggerPort.setText(stote.getDefaultString(AddonDevPlugin.DEBUG_DEBUGGERPORT));	
		eclipsePort.setText(stote.getString(AddonDevPlugin.DEBUG_ECLIPSEPORT));	
		firebugServerConnectTimeout.setText("20000");
		super.performDefaults();
	}
	

	
	@Override
	public boolean performOk() {
		IPreferenceStore stote = getPreferenceStore();
		//stote.setValue(AddonDevPlugin.DEBUG_APP_PATH, firefoxPath.getStringValue());		
		stote.setValue(AddonDevPlugin.DEBUG_DEBUGGERPORT, debuggerPort.getText());
		stote.setValue(AddonDevPlugin.DEBUG_ECLIPSEPORT, eclipsePort.getText());
		stote.setValue(AddonDevPlugin.FIREBUG_SERVER_CONEECT_TIMEOUT, firebugServerConnectTimeout.getText());
		return true;
	}
	

}
