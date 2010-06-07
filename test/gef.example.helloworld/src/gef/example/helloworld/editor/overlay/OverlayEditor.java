package gef.example.helloworld.editor.overlay;

import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

public class OverlayEditor extends FormPage {

	private OverlayMasterBlock black;
	
	public OverlayEditor(FormEditor editor, String id, String title) {
		super(editor, id, title);
		// TODO Auto-generated constructor stub
		black = new OverlayMasterBlock(editor.getEditorInput());
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		// TODO Auto-generated method stub
		super.createFormContent(managedForm);
		black.createContent(managedForm);
	}

}
