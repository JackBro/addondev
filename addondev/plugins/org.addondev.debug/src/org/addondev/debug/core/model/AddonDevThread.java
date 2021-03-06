package org.addondev.debug.core.model;

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

public class AddonDevThread extends PlatformObject implements IThread {

	private AddonDevDebugTarget target;
	private IBreakpoint[] fBreakpoints;
	private boolean fStepping = false;
	private IStackFrame[] stack;
	private boolean isSuspended;
	
	public AddonDevThread(AddonDevDebugTarget target) {
		//super(target);
		// TODO Auto-generated constructor stub
		this.target = target;
		isSuspended = true;
	}

	@Override
	public IBreakpoint[] getBreakpoints() {
		// TODO Auto-generated method stub
		if (fBreakpoints == null) {
			//return new IBreakpoint[0];
			return new AddonDevBreakpoint[0];
		}
		return fBreakpoints;
	}

	@Override
	public String getName() throws DebugException {
		// TODO Auto-generated method stub
		return "Thread";
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
		if (stack == null || stack.length == 0) {
			return null;
		}
		
		 return stack[0];
	}

	@Override
	public boolean hasStackFrames() throws DebugException {
		// TODO Auto-generated method stub
		//return isSuspended();
		if(stack == null)
			return false;
		
		return stack.length > 0;
	}

	@Override
	public synchronized boolean canResume() {
		// TODO Auto-generated method stub
		return target.canResume();
		//return isSuspended();
	}

	@Override
	public synchronized boolean canSuspend() {
		// TODO Auto-generated method stub
		return !isSuspended();
	}

	@Override
	public synchronized boolean isSuspended() {
		// TODO Auto-generated method stub
		//return getDebugTarget().isSuspended();
		return isSuspended;
	}

	@Override
	public synchronized void resume() throws DebugException {
		// TODO Auto-generated method stub
		if (!isSuspended ()) {
			return;	
		}
		
		stack = null;
		isSuspended = false;
		
//		DebugEvent ev = new DebugEvent (this, DebugEvent.RESUME, DebugEvent.STEP_OVER);
//		DebugPlugin.getDefault ().fireDebugEventSet (new DebugEvent[] { ev });
		
		fireResumeEvent(DebugEvent.BREAKPOINT);
		//fireSuspendEvent(DebugEvent.BREAKPOINT);
		
	  	//fireResumeEvent(DebugEvent.STEP_OVER);
		getDebugTarget().resume();
	}

	@Override
	public synchronized void suspend() throws DebugException {
		// TODO Auto-generated method stub
		isSuspended = true;
		
		DebugEvent ev = new DebugEvent (this, DebugEvent.SUSPEND, DebugEvent.BREAKPOINT);
		DebugPlugin.getDefault ().fireDebugEventSet (new DebugEvent[] { ev });
		
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
		//((AddonDebugTarget)getDebugTarget()).stepInto();
		fireResumeEvent(DebugEvent.STEP_INTO);
		target.stepInto();
		//stack[0].stepInto();
		
	}

	@Override
	public void stepOver() throws DebugException {
		// TODO Auto-generated method stub
		//((AddonDebugTarget)getDebugTarget()).stepOver();
		//DebugEvent ev = new DebugEvent (this, DebugEvent.RESUME, DebugEvent.STEP_OVER);
		//DebugPlugin.getDefault ().fireDebugEventSet (new DebugEvent[] { ev });
		this.stack = null; 
		fireResumeEvent(DebugEvent.STEP_OVER);
		target.stepOver();
		//stack[0].stepOver();
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
	public synchronized void terminate() throws DebugException {
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
        
        if (adapter.equals(ILaunch.class) ||
            adapter.equals(IResource.class)){
            return target.getAdapter(adapter);
        }
        return super.getAdapter(adapter);
    }

	public void setStackFrames(IStackFrame[] stack)
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
	
	
	protected void fireEvent(DebugEvent event) {
		DebugPlugin.getDefault().fireDebugEventSet(new DebugEvent[] { event });
	}
	
	public void fireCreationEvent() {
		fireEvent(new DebugEvent(this, DebugEvent.CREATE));
	}
	
	public void fireResumeEvent(int detail) {
		fireEvent(new DebugEvent(this, DebugEvent.RESUME, detail));
	}
	
	public void fireSuspendEvent(int detail) {
		fireEvent(new DebugEvent(this, DebugEvent.SUSPEND, detail));
	}
	
	protected void fireTerminateEvent() {
		fireEvent(new DebugEvent(this, DebugEvent.TERMINATE));
	}
}
