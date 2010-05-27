package gef.example.helloworld.viewers;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.text.edits.MultiTextEdit;

public class MultiTextDialog extends Dialog {
	
	private Text text;
	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;//text.getText();
	}
	
	protected MultiTextDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getShellStyle() {
		return super.getShellStyle()|SWT.RESIZE|SWT.MAX;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		//return super.createDialogArea(parent);
		Composite composite = (Composite)super.createDialogArea(parent);
		text = new Text(composite, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.setText(value);
		
		return composite;
	}

	@Override
	protected void okPressed() {
		// TODO Auto-generated method stub
		value = text.getText();
		super.okPressed();
	}

}
