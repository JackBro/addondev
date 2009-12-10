package org.addondev.debug.core;

import java.util.ArrayList;

import org.addondev.debug.core.model.AddonDebugTarget;
import org.addondev.debug.core.model.AddonStackFrame;
import org.addondev.debug.core.model.AddonVariable;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointsListener;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.debug.core.model.IWatchExpressionDelegate;
import org.eclipse.debug.core.model.IWatchExpressionListener;
import org.eclipse.debug.core.model.IWatchExpressionResult;

public class AddonDevWatchExpressionDelegate implements
		IWatchExpressionDelegate, IWatchExpressionResult {

	private IVariable[] variables;
	private IVariable fVariable;
	
	
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
			AddonStackFrame stackframe = (AddonStackFrame)context;
			AddonDebugTarget debugtarget = (AddonDebugTarget) stackframe.getDebugTarget();
			String parent = null;
			String name = null;
			if(expression.contains("."))
			{
				String[] paths = expression.split("\\.");
				parent = paths[0];
				name = paths[1];
			}
			else
			{
				name = expression;
			}
			
			fVariable = debugtarget.getVariable("0" , parent, name);
			listener.watchEvaluationFinished(this);
		}
	}

	@Override
	public String[] getErrorMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DebugException getException() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExpressionText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue getValue() {
		// TODO Auto-generated method stub
		IValue value = null;
		try {
			value = fVariable.getValue();
		} catch (DebugException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public boolean hasErrors() {
		// TODO Auto-generated method stub
		return false;
	}

}
