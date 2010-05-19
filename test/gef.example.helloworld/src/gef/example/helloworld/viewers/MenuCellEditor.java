package gef.example.helloworld.viewers;

import gef.example.helloworld.model.ElementModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class MenuCellEditor extends DialogCellEditor {

    public MenuCellEditor(Composite parent) {
        this(parent, SWT.NONE);
    }
    
    public MenuCellEditor(Composite parent, int style) {
        super(parent, style);
    }
	
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
        MenuDialog dialog = new MenuDialog(cellEditorWindow.getShell());
        Object value = getValue();	
        List newvalue = new ArrayList();
        if(value != null){
        	List children = (List)value;
        	for (Object elem : children) {
        		newvalue.add(((ElementModel)elem).getCopy());
			}
        	dialog.setValue(newvalue);
        }
        int ret = dialog.open();
        if (ret == IDialogConstants.OK_ID) {
        	List org = (List)value;
        	List newlist = dialog.getValue();
        	org.clear();
        	org.addAll(newlist);
        	return org;
        }
        return null;
	}

}
