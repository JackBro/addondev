package gef.example.helloworld.editor.overlay.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

public class ExtentionWizardSelectionPage extends WizardSelectionPage implements ISelectionChangedListener{

	private Map<String, IWizard> elementMap = new HashMap<String, IWizard>();
	private Object value;
	
	public Object getValue() {
		return value;
	}

	protected ExtentionWizardSelectionPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
		
		elementMap.put("Menu", initWizard(new MenuWizard()));
		elementMap.put("ToolBarButton", initWizard(new ToolBarButtonWizard()));
		elementMap.put("ContextMenu", initWizard(new ContextMenuWizard()));
	}

	private IWizard initWizard(AbstractXULWizard wizard){
		wizard.setSelectionListener(new FinishPerformSelectionLisner() {
			
			@Override
			public void finishSelected(Event e) {
				// TODO Auto-generated method stub
				value = e.data;
			}
		});
		
		return wizard;
	}
	
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        GridData gd;
        new Label(composite, SWT.NONE).setText("test sel");
        
        ListViewer listviewer = new ListViewer(composite);
        listviewer.getList().setLayoutData(new GridData(GridData.FILL_BOTH));
        ArrayList<String> keylist = new ArrayList<String>();
       
        for (String key : elementMap.keySet()) {
        	keylist.add(key);
		}
        Collections.sort(keylist);
        for (String string : keylist) {
        	listviewer.add(string);
		}

        listviewer.addSelectionChangedListener(this);
        listviewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub
				
				value = ((IStructuredSelection)event.getSelection()).getFirstElement();
				String key = (String)value; 
				IWizard wizerd = elementMap.get(key);
				setSelectedNode(new ExtentionWizardNode(wizerd));
			}
		});
        //Dialog.applyDialogFont(container);
        setControl(composite);
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
