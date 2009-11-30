package org.addondev.debug.core.model;

import java.io.IOException;

import org.addondev.debug.net.SendRequest;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.tasklist.ITaskListResourceAdapter;

public class JSStackFrame extends PlatformObject implements IStackFrame {

	private JSThread fThread;
	private String fName;
	private int fPC;
	private String fURL;
	private String fFileName;
	private IPath path;
	private int line;
	private String fFn;
	private String fdepth;
	private IVariable[] fVariables;
	private AddonDebugTarget target;
	
	public JSStackFrame(JSThread thread, 
			AddonDebugTarget target, 
			String depth, 
			String url, 
			String filename, 
			String functionname, 
			String line,
			String fn) {
		// TODO Auto-generated constructor stub
		this.target = target;
		fThread = thread;
		fdepth = depth;
		fURL = url;
		fFileName = filename;
		fName = functionname;
		fPC = Integer.parseInt(line);
		fFn = fn;
		fVariables = null;
		
		path = new Path(filename);
		this.line = Integer.parseInt(line);
	}
	
	public String getURL()
	{
		return fURL;
	}
	
	public String getFileFullPath()
	{
		return fFileName;
	}
	
	public String getFn()
	{
		return fFn;
	}
	
	@Override
	public int getCharEnd() throws DebugException {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int getCharStart() throws DebugException {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int getLineNumber() {
		// TODO Auto-generated method stub
		return fPC;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		//return fName;
		return fName + " [" + path.lastSegment() + ":" + Integer.toString(line) + "]";
	}

	@Override
	public IRegisterGroup[] getRegisterGroups() throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IThread getThread() {
		// TODO Auto-generated method stub
		return fThread;
	}

	@Override
	public IVariable[] getVariables() throws DebugException {
		// TODO Auto-generated method stub
		if(this.fVariables == null){
			fVariables = target.getVariables(fdepth, null, null);
		}
		return fVariables;
	}

	@Override
	public boolean hasRegisterGroups() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasVariables() throws DebugException {
		// TODO Auto-generated method stub
		return fVariables.length > 0;
	}

	@Override
	public boolean canStepInto() {
		// TODO Auto-generated method stub
		return getThread().canStepInto();
	}

	@Override
	public boolean canStepOver() {
		// TODO Auto-generated method stub
		return getThread().canStepOver();
	}

	@Override
	public boolean canStepReturn() {
		// TODO Auto-generated method stub
		return getThread().canStepReturn();
	}

	@Override
	public boolean isStepping() {
		// TODO Auto-generated method stub
		return getThread().isStepping();
	}

	@Override
	public void stepInto() throws DebugException {
		// TODO Auto-generated method stub
		//getThread().stepInto();
		target.resumed(DebugEvent.STEP_INTO);
		try {
			SendRequest.stepInto();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stepOver() throws DebugException {
		// TODO Auto-generated method stub
		//getThread().stepOver();
		target.resumed(DebugEvent.STEP_OVER);
		try {
			SendRequest.stepOver();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void stepReturn() throws DebugException {
		// TODO Auto-generated method stub
		getThread().stepReturn();
	}

	@Override
	public boolean canResume() {
		// TODO Auto-generated method stub
		return getThread().canResume();
	}

	@Override
	public boolean canSuspend() {
		// TODO Auto-generated method stub
		return getThread().canSuspend();
	}

	@Override
	public boolean isSuspended() {
		// TODO Auto-generated method stub
		return getThread().isSuspended();
	}

	@Override
	public void resume() throws DebugException {
		// TODO Auto-generated method stub
		getThread().resume();
	}

	@Override
	public void suspend() throws DebugException {
		// TODO Auto-generated method stub
		getThread().suspend();
	}

	@Override
	public boolean canTerminate() {
		// TODO Auto-generated method stub
		return getThread().canTerminate();
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return getThread().isTerminated();
	}

	@Override
	public void terminate() throws DebugException {
		// TODO Auto-generated method stub
		getThread().terminate();
	}

	public String getSourceName() {
		return fFileName;
	}

	@Override
	public IDebugTarget getDebugTarget() {
		// TODO Auto-generated method stub
		return fThread.getDebugTarget();
	}

	@Override
	public ILaunch getLaunch() {
		// TODO Auto-generated method stub
		 return fThread.getLaunch();
	}

	@Override
	public String getModelIdentifier() {
		// TODO Auto-generated method stub
		return fThread.getModelIdentifier();
	}
	
    public void setVariables(IVariable[] locals) {
        this.fVariables = locals;
    }
    	
    public Object getAdapter(Class adapter) {
        //AdapterDebug.print(this, adapter);

        if (adapter.equals(ILaunch.class) ||
            adapter.equals(IResource.class)){
            return fThread.getAdapter(adapter);
        }    
        
        if (adapter.equals(ITaskListResourceAdapter.class)){
            return null;
        }
        
        if (adapter.equals(IPropertySource.class) 
            || adapter.equals(ITaskListResourceAdapter.class)
            || adapter.equals(org.eclipse.debug.ui.actions.IToggleBreakpointsTarget.class)
            || adapter.equals(org.eclipse.debug.ui.actions.IRunToLineTarget.class)
            ){
            return  super.getAdapter(adapter);
        }
        
//        if (adapter.equals(IDeferredWorkbenchAdapter.class)){
//            return new DeferredWorkbenchAdapter(this);
//        }
        
        //AdapterDebug.printDontKnow(this, adapter);
        // ongoing, I do not fully understand all the interfaces they'd like me to support
        return super.getAdapter(adapter);
    }
	
    public String getID()
    {
    	return fdepth;
    }
}
