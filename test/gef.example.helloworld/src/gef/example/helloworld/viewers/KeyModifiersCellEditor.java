package gef.example.helloworld.viewers;

import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class KeyModifiersCellEditor extends CellEditor {

	private Button alt, control, meta, shift, accel;
	
	public KeyModifiersCellEditor(Composite parent) {
		super(parent, SWT.NONE);
	}
	
	@Override
	protected Control createControl(Composite parent) {
		// TODO Auto-generated method stub
		PopupDialog pd = new PopupDialog(parent.getShell(), SWT.NONE, true, true, true, true, true, "titleText", "infoText");
		pd.open();
//		Composite composite = new Composite(parent, SWT.NONE);
//		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		composite.setLayout(new GridLayout(3, false));
//		alt = new Button(composite, SWT.CHECK);
//		alt.setText("alt");
//		control = new Button(composite, SWT.CHECK);
//		control.setText("control");
//		meta = new Button(composite, SWT.CHECK);
//		meta.setText("meta");
//		shift = new Button(composite, SWT.CHECK);
//		shift.setText("shift");
//		accel = new Button(composite, SWT.CHECK);
//		accel.setText("accel");
		
		return parent;
	}

	@Override
	protected Object doGetValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doSetFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doSetValue(Object value) {
		// TODO Auto-generated method stub

	}

}
