package jp.addondev.debug.ui.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.texteditor.AbstractRulerActionDelegate;
import org.eclipse.ui.texteditor.ITextEditor;

public class JSManageBreakpointRulerActionDelegate extends
		AbstractRulerActionDelegate {

	private ToggleBreakpointRulerAction fTargetAction;
	
	@Override
	protected IAction createAction(ITextEditor editor,
			IVerticalRulerInfo rulerInfo) {
		// TODO Auto-generated method stub
		try {
			fTargetAction = new ToggleBreakpointRulerAction( editor, rulerInfo );
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fTargetAction;
	}

}
