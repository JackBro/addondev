package gef.example.helloworld.viewers;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ListCellEditor extends DialogCellEditor {

	
    public ListCellEditor(Composite parent) {
        this(parent, SWT.NONE);
    }
    
    public ListCellEditor(Composite parent, int style) {
        super(parent, style);
        //doSetValue(new RGB(0, 0, 0));
    }
    
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		// TODO Auto-generated method stub
        ListDialog dialog = new ListDialog(cellEditorWindow.getShell());
        Object value = getValue(); 
        if (value != null && (value instanceof ListProperty)) {
        	ListProperty property = ((ListProperty)value).cp();
        	///dialog.setValue(property.getClass(), property.getValues());
        	dialog.setValue(property);
		}
        int ret = dialog.open();
        if (ret == IDialogConstants.OK_ID) {
        	Object oo = dialog.getValue();
        	return dialog.getValue();
        }
        else{
        	return null;
        }
	}

	@Override
	protected void updateContents(Object value) {
		// TODO Auto-generated method stub
		super.updateContents(value);
	}
}
