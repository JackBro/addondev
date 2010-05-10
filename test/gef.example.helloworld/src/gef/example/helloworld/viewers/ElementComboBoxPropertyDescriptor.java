package gef.example.helloworld.viewers;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ElementComboBoxPropertyDescriptor extends PropertyDescriptor {

	private String[] items;

	private ElementComboBoxPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}
	
	public ElementComboBoxPropertyDescriptor(Object id, String displayName, String[] items) {
		super(id, displayName);
		this.items = items;
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new ComboBoxCellEditor(parent, items, SWT.READ_ONLY){

			@Override
			protected void doSetValue(Object value) {
				int index = getItemIndex((String) value);
				super.doSetValue(new Integer(index));
			}

			@Override
			protected Object doGetValue() {
				int selection = ((Integer) super.doGetValue()).intValue();
				if(selection == -1) selection = 0;
				return items[selection];
			}
			
		};
		
		return editor;
	}
	
	private int getItemIndex(String value){
		for (int i = 0; i < items.length; i++) {
			if(items[i].equals(value))
				return i;
		}
		return 0;
	}

}
