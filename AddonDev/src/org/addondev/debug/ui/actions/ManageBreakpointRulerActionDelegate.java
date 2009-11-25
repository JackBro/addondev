package org.addondev.debug.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.texteditor.AbstractRulerActionDelegate;
import org.eclipse.ui.texteditor.ITextEditor;

public class ManageBreakpointRulerActionDelegate extends
		AbstractRulerActionDelegate {

	public ManageBreakpointRulerActionDelegate() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IAction createAction(ITextEditor editor,
			IVerticalRulerInfo rulerInfo) {
		// TODO Auto-generated method stub
		//return null;
		return new BreakpointRulerAction(editor, rulerInfo);
	}

}
