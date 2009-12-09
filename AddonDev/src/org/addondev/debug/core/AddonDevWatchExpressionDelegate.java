package org.addondev.debug.core;

import org.addondev.debug.core.model.AddonStackFrame;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointsListener;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IWatchExpressionDelegate;
import org.eclipse.debug.core.model.IWatchExpressionListener;

public class AddonDevWatchExpressionDelegate implements
		IWatchExpressionDelegate {

	public AddonDevWatchExpressionDelegate() {
		super();
		// TODO Auto-generated constructor stub

	}

	@Override
	public void evaluateExpression(String expression, IDebugElement context,
			IWatchExpressionListener listener) {
		// TODO Auto-generated method stub
		if(context instanceof AddonStackFrame)
		{
			String data = expression;
			
		}
	}

}
