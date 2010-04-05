package org.addondev.ui.editor.javascript.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

public class JsComment extends AbstractHandler {

//	@Override
//	public void run(IAction action) {
//		// TODO Auto-generated method stub
//
//		
//		Button button = new Button(targetEditor.getSite().getShell(), SWT.NORMAL);
//		button.setText("OK");
//	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IEditorPart editor = window.getActivePage().getActiveEditor();
		return null;
	}
	
	private class CategoryFilterSelectionDialog extends SelectionStatusDialog{

		public CategoryFilterSelectionDialog(Shell parent) {
			super(parent);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			// TODO Auto-generated method stub
			
			return super.createDialogArea(parent);
		}

		@Override
		protected void computeResult() {
			// TODO Auto-generated method stub
			
		}
		
	}
}

