package gef.example.helloworld.viewers;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class MultiTextCellEditor extends DialogCellEditor {

	public MultiTextCellEditor(Composite parent) {
		// TODO Auto-generated constructor stub
		super(parent, SWT.NONE);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		// TODO Auto-generated method stub
        MultiTextDialog dialog = new MultiTextDialog(cellEditorWindow.getShell());
        Object value = getValue();
        dialog.setValue((String) value);
        int ret = dialog.open();
        if (ret == IDialogConstants.OK_ID) {
        	return dialog.getValue();
        }
		return null;
	}

}
