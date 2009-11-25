package org.addondev.preferences;

import org.addondev.plugin.AddonDevPlugin;
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
//	if (!getPreferenceStore().contains(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND)) {
//		RGB rgb= getControl().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB();
//		PreferenceConverter.setDefault(fOverlayStore, AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND, rgb);
//		PreferenceConverter.setDefault(getPreferenceStore(), AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND, rgb);
//	}
//	if (!getPreferenceStore().contains(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND)) {
//		RGB rgb= getControl().getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND).getRGB();
//		PreferenceConverter.setDefault(fOverlayStore, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, rgb);
//		PreferenceConverter.setDefault(getPreferenceStore(), AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, rgb);
//	}
	

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
		
		prefstote.setValue(PrefConst.ECLIPSE_PORT, feclipsePort.getText());
		prefstote.setValue(PrefConst.DEBUGGER_PORT, fdebuggerPort.getText());
		prefstote.setValue(PrefConst.DEFAULT_FIREFOX_ADDON_GUID, fguid.getText());
		prefstote.setValue(PrefConst.FIREFOX_ADDON_VERSION, fguid.getText());
		prefstote.setValue(PrefConst.FIREFOX_ADDON_MINVERSION, fminVersion.getText());
		prefstote.setValue(PrefConst.FIREFOX_ADDON_MAXVERSION, fmaxVersion.getText());
		
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
        
        addText(group, "&Eclipse Port", feclipsePort, PrefConst.ECLIPSE_PORT);
        addText(group, "&Debugger Port", fdebuggerPort, PrefConst.DEBUGGER_PORT);       
	}
	
	public void createDefaultPrefControl(Composite parent) {
        Group group= new Group(parent, SWT.NONE);
        group.setText("Default"); 
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        
        addText(group, "&ID", fguid, PrefConst.FIREFOX_ADDON_GUID);
        addText(group, "&Version", fversion, PrefConst.FIREFOX_ADDON_VERSION);    
        addText(group, "&MinVersion", fminVersion, PrefConst.FIREFOX_ADDON_MINVERSION);
        addText(group, "&MaxVersion", fmaxVersion, PrefConst.FIREFOX_ADDON_MAXVERSION);
	}

	private void addText(Composite parent, String label, Text text, String ID) {
        Label L = new Label(parent, SWT.NONE);
        L.setText(label);

        text = new Text(parent, SWT.SINGLE | SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.setText(getPreferenceStore().getString(ID));
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
