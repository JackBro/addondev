package org.addondev.ui.editor.javascript.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public class JsComment extends JsAction {

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		
		Button button = new Button(targetEditor.getSite().getShell(), SWT.NORMAL);
		button.setText("OK");
	}

}
