package gef.example.helloworld.editor.overlay;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import gef.example.helloworld.model.AbstractElementModel;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.IPropertySheetEntryListener;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetPage;

public class PropertyDetailsPage implements IDetailsPage {

	private PropertySheetPage fPropertySheetPage;
	private PropertySheetEntry fPropertySheetEntry;
	private PropertyChangeListener fPropertyChangeListener;

	public void setPropertyChange(PropertyChangeListener listener) {
		this.fPropertyChangeListener = listener;
	}

	@Override
	public void createContents(Composite parent) {
		// TODO Auto-generated method stub
		parent.setLayout(new FillLayout());
        
        fPropertySheetPage = new PropertySheetPage();
        fPropertySheetPage.createControl(parent);
        fPropertySheetEntry = new PropertySheetEntry();
        fPropertySheetEntry.setPropertySourceProvider(new IPropertySourceProvider() {
			
			@Override
			public IPropertySource getPropertySource(Object object) {
				// TODO Auto-generated method stub
				if(object instanceof IPropertySource){
					IPropertySource src = (IPropertySource) object;
					return src;
				}
				return null;
			}
		});
		fPropertySheetPage.setRootEntry(fPropertySheetEntry);      
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

	private Object[] objs = new Object[1];
	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		// TODO Auto-generated method stub
		AbstractElementModel model = (AbstractElementModel) ((IStructuredSelection) selection).getFirstElement();
		model.removePropertyChangeListener(fPropertyChangeListener);
		model.addPropertyChangeListener(fPropertyChangeListener);
		objs[0] = model;
		fPropertySheetEntry.setValues(objs);
	}

}
