package gef.example.helloworld.editor.overlay.wizard;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ExtentionWizardSelectionPage extends WizardSelectionPage implements ISelectionChangedListener{

	protected ExtentionWizardSelectionPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
		
		setSelectedNode(new ExtentionWizardNode());
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite c = new Composite(parent, SWT.NONE);
        c.setLayout(new GridLayout(2, false));
        GridData gd;
        new Label(c, SWT.NONE).setText("test");
        ListViewer listviewer = new ListViewer(parent);
        listviewer.add("add mennubar");
        listviewer.add("add menupopup");
        listviewer.add("add ContextMenu");
        listviewer.add("add toolbarbutton");
        listviewer.addSelectionChangedListener(this);
        
        //Dialog.applyDialogFont(container);
        setControl(c);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub
		//getWizard().getContainer().updateButtons();
	}

}
