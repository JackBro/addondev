package org.addondev.editor.javascript.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public abstract class JsAction implements IEditorActionDelegate {

	//protected volatile IEditorPart targetEditor;
	protected IEditorPart targetEditor;
	
	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// TODO Auto-generated method stub
		this.targetEditor = targetEditor;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		action.setEnabled(true);
	}

}
