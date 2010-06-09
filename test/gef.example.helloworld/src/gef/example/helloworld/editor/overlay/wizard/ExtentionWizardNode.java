package gef.example.helloworld.editor.overlay.wizard;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.swt.graphics.Point;

public class ExtentionWizardNode implements IWizardNode {

	private IWizard wizard;
	
	public ExtentionWizardNode(IWizard wizard) {
		this.wizard = wizard;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Point getExtent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWizard getWizard() {
		// TODO Auto-generated method stub
		return wizard;
	}

	@Override
	public boolean isContentCreated() {
		// TODO Auto-generated method stub
		return false;
	}

}
