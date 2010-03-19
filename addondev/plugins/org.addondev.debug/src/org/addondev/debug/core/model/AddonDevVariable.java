package org.addondev.debug.core.model;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

public class AddonDevVariable extends PlatformObject implements IVariable {
	
	private String fName;
	private String ftype;
	//private JSStackFrame fFrame;
	//private AddonDevDebugTarget target;
	private IDebugTarget target;
	private AddonDevValue fvalue;
	
	/**
	 * Constructs a variable contained in the given stack frame
	 * with the given name.
	 * 
	 * @param frame owning stack frame
	 * @param name variable name
	 */
	//public AddonDevVariable(AddonDevDebugTarget target, String stackframeid, String parent, String name, String type, String value, boolean hasChildren) {
	public AddonDevVariable(IDebugTarget target, String stackframeid, String parent, String name, String type, String value, boolean hasChildren) {
		//super((JSDebugTarget) frame.getDebugTarget());
		this.target = target;
		//fFrame = frame;
		fName = name;
		ftype = type;
		fvalue = new AddonDevValue(target, stackframeid, parent, name, type, value, hasChildren);
	}

	@Override
	public String getName() throws DebugException {
		// TODO Auto-generated method stub
		return fName;
	}
	
//	/**
//	 * Returns the stack frame owning this variable.
//	 * 
//	 * @return the stack frame owning this variable
//	 */
//	protected JSStackFrame getStackFrame() {
//		return fFrame;
//	}

	@Override
	public String getReferenceTypeName() throws DebugException {
		// TODO Auto-generated method stub
		//return null;
		return "Thing";
	}

	@Override
	public IValue getValue() throws DebugException {
		// TODO Auto-generated method stub
		//return ((JSDebugTarget)getDebugTarget()).getVariableValue(this);
		return fvalue;
	}

	@Override
	public boolean hasValueChanged() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValue(String expression) throws DebugException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(IValue value) throws DebugException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean supportsValueModification() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyValue(String expression) throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyValue(IValue value) throws DebugException {
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

	//public String 
	
    public Object getAdapter(Class adapter) {        
        if (adapter.equals(ILaunch.class))
            return target.getAdapter(adapter);
 
        return super.getAdapter(adapter);
    }
}
