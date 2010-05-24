package gef.example.helloworld.viewers;

import gef.example.helloworld.model.AbstractElementModel;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class TabListPropertyDescriptor extends PropertyDescriptor {
	private Class fClass;
	private AbstractElementModel flistenermodel;
	
	public TabListPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
	}
	public TabListPropertyDescriptor(Object id, String displayName, AbstractElementModel listenermodel, Class _class) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
		fClass = _class;
		flistenermodel = listenermodel;
	}
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		TabListCellEditor editor = new TabListCellEditor(parent);
		editor.setClass(fClass);
		editor.setListenerModel(flistenermodel);
		editor.setID(getId().toString());
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}		
		return editor;
	}
	
}
