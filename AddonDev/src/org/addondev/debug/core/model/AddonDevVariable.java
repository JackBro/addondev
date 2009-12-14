package org.addondev.debug.core.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.tasklist.ITaskListResourceAdapter;

public class AddonDevVariable extends PlatformObject implements IVariable {
	
	private String fName;
	private String ftype;
	//private JSStackFrame fFrame;
	private AddonDebugTarget target;
	private AddonDevValue fvalue;
	
	/**
	 * Constructs a variable contained in the given stack frame
	 * with the given name.
	 * 
	 * @param frame owning stack frame
	 * @param name variable name
	 */
	public AddonDevVariable(AddonDebugTarget target, String stackframeid, String parent, String name, String type, String value, boolean hasChildren) {
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
        //AdapterDebug.print(this, adapter);
        
        if (adapter.equals(ILaunch.class))
            return target.getAdapter(adapter);
        else if (adapter.equals(IPropertySource.class) ||
                adapter.equals(ITaskListResourceAdapter.class) ||
                adapter.equals(org.eclipse.ui.IContributorResourceAdapter.class) ||
                adapter.equals(org.eclipse.ui.IActionFilter.class) ||
                adapter.equals(org.eclipse.ui.model.IWorkbenchAdapter.class)
                || adapter.equals(org.eclipse.debug.ui.actions.IToggleBreakpointsTarget.class)
                || adapter.equals(org.eclipse.debug.ui.actions.IRunToLineTarget.class)
                ||    adapter.equals(IResource.class)
                || adapter.equals(org.eclipse.core.resources.IFile.class)
                )
            return  super.getAdapter(adapter);
        // ongoing, I do not fully understand all the interfaces they'd like me to support
        // so I print them out as errors
//        if(adapter.equals(IDeferredWorkbenchAdapter.class)){
//            return new DeferredWorkbenchAdapter(this);
//        }
        
        //cannot check for the actual interface because it may not be available on eclipse 3.2 (it's only available
        //from 3.3 onwards... and this is only a hack for it to work with eclipse 3.4)
//        if(adapter.toString().endsWith("org.eclipse.debug.internal.ui.viewers.model.provisional.IElementContentProvider")){
//            return new PyVariableContentProviderHack();
//        }
        //AdapterDebug.printDontKnow(this, adapter);
        return super.getAdapter(adapter);
    }
}
