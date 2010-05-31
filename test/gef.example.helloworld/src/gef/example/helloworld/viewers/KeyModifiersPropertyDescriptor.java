package gef.example.helloworld.viewers;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class KeyModifiersPropertyDescriptor extends PropertyDescriptor {

	public KeyModifiersPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		// TODO Auto-generated method stub
		//return super.createPropertyEditor(parent);
		DialogCellEditor editor = new DialogCellEditor(parent) {
			
			@Override
			protected Object openDialogBox(Control cellEditorWindow) {
				// TODO Auto-generated method stub
				KeyModifiersDialog dialog = new KeyModifiersDialog(cellEditorWindow.getShell());
				int ret = dialog.open();
				return null;
			}
		};
		//KeyModifiersCellEditor editor = new KeyModifiersCellEditor(parent);
		return editor;
	}

}
