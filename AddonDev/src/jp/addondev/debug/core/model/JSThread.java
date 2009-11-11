package jp.addondev.debug.core.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.tasklist.ITaskListResourceAdapter;

public class JSThread extends PlatformObject implements IThread {

	private AddonDebugTarget target;
	private IBreakpoint[] fBreakpoints;
	private boolean fStepping = false;
	private IStackFrame[] stack;
	
	public JSThread(AddonDebugTarget target) {
		//super(target);
		// TODO Auto-generated constructor stub
		this.target = target;
	}

	@Override
	public IBreakpoint[] getBreakpoints() {
		// TODO Auto-generated method stub
		if (fBreakpoints == null) {
			//return new IBreakpoint[0];
			return new JSLineBreakpoint[0];
		}
		return fBreakpoints;
	}

	@Override
	public String getName() throws DebugException {
		// TODO Auto-generated method stub
		return "Thread[1]";
	}

	@Override
	public int getPriority() throws DebugException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IStackFrame[] getStackFrames() throws DebugException {
//		// TODO Auto-generated method stub
//		if (isSuspended()) {
//			return ((JSDebugTarget)getDebugTarget()).getStackFrames();
//		} else {
//			return new IStackFrame[0];
//		}
        if(isSuspended() && stack != null){
            return stack;
        }
        return new IStackFrame[0];
	}

	@Override
	public IStackFrame getTopStackFrame() throws DebugException {
//		// TODO Auto-generated method stub
//		IStackFrame[] frames = getStackFrames();
//		//JSStackFrame ff = new JSStackFrame(this, JSTestData.TESTDATA, 10, target );
//		//IStackFrame[] frames = new IStackFrame[]{ff};
//		if (frames.length > 0) {
//			return frames[0];
//		}
//		return null;
		 return stack == null ? null : stack[0];
	}

	@Override
	public boolean hasStackFrames() throws DebugException {
		// TODO Auto-generated method stub
		return isSuspended();
	}

	@Override
	public boolean canResume() {
		// TODO Auto-generated method stub
		return isSuspended();
	}

	@Override
	public boolean canSuspend() {
		// TODO Auto-generated method stub
		return !isSuspended();
	}

	@Override
	public boolean isSuspended() {
		// TODO Auto-generated method stub
		return getDebugTarget().isSuspended();
	}

	@Override
	public void resume() throws DebugException {
		// TODO Auto-generated method stub
		getDebugTarget().resume();
	}

	@Override
	public void suspend() throws DebugException {
		// TODO Auto-generated method stub
		getDebugTarget().suspend();
	}

	@Override
	public boolean canStepInto() {
		// TODO Auto-generated method stub
		return canResume();
	}

	@Override
	public boolean canStepOver() {
		// TODO Auto-generated method stub
		return isSuspended();
	}

	@Override
	public boolean canStepReturn() {
		// TODO Auto-generated method stub
		return canResume();
	}

	@Override
	public boolean isStepping() {
		// TODO Auto-generated method stub
		return fStepping;
	}

	@Override
	public void stepInto() throws DebugException {
		// TODO Auto-generated method stub
		((AddonDebugTarget)getDebugTarget()).stepInto();
	}

	@Override
	public void stepOver() throws DebugException {
		// TODO Auto-generated method stub
		((AddonDebugTarget)getDebugTarget()).stepOver();
	}

	@Override
	public void stepReturn() throws DebugException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canTerminate() {
		// TODO Auto-generated method stub
		return !isTerminated();
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return getDebugTarget().isTerminated();
	}

	@Override
	public void terminate() throws DebugException {
		// TODO Auto-generated method stub
		getDebugTarget().terminate();
	}

	/**
	 * Sets the breakpoints this thread is suspended at, or <code>null</code>
	 * if none.
	 * 
	 * @param breakpoints the breakpoints this thread is suspended at, or <code>null</code>
	 * if none
	 */
	protected void setBreakpoints(IBreakpoint[] breakpoints) {
		fBreakpoints = breakpoints;
	}
	
	protected void setStepping(boolean stepping) {
		fStepping = stepping;
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
	
    public Object getAdapter(Class adapter) {
        //AdapterDebug.print(this, adapter);
        
        if (adapter.equals(ILaunch.class) ||
            adapter.equals(IResource.class)){
            return target.getAdapter(adapter);
        }else if (adapter.equals(ITaskListResourceAdapter.class)){
            return null;
        }else if (adapter.equals(IPropertySource.class) 
                || adapter.equals(ITaskListResourceAdapter.class)
                || adapter.equals(org.eclipse.debug.ui.actions.IToggleBreakpointsTarget.class)
                || adapter.equals(org.eclipse.debug.ui.actions.IRunToLineTarget.class)
                || adapter.equals(org.eclipse.ui.IContributorResourceAdapter.class)
                || adapter.equals(org.eclipse.ui.model.IWorkbenchAdapter.class)
                || adapter.equals(org.eclipse.ui.IActionFilter.class)
                ) {
            return  super.getAdapter(adapter);
        }
        //Platform.getAdapterManager().getAdapter(this, adapter);
        //AdapterDebug.printDontKnow(this, adapter);
        // ongoing, I do not fully understand all the interfaces they'd like me to support
        return super.getAdapter(adapter);
    }
	
	/**
	 * Fires a debug event
	 * 
	 * @param event the event to be fired
	 */
	protected void fireEvent(DebugEvent event) {
		//DebugPlugin.getDefault().fireDebugEventSet(new DebugEvent[] {event});
        DebugPlugin manager= DebugPlugin.getDefault();
        if (manager != null) {
            manager.fireDebugEventSet(new DebugEvent[]{event});
        }
	}
	
	/**
	 * Fires a <code>CREATE</code> event for this element.
	 */
	public void fireCreationEvent() {
		fireEvent(new DebugEvent(this, DebugEvent.CREATE));
	}	
	
	/**
	 * Fires a <code>RESUME</code> event for this element with
	 * the given detail.
	 * 
	 * @param detail event detail code
	 */
	public void fireResumeEvent(int detail) {
		fireEvent(new DebugEvent(this, DebugEvent.RESUME, detail));
	}

	/**
	 * Fires a <code>SUSPEND</code> event for this element with
	 * the given detail.
	 * 
	 * @param detail event detail code
	 */
	public void fireSuspendEvent(int detail) {
		fireEvent(new DebugEvent(this, DebugEvent.SUSPEND, detail));
	}
	
	/**
	 * Fires a <code>TERMINATE</code> event for this element.
	 */
	protected void fireTerminateEvent() {
		fireEvent(new DebugEvent(this, DebugEvent.TERMINATE));
	}	

	public void SetStackFrames(IStackFrame[] stack)
	{
        this.stack = stack;
	}
	
//	public JSStackFrame getStackFrameByID(String ID) {
//		for(IStackFrame stackframe : stack)
//		{
//			JSStackFrame jsstackframe = (JSStackFrame)stackframe;
//			if(jsstackframe.getID() == ID )
//				return jsstackframe;
//		}
//		return null;
//	}
}
