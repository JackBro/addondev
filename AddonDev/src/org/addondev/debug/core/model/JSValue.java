package org.addondev.debug.core.model;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

public class JSValue extends PlatformObject implements IValue {

	private AddonDebugTarget target;
	
	private String stackFrameID;
	
	private String		fType; 							// The type of this value (see the PEVT_... values)
	private String 		fValue; 							// The value of this variable as text
	private String name;
	private boolean hasChildren;
	private static final IVariable[] EMPTY_IVARIABLE_ARRAY = new IVariable[0]; 
	public String parent;
	
	public JSValue(AddonDebugTarget target, String stackFrameID, String parent, String name, String type, String value, boolean hasChildren) {
		this.target = target;
		this.stackFrameID = stackFrameID;
		
		this.parent = parent;
		this.name = name;
		this.fType = type;	
		this.fValue = value;
		this.hasChildren = hasChildren;

	}
	
	@Override
	public String getReferenceTypeName() throws DebugException {
		// TODO Auto-generated method stub
		return fType;
	}

	@Override
	public String getValueString() throws DebugException {
		// TODO Auto-generated method stub
		return fValue;
	}

	@Override
	public IVariable[] getVariables() throws DebugException {
		// TODO Auto-generated method stub
		//return EMPTY_IVARIABLE_ARRAY;
		//return target.getVariables(fValue);
		if(hasChildren)
		{
			//return target.getVariables(stackFrameID, parent, name);			
			return target.getChildVariables(stackFrameID, parent, name);
		}
		else
			return EMPTY_IVARIABLE_ARRAY;
	}

	@Override
	public boolean hasVariables() throws DebugException {
		// TODO Auto-generated method stub
		return hasChildren;
	}

	@Override
	public boolean isAllocated() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IDebugTarget getDebugTarget() {
		// TODO Auto-generated method stub
		return target;
	}

	@Override
	public ILaunch getLaunch() {
		// TODO Auto-generated method stub
		return target.getLaunch();
	}

	@Override
	public String getModelIdentifier() {
		// TODO Auto-generated method stub
		return target.getModelIdentifier();
	}
}
