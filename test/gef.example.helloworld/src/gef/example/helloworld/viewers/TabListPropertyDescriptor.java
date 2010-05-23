package gef.example.helloworld.viewers;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class TabListPropertyDescriptor extends PropertyDescriptor {
	private Class fClass;
	
	public TabListPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
	}
	public TabListPropertyDescriptor(Object id, String displayName, Class _class) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
		fClass = _class;
	}
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		TabListCellEditor editor = new TabListCellEditor(parent, fClass);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}		
		return editor;
	}
	
}
