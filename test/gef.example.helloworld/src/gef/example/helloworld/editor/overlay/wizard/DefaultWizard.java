package gef.example.helloworld.editor.overlay.wizard;

import org.eclipse.jface.wizard.Wizard;

public class DefaultWizard extends Wizard {

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
