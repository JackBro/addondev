package gef.example.helloworld.editor.overlay.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class MenuWizardPage extends WizardPage {

	protected MenuWizardPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite c = new Composite(parent, SWT.NONE);
        c.setLayout(new GridLayout(2, false));
        GridData gd;
        new Label(c, SWT.NONE).setText("menu");
        
        //Dialog.applyDialogFont(container);
        setControl(c);
	}

}
