package gef.example.helloworld.editor.overlay;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class PropertyDetailsPage implements IDetailsPage {

	private IManagedForm form;
	
	public PropertyDetailsPage(IManagedForm form) {
		super();
		this.form = form;
	}

	@Override
	public void createContents(Composite parent) {
		// TODO Auto-generated method stub
		parent.setLayout(new FillLayout());
		FormToolkit toolkit =  form.getToolkit();
		
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION);
		section.setText("test"); 
		
		Composite client = toolkit.createComposite(section);
		client.setLayout(new FillLayout());
		
		
	}

	@Override
	public void commit(boolean onSave) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(IManagedForm form) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStale() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean setFormInput(Object input) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
