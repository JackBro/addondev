package gef.example.helloworld.viewers;

import gef.example.helloworld.model.AbstractElementModel;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ListBoxItemPropertyDescriptor extends ListPropertyDescriptor {

	public ListBoxItemPropertyDescriptor(Object id, String displayName,
			AbstractElementModel listenermodel, Class class1) {
		super(id, displayName, listenermodel, class1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		// TODO Auto-generated method stub
		ListBoxItemDialogCellEditor editor = new ListBoxItemDialogCellEditor(parent);
		editor.setClass(fClass);
		editor.setListenerModel(flistenermodel);
		editor.setID(getId().toString());
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}		
		 return editor;
	}


}
