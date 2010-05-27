package gef.example.helloworld.viewers;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class MultiTextPropertyDescriptor extends PropertyDescriptor {

	public MultiTextPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		// TODO Auto-generated method stub
//		return super.createPropertyEditor(parent);
		MultiTextCellEditor editor = new MultiTextCellEditor(parent);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}		
		 return editor;
	}

}
