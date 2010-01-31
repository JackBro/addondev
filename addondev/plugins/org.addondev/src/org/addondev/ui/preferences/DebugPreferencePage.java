package org.addondev.ui.preferences;


import org.addondev.plugin.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class DebugPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {
	
	private class IntegerVerifyListener implements VerifyListener
	{
		@Override
		public void verifyText(VerifyEvent e) {
			try
			{
				int s = Integer.parseInt(e.text);
			}
			catch (NumberFormatException ex) {
				e.doit = false;
			}
		}
	}

	IPreferenceStore fStore;
	
	private IntegerVerifyListener fIntegerVerifyListener = new IntegerVerifyListener();
	
	private Button fAutoButton;
	private Text fEclipsePortStartText, fEclipsePortEndText;
	private Text fDebuggerStartPortText, fDebuggerStartEndPortText;
	
	private Button fManualButton;
	private Text fEclipsePortText, fDebuggerPortText;	
	

	public DebugPreferencePage() {
		// TODO Auto-generated constructor stub
		super(Messages.DebugPreferencePage_0);
		setPreferenceStore(AddonDevPlugin.getDefault().getPreferenceStore());	
		fStore = getPreferenceStore();
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
	}
	 
	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		createPortGroup(parent);
		
		fAutoButton.setSelection(fStore.getBoolean(PrefConst.DEBUG_PORT_AUTO));
		fManualButton.setSelection(!fStore.getBoolean(PrefConst.DEBUG_PORT_AUTO));
				
		fEclipsePortStartText.setText(String.valueOf(fStore.getInt(PrefConst.DEBUG_PORT_AUTO_ECLIPSE_START)));
		fEclipsePortEndText.setText(String.valueOf(fStore.getInt(PrefConst.DEBUG_PORT_AUTO_ECLIPSE_END)));
		fDebuggerStartPortText.setText(String.valueOf(fStore.getInt(PrefConst.DEBUG_PORT_AUTO_DEBUGGER_START)));
		fDebuggerStartEndPortText.setText(String.valueOf(fStore.getInt(PrefConst.DEBUG_PORT_AUTO_DEBUGGER_END)));
		
		fEclipsePortText.setText(String.valueOf(fStore.getInt(PrefConst.DEBUG_PORT_MANUAL_ECLIPSE)));
		fDebuggerPortText.setText(String.valueOf(fStore.getInt(PrefConst.DEBUG_PORT_MANUAL_DEBUGGER)));
		
		
		fEclipsePortStartText.setEnabled(fAutoButton.getSelection());
		fEclipsePortEndText.setEnabled(fAutoButton.getSelection());
		fDebuggerStartPortText.setEnabled(fAutoButton.getSelection());
		fDebuggerStartEndPortText.setEnabled(fAutoButton.getSelection());	
		fEclipsePortText.setEnabled(!fAutoButton.getSelection());
		fDebuggerPortText.setEnabled(!fAutoButton.getSelection());	
		
		return parent;	
	}
	
	 private void createPortGroup(Composite parent) {
			
			Group portgroup= new Group(parent, SWT.NONE);
	        GridLayout layout = new GridLayout();
	        portgroup.setLayout(layout);
	        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	        portgroup.setLayoutData(data);	
	        portgroup.setText(Messages.DebugPreferencePage_1);
	        
	        
	        //auto   
	        Composite rangComposite = new Composite(portgroup, SWT.NONE);
	        GridLayout rlayout = new GridLayout();
	        rlayout.numColumns = 4;
	        rangComposite.setLayout(rlayout);
	        
	        fAutoButton = new Button(rangComposite, SWT.RADIO);
	        fAutoButton.setText(Messages.DebugPreferencePage_2);
	        fAutoButton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					fManualButton.setSelection(false);
					
					fEclipsePortStartText.setEnabled(true);
					fEclipsePortEndText.setEnabled(true);
					fDebuggerStartPortText.setEnabled(true);
					fDebuggerStartEndPortText.setEnabled(true);
					
					fEclipsePortText.setEnabled(false);
					fDebuggerPortText.setEnabled(false);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
	        createDummy(rangComposite);
	        createDummy(rangComposite);
	        createDummy(rangComposite);
	        
	        Label eclipsePortRange = new Label(rangComposite, SWT.NONE);
	        eclipsePortRange.setText(Messages.DebugPreferencePage_3);
	        fEclipsePortStartText = createText(rangComposite);
	        Label eclipseRangeLabel = new Label(rangComposite, SWT.NONE);
	        eclipseRangeLabel.setText("-"); //$NON-NLS-1$
	        fEclipsePortEndText = createText(rangComposite);
	        Label debuggerPortRange = new Label(rangComposite, SWT.NONE);
	        debuggerPortRange.setText(Messages.DebugPreferencePage_5);
	        fDebuggerStartPortText = createText(rangComposite);
	        Label debuggerRangeLabel = new Label(rangComposite, SWT.NONE);
	        debuggerRangeLabel.setText("-"); //$NON-NLS-1$
	        fDebuggerStartEndPortText = createText(rangComposite);
	        
	        
	        
	        //manual
	        Composite assignComposite = new Composite(portgroup, SWT.NONE);
	        GridLayout alayout = new GridLayout();
	        alayout.numColumns = 2;
	        assignComposite.setLayout(alayout);  
	        
	        fManualButton = new Button(assignComposite, SWT.RADIO);
	        fManualButton.setText(Messages.DebugPreferencePage_7);
	        fManualButton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					fAutoButton.setSelection(false);
					
					fEclipsePortStartText.setEnabled(false);
					fEclipsePortEndText.setEnabled(false);
					fDebuggerStartPortText.setEnabled(false);
					fDebuggerStartEndPortText.setEnabled(false);
					
					fEclipsePortText.setEnabled(true);
					fDebuggerPortText.setEnabled(true);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
	        createDummy(assignComposite);

	        Label eclipsePort = new Label(assignComposite, SWT.NONE);
	        eclipsePort.setText(Messages.DebugPreferencePage_8);
	        
	        fEclipsePortText = createText(assignComposite);
			
	        Label debuggerPort = new Label(assignComposite, SWT.NONE);
	        debuggerPort.setText(Messages.DebugPreferencePage_9);
	        fDebuggerPortText = createText(assignComposite);
	 }
	
	@Override
	protected void performDefaults() {	
		fAutoButton.setSelection(fStore.getDefaultBoolean(PrefConst.DEBUG_PORT_AUTO));
		fManualButton.setSelection(!fStore.getDefaultBoolean(PrefConst.DEBUG_PORT_AUTO));
		
		fEclipsePortStartText.setText(String.valueOf(fStore.getDefaultInt(PrefConst.DEBUG_PORT_AUTO_ECLIPSE_START)));
		fEclipsePortEndText.setText(String.valueOf(fStore.getDefaultInt(PrefConst.DEBUG_PORT_AUTO_ECLIPSE_END)));
		fDebuggerStartPortText.setText(String.valueOf(fStore.getDefaultInt(PrefConst.DEBUG_PORT_AUTO_DEBUGGER_START)));
		fDebuggerStartEndPortText.setText(String.valueOf(fStore.getDefaultInt(PrefConst.DEBUG_PORT_AUTO_DEBUGGER_END)));
		
		fEclipsePortText.setText(String.valueOf(fStore.getDefaultInt(PrefConst.DEBUG_PORT_MANUAL_ECLIPSE)));
		fDebuggerPortText.setText(String.valueOf(fStore.getDefaultInt(PrefConst.DEBUG_PORT_MANUAL_DEBUGGER)));		
		
		super.performDefaults();
	}
	

	
	@Override
	public boolean performOk() {	
		fStore.setValue(PrefConst.DEBUG_PORT_AUTO, fAutoButton.getSelection());
		
		fStore.setValue(PrefConst.DEBUG_PORT_AUTO_ECLIPSE_START, Integer.parseInt(fEclipsePortStartText.getText()));
		fStore.setValue(PrefConst.DEBUG_PORT_AUTO_ECLIPSE_END, Integer.parseInt(fEclipsePortEndText.getText()));
		fStore.setValue(PrefConst.DEBUG_PORT_AUTO_DEBUGGER_START, Integer.parseInt(fDebuggerStartPortText.getText()));
		fStore.setValue(PrefConst.DEBUG_PORT_AUTO_DEBUGGER_END, Integer.parseInt(fDebuggerStartEndPortText.getText()));
		
		fStore.setValue(PrefConst.DEBUG_PORT_MANUAL_ECLIPSE, Integer.parseInt(fEclipsePortText.getText()));
		fStore.setValue(PrefConst.DEBUG_PORT_MANUAL_DEBUGGER, Integer.parseInt(fDebuggerPortText.getText()));
		
		return true;
	}
	
	private Text createText(Composite parent)
	{
        Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);	
        text.addVerifyListener(fIntegerVerifyListener);	
        
        return text;
	}
	
	private void createDummy(Composite parent)
	{
		Label l = new Label(parent, SWT.NONE);
	}
}
