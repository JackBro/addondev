package gef.example.helloworld.editor.overlay.wizard;

import org.eclipse.jface.wizard.Wizard;

public class ExtentionWizard extends Wizard {

	private ExtentionWizardSelectionPage page;
	
	public Object getValue(){
		return page.getValue();
	}
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPages();
		page = new ExtentionWizardSelectionPage("");
		addPage(page);
		//addPage(new ExtentionWizardSelectionPage("2"));
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needsPreviousAndNextButtons() {
		// TODO Auto-generated method stub
		return true;//super.needsPreviousAndNextButtons();
	}

}
