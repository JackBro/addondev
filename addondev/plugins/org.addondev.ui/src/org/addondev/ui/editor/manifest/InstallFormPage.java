package org.addondev.ui.editor.manifest;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class InstallFormPage extends FormPage {

	private InstallMasterDetailsBlock black;
	
	public InstallFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		// TODO Auto-generated constructor stub
		black = new InstallMasterDetailsBlock(editor.getEditorInput());
	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		// TODO Auto-generated method stub
		super.createFormContent(managedForm);
		black.createContent(managedForm);
	
//		ScrolledForm form = managedForm.getForm();
//	    form.setText("ラベルとテキストボックス");
//		Composite body = form.getBody();
//		body.setLayout(new GridLayout(2, false));
//	        
//	    FormToolkit toolkit = managedForm.getToolkit();
//	    toolkit.createLabel(body, "ID：");
//	    Text id = toolkit.createText(body, "テキストボックス");
//	    
//	    toolkit.createLabel(body, "ID：");
//	    Text name = toolkit.createText(body, "テキストボックス");
//	    
//	    toolkit.createLabel(body, "ID：");
//	    Text version = toolkit.createText(body, "テキストボックス");
//	    
//	    toolkit.createLabel(body, "ID：");
//	    Text description = toolkit.createText(body, "テキストボックス");
//	    
//	    toolkit.createLabel(body, "ID：");
//	    Text description = toolkit.createText(body, "テキストボックス");
//
//	    toolkit.createLabel(body, "ID：");
//	    Combo combo = new Combo(body, SWT.READ_ONLY);
//	    combo.add("Eclipse");
//	    combo.add("NetBeans");
	}
}
