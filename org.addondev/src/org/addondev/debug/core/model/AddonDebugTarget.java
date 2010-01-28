package org.addondev.debug.core.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


import org.addondev.debug.net.SendRequest;
import org.addondev.debug.net.SimpleServer;
import org.addondev.debug.ui.model.AddonDevDebugModelPresentation;
import org.addondev.plugin.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.addondev.util.AddonDevUtil;
import org.addondev.util.XMLUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchListener;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.tasklist.ITaskListResourceAdapter;
import org.xml.sax.SAXException;


public class AddonDebugTarget extends PlatformObject implements IDebugTarget, ILaunchListener, IDebugEventSetListener {

	private IProcess fProcess;
	
	// containing launch object
	private ILaunch fLaunch;
	
	// program name
	private String fName;
	
	// suspend state
	private boolean fSuspended = true;
	
	// terminated state
	private boolean fTerminated = false;
	
	private boolean fCloseBrowser = true;
	
	// threads
	private AddonDevThread fThread;
	private IThread[] fThreads;
	
	private AddonDevUtil fAddonDevUtil;
	//private List<IProject> fDebugProjects;
	
	private boolean fstateChange = false; 
	

	private ArrayList<IBreakpoint> fBreakpointList = new ArrayList<IBreakpoint>();
	private ArrayList<IBreakpoint> fRemoveBreakpointList = new ArrayList<IBreakpoint>();
	
	
	
	public boolean isCloseBrowser() {
		return fCloseBrowser;
	}

	public void setCloseBrowser(boolean closeBrowser) {
		this.fCloseBrowser = closeBrowser;
	}

	public AddonDebugTarget(ILaunchConfiguration configuration, ILaunch launch) throws Exception {
		// TODO Auto-generated constructor stub
		fLaunch = launch;		
		//fAddonDevUtil = addondevutil;
		fAddonDevUtil = new AddonDevUtil(configuration);
		
		DebugPlugin.getDefault().addDebugEventListener(this);
		//startDebug();
		//IPath pp = ((JSLineBreakpoint)breakpoint).getPath().makeAbsolute();
		//String pp2 = ((JSLineBreakpoint)breakpoint).getPath().makeAbsolute().toPortableString();
		
//		fThread = new JSThread(this);
//		fThreads = new IThread[] {fThread};
//		
//		DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(this);
//		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(this);
//
//		//test
//		started();		
//		fThread.setBreakpoints(null);
//		fThread.setStepping(false);
//		fThread.SetStackFrames(null);
	}
	
	public void init() throws CoreException, IOException, ParserConfigurationException, SAXException, TransformerException
	{
		fAddonDevUtil.init();
	}
	
	public void startPrcess(ILaunchConfiguration configuration, ILaunch launch) throws Exception
	{
		IPreferenceStore store = AddonDevPlugin.getDefault().getPreferenceStore();
		
		int eclispport = store.getInt(PrefConst.ECLIPSE_PORT);
		SendRequest.debuggerport = store.getString(PrefConst.DEBUGGER_PORT);
		
		//AddonDevPlugin.startServer(this, eclispport);
		SimpleServer.getInstance().Start(this, eclispport);
		
		String[] commandLine = fAddonDevUtil.getDebugStartCommandLine();
		Process process = DebugPlugin.exec(commandLine, null);
		fProcess = DebugPlugin.newProcess(launch, process, commandLine[0]);
		
		fTerminated = false;
		
		fThread = new AddonDevThread(this);
		fThreads = new IThread[] {fThread};
		
		DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(this);
		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(this);

		started();		
		fThread.setBreakpoints(null);
		fThread.setStepping(false);
		fThread.SetStackFrames(null);
	}
	
