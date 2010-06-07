package gef.example.helloworld.editor.overlay;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

public class OverlayFormEditor extends FormEditor {

	//WizardSelectionPage 
	@Override
	protected void addPages() {
		// TODO Auto-generated method stub
		
		try {
			addPage(new OverlayEditor(this, "overlay", "title"));
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

}
