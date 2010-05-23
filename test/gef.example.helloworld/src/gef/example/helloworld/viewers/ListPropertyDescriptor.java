package gef.example.helloworld.viewers;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ListPropertyDescriptor extends PropertyDescriptor {

	private Class fClass;
	
	private ListPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
	}

	public ListPropertyDescriptor(Object id, String displayName, Class _class) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
		fClass = _class;
	}
	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		// TODO Auto-generated method stub
		//return super.createPropertyEditor(parent);
		ListCellEditor editor = new ListCellEditor(parent, fClass);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}		
		 return editor;
	}

}
