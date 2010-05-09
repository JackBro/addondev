package gef.example.helloworld.viewers;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ListPropertyDescriptor extends PropertyDescriptor {

	public ListPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		// TODO Auto-generated method stub
		//return super.createPropertyEditor(parent);
		ListCellEditor editor = new ListCellEditor(parent);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}		
		 return editor;
	}

}
