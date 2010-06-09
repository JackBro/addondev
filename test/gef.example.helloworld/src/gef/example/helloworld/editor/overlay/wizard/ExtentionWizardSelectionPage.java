package gef.example.helloworld.editor.overlay.wizard;

import gef.example.helloworld.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ExtentionWizardSelectionPage extends WizardSelectionPage implements ISelectionChangedListener{

	private static Map<String, IWizard> elementMap = new HashMap<String, IWizard>();
	static{
//		elementMap.put("KeySet", KeySetModel.class);
//		elementMap.put("CommandSet", CommandSetModel.class);
//		elementMap.put("StringBundleSet", StringBundleSetModel.class);
//		
//		elementMap.put("ToolBox", ToolBoxModel.class);
//		elementMap.put("ToolBar", ToolBarModel.class);
		
		elementMap.put("Menu", new MenuWizard());
	}
	
	private Wizard fWizard;
	private Object value;
	
	public Object getValue() {
		return elementMap.get(value);
	}

	protected ExtentionWizardSelectionPage(String pageName, Wizard wizard) {
		super(pageName);
		// TODO Auto-generated constructor stub
		fWizard = wizard;	
		
		
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite c = new Composite(parent, SWT.NONE);
        c.setLayout(new GridLayout(2, false));
        GridData gd;
        new Label(c, SWT.NONE).setText("test sel");
        
        ListViewer listviewer = new ListViewer(c);
        ArrayList<String> keylist = new ArrayList<String>();
       
        for (String key : elementMap.keySet()) {
        	keylist.add(key);
		}
        Collections.sort(keylist);
        for (String string : keylist) {
        	listviewer.add(string);
		}
//        listviewer.add("add mennubar");
//        listviewer.add("add menupopup");
//        listviewer.add("add ContextMenu");
//        listviewer.add("add toolbarbutton");
        listviewer.addSelectionChangedListener(this);
        listviewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub
				
				value = ((IStructuredSelection)event.getSelection()).getFirstElement();
				String key = (String)value; 
				IWizard wizerd = elementMap.get(key);
				setSelectedNode(new ExtentionWizardNode(wizerd));
				//setPageComplete(true);
//				fWizard.performFinish();
//				fWizard.getShell().close();
//				fWizard.dispose();
//				fWizard.setContainer(null);
			}
		});
        //Dialog.applyDialogFont(container);
        setControl(c);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub
		value = ((IStructuredSelection)event.getSelection()).getFirstElement(); 
		//setPageComplete(true);
		value = ((IStructuredSelection)event.getSelection()).getFirstElement();
		String key = (String)value; 
		IWizard wizerd = elementMap.get(key);
		setSelectedNode(new ExtentionWizardNode(wizerd));
	}

}
