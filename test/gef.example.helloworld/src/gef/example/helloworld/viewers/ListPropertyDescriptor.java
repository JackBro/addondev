package gef.example.helloworld.viewers;

import gef.example.helloworld.model.AbstractElementModel;


import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ListPropertyDescriptor extends PropertyDescriptor {

	private Class fClass;
	private AbstractElementModel flistenermodel;
	
	private ListPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
	}

	public ListPropertyDescriptor(Object id, String displayName, Class _class) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
		fClass = _class;
		setValidator(new ListValidator());
		//setLabelProvider(new ListLabelProvider());
	}
	
	public ListPropertyDescriptor(Object id, String displayName, AbstractElementModel listenermodel, Class _class){
		super(id, displayName);
		// TODO Auto-generated constructor stub
		fClass = _class;
		flistenermodel = listenermodel;	
		setValidator(new ListValidator());
		//setLabelProvider(new ListLabelProvider());
	}
	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		// TODO Auto-generated method stub
		//return super.createPropertyEditor(parent);
		ListCellEditor editor = new ListCellEditor(parent, fClass);
		editor.setClass(fClass);
		editor.setListenerModel(flistenermodel);
		editor.setID(getId().toString());
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}		
		 return editor;
	}
	
	class ListValidator implements ICellEditorValidator{

		@Override
		public String isValid(Object value) {
			// TODO Auto-generated method stub
			//if()
			return null;
		}
	}

	class ListLabelProvider extends LabelProvider{
		
	}
}
