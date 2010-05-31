package gef.example.helloworld.viewers;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class KeyModifiersDialog extends Dialog {

	private Button alt, control, meta, shift, accel;
	
	protected KeyModifiersDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		//return super.createDialogArea(parent);
		Composite composite = (Composite)super.createDialogArea(parent);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(3, false));
		alt = new Button(composite, SWT.CHECK);
		alt.setText("alt");
		control = new Button(composite, SWT.CHECK);
		control.setText("control");
		meta = new Button(composite, SWT.CHECK);
		meta.setText("meta");
		shift = new Button(composite, SWT.CHECK);
		shift.setText("shift");
		accel = new Button(composite, SWT.CHECK);
		accel.setText("accel");
		
		return composite;
	}

}
