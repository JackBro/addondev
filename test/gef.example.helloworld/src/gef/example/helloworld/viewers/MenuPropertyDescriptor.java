package gef.example.helloworld.viewers;

import gef.example.helloworld.model.AbstractElementModel;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class MenuPropertyDescriptor extends PropertyDescriptor {

	private AbstractElementModel flistenermodel;
	
	public MenuPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
	}

	public MenuPropertyDescriptor(Object id, String displayName, AbstractElementModel listenermodel){
		super(id, displayName);
		// TODO Auto-generated constructor stub
		flistenermodel = listenermodel;		
	}	
	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		// TODO Auto-generated method stub
		MenuCellEditor editor = new MenuCellEditor(parent);
		editor.setListenerModel(flistenermodel);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}		
		 return editor;
	}

}
