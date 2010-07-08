package org.addondev.ui.editor.manifest;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class ManifestFormPage extends FormPage {

	public ManifestFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		// TODO Auto-generated method stub
		super.createFormContent(managedForm);
		
		ScrolledForm form = managedForm.getForm();
	    form.setText("ラベルとテキストボックス");
	    form.getBody().setLayout(new GridLayout(2, false));
	        
	    FormToolkit toolkit = managedForm.getToolkit();
	    toolkit.createLabel(form.getBody(), "ラベル：");
	    toolkit.createText(form.getBody(), "テキストボックス");

	}
	
}