	public void startDebug()
	{
//		String xml = "";
//		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(AddonDevLineBreakpoint.BREAKPOINT_ID);
//		for (int i = 0; i < breakpoints.length; i++) {
//			IBreakpoint breakpoint = breakpoints[i];
//			//if (supportsBreakpoint(breakpoint)) {
//				if (breakpoint instanceof ILineBreakpoint) {
//					IProject project = ((AddonDevLineBreakpoint)breakpoint).getProject();
//					String path = fAddonDevUtil.convertChrome(project, ((AddonDevLineBreakpoint)breakpoint).getPath().toPortableString());
//					int line = 0;
//
//					try {
//						path = URLEncoder.encode(path, "UTF-8");
//						line = ((AddonDevLineBreakpoint)breakpoint).getLineNumber();
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (CoreException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}	
//
//					xml += String.format("<breakpoint filename=\"%s\" line=\"%s\"/>",  path, line);
//				}
//			//}
//		}	
//		
//		xml = "<xml>" +xml + "</xml>";
		//fBreakpointList.clear();
		
		String xml = getBreakPoint(fBreakpointList);
		try {
			SendRequest.setBreakPoint(xml);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			SendRequest.open("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.fCloseBrowser = false;
	}
	
	public void restart() throws Exception
	{
		started();		
		EventInit();
		fThread.SetStackFrames(null);
		SendRequest.reload();
	}

	public AddonDevUtil getAddonDevUtil()
	{
		return fAddonDevUtil;
	}
//	public String URLDecode(String url)
//	{
//		String decodeurl = null;
//		try {
//			decodeurl = fAddonDevUtil.URLDecode(url);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return decodeurl;
//	}
	
	@Override
	public String getName() throws DebugException {
		// TODO Auto-generated method stub
		if (fName == null) {
			fName = "chromebug";
			//fName = "JS VM";//getLaunch().getLaunchConfiguration().getAttribute(AddonDevPlugin.ATTR_JS_PROGRAM, "JS VM");
		}
		return fName;
	}

	@Override
	public IProcess getProcess() {
		// TODO Auto-generated method stub
		return fProcess;
	}

	@Override
	public IThread[] getThreads() throws DebugException {
		// TODO Auto-generated method stub
		return fThreads;
	}

	@Override
	public boolean hasThreads() throws DebugException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean supportsBreakpoint(IBreakpoint breakpoint) {
		// TODO Auto-generated method stub
		//if (breakpoint.getModelIdentifier().equals("org.eclipse.debug.core.breakpointMarker")){ //IJSConstants.ID_JS_DEBUG_MODEL)) {
		if (breakpoint.getModelIdentifier().equals(AddonDevLineBreakpoint.BREAKPOINT_ID)) {
			return true;
//			try {
//				String program = getLaunch().getLaunchConfiguration().getAttribute(IJSConstants.ATTR_JS_PROGRAM, (String)null);
//				if (program != null) {
//					IMarker marker = breakpoint.getMarker();
//					if (marker != null) {
//						IPath p = new Path(program);
//						return marker.getResource().getFullPath().equals(p);
//					}
//				}
//			} catch (CoreException e) {
//			}			
		}
		return false;
	}

	@Override
	public boolean canTerminate() {
		// TODO Auto-generated method stub
		if(this.fCloseBrowser && !fTerminated)
			return !fTerminated;
		else if(this.fCloseBrowser && fTerminated)
			return !fTerminated;
		else
			return true;
	}

	@Override
	public synchronized boolean isTerminated() {
		// TODO Auto-generated method stub
		if(this.fCloseBrowser && fTerminated)
			return fTerminated;
		
		//return fProcess.isTerminated();
		else
			return false;
	}

	@Override
	public void terminate() {
		if(this.fCloseBrowser)
		{	
			
			// TODO Auto-generated method stub
			try {
				SendRequest.terminate();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//AddonDevPlugin.stopServer();

			while(!fProcess.isTerminated())
			{
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
			}
			fTerminated = true;
			SimpleServer.getInstance().Stop();
			//fireTerminateEvent();
			//fThread.fireTerminateEvent();
		}
		else
		{
			try {
				
				fCloseBrowser = true;
				fBreakpointList.clear();
				fRemoveBreakpointList.clear();
				
				SendRequest.closeBrowser();
				//fThread.fireTerminateEvent();
				
				//fireTerminateEvent();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean canResume() {
		// TODO Auto-generated method stub
		//return !isTerminated() && isSuspended();
		if (isTerminated())
			return false;
		
		if(fCloseBrowser && !isTerminated())
			return true;
		
		return !isSuspended();
	}

	@Override
	public boolean canSuspend() {
		// TODO Auto-generated method stub
		//return !isTerminated() && !isSuspended();
		
		if (isTerminated())
			return false;
		
		return !isSuspended();
	}

	@Override
	public synchronized boolean isSuspended() {
		// TODO Auto-generated method stub
		return fSuspended;
	}

	@Override
	public void resume() throws DebugException {
		// TODO Auto-generated method stub
		
//		fBreakpointList.removeAll(fRemoveBreakpointList);
//		String xml = getBreakPoint(fBreakpointList);
//		try {
//			SendRequest.setBreakPoint(xml);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		String removexml = getBreakPoint(fBreakpointList);
//		try {
//			SendRequest.removeBreakPoint(removexml);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		fSuspended = false;
		//fireResumeEvent(DebugEvent.STEP_OVER);
		
		if(fCloseBrowser)
		{
			fBreakpointList.clear();
			fRemoveBreakpointList.clear();
			
			//resumed(DebugEvent.RESUME);
			installDeferredBreakpoints();
			String data = getBreakPoint(fBreakpointList);
			try {
				SendRequest.setBreakPoint(data);
				SendRequest.open("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			//resumed(DebugEvent.RESUME);
			//for (IThread thread : fThreads) {
			//	thread.resume();
			//}
			try {
				SendRequest.resume();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//	public void resumed(int detail) {
//		fSuspended = false;
//		fThread.fireResumeEvent(detail);
//	}
	
	@Override
	public synchronized void suspend() throws DebugException {
		// TODO Auto-generated method stub
		int i=0;
		i++;
		fSuspended = true;
		suspended(DebugEvent.BREAKPOINT);
	}

	
	@Override
	public void breakpointAdded(IBreakpoint breakpoint) {
		// TODO Auto-generated method stub
		//if (breakpoint.getModelIdentifier().equals("org.eclipse.debug.core.breakpointMarker")){ //IJSConstants.ID_JS_DEBUG_MODEL)) {
		//if (breakpoint.getModelIdentifier().equals(IJSConstants.ID_JS_DEBUG_MODEL)) {
		if(!(breakpoint instanceof AddonDevLineBreakpoint)) return;
		
		if (fSuspended) {
			try {
				if (breakpoint.isEnabled()) {
					if(!fBreakpointList.contains(breakpoint))
					{
						fBreakpointList.add(breakpoint);
					}
				}
			} catch (CoreException e) {
			}			
		}
		else
		{
			try {
				if (breakpoint.isEnabled()) {
					if(!fBreakpointList.contains(breakpoint))
					{
						fBreakpointList.add(breakpoint);
						String data = getBreakPoint(breakpoint);
						SendRequest.setBreakPoint(data);
					}
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}

	@Override
	public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
		// TODO Auto-generated method stub
		if (supportsBreakpoint(breakpoint)) {

			try {
				if (breakpoint.isEnabled()) {
					breakpointAdded(breakpoint);
				} else {
					breakpointRemoved(breakpoint, null);
				}
			} catch (CoreException e) {
			}
		}
	}

	@Override
	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
		// TODO Auto-generated method stub
		if (!(breakpoint instanceof AddonDevLineBreakpoint )) return;
		
		if (fSuspended) 
		{
			//IMarker marker =breakpoint.getMarker();
			//JSLineBreakpoint jb = (JSLineBreakpoint)breakpoint;
			if(fBreakpointList.contains(breakpoint))
			{
				fBreakpointList.remove(breakpoint);
			}
			else
			{
				fRemoveBreakpointList.add(breakpoint);
			}
//			String xml = "";
//
//			IProject project = ((JSLineBreakpoint)breakpoint).getProject();
//			String path = fAddonDevUtil.convertChrome(project, ((JSLineBreakpoint)breakpoint).getPath().toPortableString());
//			int line = 0;
//
//			try {
//				path = URLEncoder.encode(path, "UTF-8");
//				line = ((JSLineBreakpoint)breakpoint).getLineNumber();
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (CoreException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}	
//			xml += String.format("<breakpoint filename=\"%s\" line=\"%s\"/>",  path, line);
//			xml = "<xml>" +xml + "</xml>";
//			
//			try {
//				SendRequest.removeBreakPoint(xml);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		else
		{
			if(fBreakpointList.contains(breakpoint))
			{
				fBreakpointList.remove(breakpoint);
				String data = getBreakPoint(breakpoint);
				try {
					SendRequest.removeBreakPoint(data);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				fRemoveBreakpointList.add(breakpoint);
			}			
		}
	}

	@Override
	public boolean canDisconnect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disconnect() throws DebugException {
		// TODO Auto-generated method stub
		int i=0;
		i++;
	}

	@Override
	public boolean isDisconnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IMemoryBlock getMemoryBlock(long startAddress, long length)
			throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsStorageRetrieval() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IDebugElement#getDebugTarget()
	 */
	public IDebugTarget getDebugTarget() {
		return this;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IDebugElement#getLaunch()
	 */
	public ILaunch getLaunch() {
		
		return fLaunch;
	}
	
	public void started() {
		fThread.fireCreationEvent();
		//fireCreationEvent();
		installDeferredBreakpoints();
		//resumed(DebugEvent.RESUME);
		//fireResumeEvent(DebugEvent.RESUME);
//		try {
//			resume();
//		} catch (DebugException e) {
//		}
	}	
	
	/**
	 * Notification the target has suspended for the given reason
	 * 
	 * @param detail reason for the suspend
	 */
	private void suspended(int detail) {
		fSuspended = true;
		//fireSuspendEvent(detail);
		fThread.fireSuspendEvent(detail);
		//fireEvent(new DebugEvent(fThread, DebugEvent.SUSPEND, detail));
		//DebugEvent ev = new DebugEvent (fThread, DebugEvent.SUSPEND, DebugEvent.BREAKPOINT);
		//DebugPlugin.getDefault ().fireDebugEventSet (new DebugEvent[] { ev });
	}	
	
	private void installDeferredBreakpoints() {
		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints();
		for (IBreakpoint iBreakpoint : breakpoints) {
			if(iBreakpoint instanceof AddonDevLineBreakpoint)
			{
				breakpointAdded(iBreakpoint);
			}
		}
	}
	
	protected void stepOver() throws DebugException {
				
//		try {
//			
//			SendRequest.stepOver();
//			resumed(DebugEvent.STEP_OVER);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		int i=0;
//		i++;
		fSuspended = false;
		//fireResumeEvent(DebugEvent.STEP_OVER);
		try {
			SendRequest.stepOver();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//resumed(DebugEvent.STEP_OVER);
//		for (IThread thread : fThreads) {
//			fSuspended = false;
//			//thread.fireResumeEvent(DebugEvent.STEP_OVER);
//			thread.stepOver();
//		}
		//fThread.stepOver();
		
	}
	
	protected void stepInto() throws DebugException {
		//fireResumeEvent(DebugEvent.STEP_INTO);
		try {
			SendRequest.stepInto();
			//resumed(DebugEvent.STEP_INTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	/**
	 * Notification a breakpoint was encountered. Determine
	 * which breakpoint was hit and fire a suspend event.
	 * 
	 * @param event debug event
	 * @throws CoreException 
	 */
	public void breakpointHit(String event, String data) throws DebugException{
		
		fstateChange = true;
		childVariablesDataCash.clear();
		
		AddonDevStackFrame[] stackframes = null;
		try {
			stackframes = XMLUtils.StackFramesFromXML(this, data);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		fThread.SetStackFrames(stackframes);

		//suspended(DebugEvent.BREAKPOINT);	
		fThread.suspend();
	}

	
//	<xml>
//	<error filename="[name]" line="[line]" msg="[errorMessage]"/>
//	.
//	</xml>
	 public void setErrorMarker(String data)
	 {
		 try {
			List<Map<String, String>> maplist = XMLUtils.getDataFromXML(data);
			
		//AddonDevPlugin
			Iterator<Map<String, String>> miter = maplist.iterator();
			while(miter.hasNext())
			{
				Map<String, String> map = miter.next();
				String filename = map.get("filename");
				int line = Integer.parseInt(map.get("line"));
				String msg = map.get("msg");
				IPath path = new Path(filename);
				IResource resource = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				IMarker marker = resource.createMarker(IMarker.PROBLEM);
				marker.setAttribute(IMarker.LINE_NUMBER, line);
				marker.setAttribute(IMarker.LOCATION, path.toString());
				marker.setAttribute(IMarker.MESSAGE, msg);
			}
						
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 public void deleteErrorMarker()
	 {
		 //PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
		 //JavaScriptDebugPlugin.getDefault().getWorkbench().
	 }
	
//    protected void fireEvent(DebugEvent event) {
//        DebugPlugin manager= DebugPlugin.getDefault();
//        if (manager != null) {
//            manager.fireDebugEventSet(new DebugEvent[]{event});
//        }
//    }
//	
	//private boolean bb = false;
//	/**
//	 * Returns the current stack frames in the target.
//	 * 
//	 * @return the current stack frames in the target
//	 * @throws DebugException if unable to perform the request
//	 */
//	protected IStackFrame[] getStackFrames() throws DebugException {
//
////		try {
////			String reqdata = SendRequest.RequestData("stack");
////			IStackFrame[] theFrames = new IStackFrame[frames.length];
////			for (int i = 0; i < frames.length; i++) {
////				String data = frames[i];
////				theFrames[frames.length - i - 1] = new JSStackFrame(fThread, data, i);
////			}
////			return theFrames;
////		} catch (IOException e) {
////			
////		}
//		
//		//
//		//try {
//			//String framesData = SendRequest.RequestData("stack");
////			String framesData = "tmp";
////			bb = true;
////			if (framesData != null && bb) {
////			//if (framesData != null) {
////				IStackFrame[] theFrames = new IStackFrame[1];
////				for (int i = 0; i < theFrames.length; i++) {
////					String data = JSTestData.TESTDATA ;//"C:/workspace/src/PDE/eclipsePDE/runtime-EclipseApplication/test0/test.js|9|main|m|n";
////					theFrames[theFrames.length - i - 1] = new JSStackFrame(fThread, data, i, this);
////				}	
////				return theFrames;
////			}
//			
//			IStackFrame[] theFrames = new JSStackFrame[]{new JSStackFrame(fThread, JSTestData.TESTDATA, 0, this)};
//			return theFrames;
//			
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//		//return new IStackFrame[0];
//	}

	 
	
	 public IVariable getVariable(String stackFramedepth, String parent, String name) {
		ArrayList<IVariable> variables = null;
		String xmldata = "";
		String path = "";
		
		try {
			if(parent != null)
				path = parent + "." + name;
			else
			{
				//parent = name;
				path = name;
			}
			
			if(path == null) path = "";
			
			xmldata =  SendRequest.getValue(stackFramedepth, path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return null;
		}
		try {
			variables = XMLUtils.VariablesFromXML(this, stackFramedepth, xmldata, parent);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return variables.get(0);
	}
		
	public ArrayList<IVariable> getVariables(String stackFramedepth, String parent, String name) {
		ArrayList<IVariable> variables = null;
		String xmldata = "";
		String path = "";
		
		try {
			if(parent != null)
				path = parent + "." + name;
			else
			{
				//parent = name;
				path = name;
			}
			
			if(path == null) path = "";
			
			xmldata =  SendRequest.getValues(stackFramedepth, path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return null;
		}
		try {
			variables = XMLUtils.VariablesFromXML(this, stackFramedepth, xmldata, parent);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return variables;
	}
	
	private HashMap<String, String> childVariablesDataCash = new HashMap<String, String>();
	
	protected ArrayList<IVariable> getChildVariables(String stackFramedepth, String parent, String name) {
		ArrayList<IVariable> variables = null;
		String xmldata = "";
		String path = "";
		try {
			if(parent != null)
				path = parent + "." + name;
			else
			{
				parent = name;
				path = name;
			}
			
			if(path == null) path = "";
			
			if(childVariablesDataCash.containsKey(path))
			{
				xmldata = childVariablesDataCash.get(path);
			}
			else
			{
				xmldata =  SendRequest.getValues(stackFramedepth, path);
				//VariablesXML = xmldata;
				childVariablesDataCash.put(path, xmldata);
				//fstateChange = false;
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			variables = XMLUtils.VariablesFromXML(this, stackFramedepth, xmldata, parent);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return variables;
	}	
	
	public void EventInit()
	{

	}
	
	 public Object getAdapter(Class adapter) {
		 
	        if (adapter.equals(ILaunch.class)){
	            return fLaunch;
	        }else if (adapter.equals(IResource.class)) {
	            // used by Variable ContextManager, and Project:Properties menu item
//	            if( file!=null ) {
//	                IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(file[0]);
//	                
//	                if (files != null && files.length > 0){
//	                    return files[0];
//
//	                }else{
//	                    return null;
//	                    
//	                }
//	            }
	        } else if (adapter.equals(IPropertySource.class)){
	            return fLaunch.getAdapter(adapter);
	            
	        } else if (adapter.equals(ITaskListResourceAdapter.class) 
	                || adapter.equals(org.eclipse.debug.ui.actions.IRunToLineTarget.class) 
	                || adapter.equals(org.eclipse.debug.ui.actions.IToggleBreakpointsTarget.class) 
	                ){
	            return  super.getAdapter(adapter);
	        }
//	        else if (adapter == IDebugElement.class) {
//				return this;
//			}
	        
	        //AdapterDebug.printDontKnow(this, adapter);
	        return super.getAdapter(adapter);      
	 }
	 
	@Override
	public String getModelIdentifier() {
		// TODO Auto-generated method stub
		//return AddonDevPlugin.ID_DEBUG_MODEL;
		return AddonDevDebugModelPresentation.DEBUG_MODEL_ID;
	}

	@Override
	public void launchAdded(ILaunch launch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchChanged(ILaunch launch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchRemoved(ILaunch launch) {
		// TODO Auto-generated method stub
        if (launch == this.fLaunch) {
            IBreakpointManager breakpointManager= DebugPlugin.getDefault().getBreakpointManager();
            breakpointManager.removeBreakpointListener(this);
        }
	}
	
	private String getBreakPoint(IBreakpoint breakpoint)
	{
		ArrayList<IBreakpoint> breakpoints = new ArrayList<IBreakpoint>();
		breakpoints.add(breakpoint);
		return getBreakPoint(breakpoints);
	}
	
	private String getBreakPoint(List<IBreakpoint> breakpoints)
	{
		String xml = "";
		for (IBreakpoint breakpoint : breakpoints) {
			if (breakpoint instanceof ILineBreakpoint) {
				AddonDevLineBreakpoint addonbreakpoint = (AddonDevLineBreakpoint)breakpoint;
				IProject project = addonbreakpoint.getProject();
				//String path = fAddonDevUtil.convertChrome(project, ((AddonDevLineBreakpoint)breakpoint).getPath().toPortableString());
				String path = AddonDevPlugin.getDefault().getChromeURLMap(project, false).convertLocal2Chrome(addonbreakpoint.getFullPath());
				int line = 0;

				try {
					path = URLEncoder.encode(path, "UTF-8");
					line = addonbreakpoint.getLineNumber();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	

				xml += String.format("<breakpoint filename=\"%s\" line=\"%s\"/>",  path, line);
			}			
		}
		xml = "<xml>" +xml + "</xml>";
		return xml;
	}

	@Override
	public void handleDebugEvents(DebugEvent[] events) {
		// TODO Auto-generated method stub
		for (int i = 0; i < events.length; i++) {
			DebugEvent event = events[i];
			if (event.getKind() == DebugEvent.TERMINATE) {
				Object source = event.getSource();
				if (source instanceof AddonDebugTarget)
				{
					SimpleServer.getInstance().Stop();
					fCloseBrowser = true;
					fTerminated = true;					
				}else if (source instanceof IProcess) {
					SimpleServer.getInstance().Stop();
					fTerminated = true;
					fCloseBrowser = true;
					fThread.fireTerminateEvent();
				}
//						|| source instanceof IDebugTarget) {
//					//getPHPDBGProxy().stop();
//				} else if (source instanceof IProcess) {
//					if (getDebugTarget().getProcess() == (IProcess) source) {
//						//getPHPDBGProxy().stop();
//					}
//				}
			} else if (event.getKind() == DebugEvent.SUSPEND) {
				//getPHPDBGProxy().pause();
			}
		}		
	}	
}
