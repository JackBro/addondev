package gef.example.helloworld.editor.overlay.wizard;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class MenuWizard extends Wizard {

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
	       
	        Button button1 = new Button(c, SWT.RADIO);
	        button1.setText("1");
	        Button button2 = new Button(c, SWT.RADIO);
	        button2.setText("2");
	        
	        ListViewer list = new ListViewer(c);
	        list.add("menu_FilePopup");
	        list.add("menu_EditPopup");
	        list.add("menu_viewPopup");
	        list.add("menu_ToolsPopup");

	        //Dialog.applyDialogFont(container);
	        setControl(c);
		}

	}	
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPages();
		addPage(new MenuWizardPage("menu"));
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
