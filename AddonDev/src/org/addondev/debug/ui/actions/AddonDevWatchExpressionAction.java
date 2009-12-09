package org.addondev.debug.ui.actions;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IWatchExpression;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public class AddonDevWatchExpressionAction implements IEditorActionDelegate {

	private ITextSelection fSelection;

	public AddonDevWatchExpressionAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
        if (fSelection == null) {
            return;
        }
        String text = fSelection.getText();
        IWatchExpression expression = DebugPlugin.getDefault().getExpressionManager().newWatchExpression(text);
        DebugPlugin.getDefault().getExpressionManager().addExpression(expression);
        
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
        fSelection = null;        
        if (selection instanceof ITextSelection ) {
            fSelection = (ITextSelection) selection;
        }		
	}

}
