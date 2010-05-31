package gef.example.helloworld.viewers;

import java.util.ArrayList;
import java.util.List;

import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.ListBoxModel;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
		DialogCellEditor editor = new DialogCellEditor(parent) {
			
			@Override
			protected Object openDialogBox(Control cellEditorWindow) {
				ListBoxItemDialog dialog = new ListBoxItemDialog(cellEditorWindow.getShell());
				dialog.setClass(fClass);
				
				int columnum = ((ListBoxModel)flistenermodel).getListcols().getChildren().size();
				
				dialog.setNumColum(columnum);
		        Object value = getValue();
		        if(value != null ){
		        	List orgvalue = (List)value;
		        	List newvalue = new ArrayList();
		        	for (Object object : orgvalue) {
		        		newvalue.add(((AbstractElementModel)object).clone());
					}
		    		dialog.setValue(newvalue);
		        }
		        
		        int ret = dialog.open();
		        if (ret == IDialogConstants.OK_ID) {
		        	List orgvalue = (List)value;
		        	orgvalue.clear();
		        	orgvalue.addAll(dialog.getValue());
		        	return orgvalue;
		        }
				return null;
			}
		};
		
		
//		ListBoxItemDialogCellEditor editor = new ListBoxItemDialogCellEditor(parent);
//		editor.setClass(fClass);
//		editor.setListenerModel(flistenermodel);
//		editor.setID(getId().toString());
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}		
		return editor;
	}


}
