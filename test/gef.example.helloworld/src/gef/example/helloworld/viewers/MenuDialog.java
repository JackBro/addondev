package gef.example.helloworld.viewers;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.PropertySheetPage;

public class MenuDialog extends Dialog {

	private TableViewer viewer;
	private PropertySheetPage fPropertySheetPage;
	
	protected MenuDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		//return super.createDialogArea(parent);
		Composite composite = (Composite)super.createDialogArea(parent);
		SashForm baseSash = new SashForm(composite, SWT.HORIZONTAL);
		
		viewer = new TableViewer(baseSash, SWT.FULL_SELECTION);
		
		fPropertySheetPage = new PropertySheetPage();
		fPropertySheetPage.createControl(baseSash);
		
		//baseSash.setWeights(new int [] {20,80})
		
		return composite;
	}

}
