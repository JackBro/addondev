package jp.addondev.debug.core.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.parser.ContentModel;
import javax.xml.parsers.ParserConfigurationException;

import jp.addondev.AddonDevPlugin;
import jp.addondev.debug.ui.model.JSDebugModelPresentation;
import jp.addondev.util.AddonDevUtil;
import jp.addondev.util.XMLUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
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
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.UIPlugin;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.tasklist.ITaskListResourceAdapter;
import org.xml.sax.SAXException;


public class AddonDebugTarget extends PlatformObject implements IDebugTarget, ILaunchListener {

	private IProcess fProcess;
	
	// containing launch object
	private ILaunch fLaunch;
	
	// program name
	private String fName;
	
	// suspend state
	private boolean fSuspended = true;
	
	// terminated state
	private boolean fTerminated = false;
	
	// threads
	private JSThread fThread;
	private IThread[] fThreads;
	
	private AddonDevUtil fAddonDevUtil;
	//private List<IProject> fDebugProjects;
	
	private boolean fstateChange = false; 
	

	private ArrayList<IBreakpoint> fBreakpointList = new ArrayList<IBreakpoint>();
	private ArrayList<IBreakpoint> fRemoveBreakpointList = new ArrayList<IBreakpoint>();
	
	
	public AddonDebugTarget(ILaunchConfiguration configuration, ILaunch launch, AddonDevUtil addondevutil) throws Exception {
		// TODO Auto-generated constructor stub
		fLaunch = launch;		
		fAddonDevUtil = addondevutil;
		
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
	
	public void startPrcess(ILaunchConfiguration configuration, ILaunch launch) throws Exception
	{
		int eclispport = AddonDevPlugin.getDefault().getPreferenceStore().getInt(AddonDevPlugin.DEBUG_ECLIPSEPORT);
		SendRequest.debuggerport = AddonDevPlugin.getDefault().getPreferenceStore().getString(AddonDevPlugin.DEBUG_DEBUGGERPORT);
		
		AddonDevPlugin.startServer(this, eclispport);
		
		String[] commandLine = fAddonDevUtil.getProcessCommandLine();
		Process process = DebugPlugin.exec(commandLine, null);
		fProcess = DebugPlugin.newProcess(launch, process, "firefox");
		fTerminated = true;
		
		fThread = new JSThread(this);
		fThreads = new IThread[] {fThread};
		
		DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(this);
		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(this);

		//test
		started();		
		fThread.setBreakpoints(null);
		fThread.setStepping(false);
		fThread.SetStackFrames(null);
	}
	
	public void startDebug()
	{
		String xml = "";
		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(JSLineBreakpoint.ADDON_BREAK_MARKER);
		for (int i = 0; i < breakpoints.length; i++) {
			IBreakpoint breakpoint = breakpoints[i];
			//if (supportsBreakpoint(breakpoint)) {
				if (breakpoint instanceof ILineBreakpoint) {
					IProject project = ((JSLineBreakpoint)breakpoint).getProject();
					String path = fAddonDevUtil.convertChrome(project, ((JSLineBreakpoint)breakpoint).getPath().toPortableString());
					int line = 0;

					try {
						path = URLEncoder.encode(path, "UTF-8");
						line = ((JSLineBreakpoint)breakpoint).getLineNumber();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	

					xml += String.format("<breakpoint filename=\"%s\" line=\"%s\"/>",  path, line);
				}
			//}
		}	
		
		xml = "<xml>" +xml + "</xml>";
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
			fName = "JS Program";
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
		if (breakpoint.getModelIdentifier().equals(JSLineBreakpoint.ADDON_BREAK_MARKER)) {
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
		return fProcess.canTerminate();
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return fProcess.isTerminated();
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		try {
			SendRequest.terminate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AddonDevPlugin.stopServer();
		
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
		
		fThread.fireTerminateEvent();
	}

	@Override
	public boolean canResume() {
		// TODO Auto-generated method stub
		return !isTerminated() && isSuspended();
	}

	@Override
	public boolean canSuspend() {
		// TODO Auto-generated method stub
		return !isTerminated() && !isSuspended();
	}

	@Override
	public boolean isSuspended() {
		// TODO Auto-generated method stub
		return fSuspended;
	}

	@Override
	public void resume() throws DebugException {
		// TODO Auto-generated method stub
		
		fBreakpointList.removeAll(fRemoveBreakpointList);
		String xml = getBreakPoint(fBreakpointList);
		try {
			SendRequest.setBreakPoint(xml);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String removexml = getBreakPoint(fBreakpointList);
		try {
			SendRequest.removeBreakPoint(removexml);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			SendRequest.resume();
			resumed(DebugEvent.RESUME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void resumed(int detail) {
		fSuspended = false;
		fThread.fireResumeEvent(detail);
	}
	
	@Override
	public void suspend() throws DebugException {
		// TODO Auto-generated method stub
		int i=0;
		i++;
	}

	
	@Override
	public void breakpointAdded(IBreakpoint breakpoint) {
		// TODO Auto-generated method stub
		//if (breakpoint.getModelIdentifier().equals("org.eclipse.debug.core.breakpointMarker")){ //IJSConstants.ID_JS_DEBUG_MODEL)) {
		//if (breakpoint.getModelIdentifier().equals(IJSConstants.ID_JS_DEBUG_MODEL)) {
		 if (fSuspended && breakpoint instanceof JSLineBreakpoint ) {
			try {
				if (breakpoint.isEnabled()) {
					if(!fBreakpointList.contains(breakpoint))
					{
						fBreakpointList.add(breakpoint);
					}
//					IMarker marker =breakpoint.getMarker();
//					if (marker != null) {
//						String path = marker.getResource().getLocation().toOSString();
//						//String tmpp= "setbreak:" + path+":"+ (((ILineBreakpoint)breakpoint).getLineNumber() - 1);
//						//sendRequest("setbreak:" + path+":"+ (((ILineBreakpoint)breakpoint).getLineNumber() - 1));
//					}
				}
			} catch (CoreException e) {
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
		if (fSuspended && breakpoint instanceof JSLineBreakpoint ) 
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
	}

	@Override
	public boolean canDisconnect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disconnect() throws DebugException {
		// TODO Auto-generated method stub
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
		installDeferredBreakpoints();
		resumed(DebugEvent.RESUME);
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
		fThread.fireSuspendEvent(detail);
		//fireEvent(new DebugEvent(fThread, DebugEvent.SUSPEND, detail));
	}	
	
	private void installDeferredBreakpoints() {
		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(JSLineBreakpoint.ADDON_BREAK_MARKER);
		//IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints("org.eclipse.debug.core.breakpointMarker");//IJSConstants.ID_JS_DEBUG_MODEL);
		for (int i = 0; i < breakpoints.length; i++) {
			breakpointAdded(breakpoints[i]);
		}
	}
	
	protected void stepOver() throws DebugException {
		try {
			SendRequest.stepOver();
			resumed(DebugEvent.STEP_OVER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void stepInto() throws DebugException {
		try {
			SendRequest.stepInto();
			resumed(DebugEvent.STEP_INTO);
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
	void breakpointHit(String event, String data) {
		fstateChange = true;
		
		JSStackFrame[] stackframes = null;
		try {
			stackframes = XMLUtils.StackFramesFromXML(this, data);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		childVariablesDataCash.clear();
		
		//IStackFrame[] theFrames = new JSStackFrame[]{new JSStackFrame(fThread, JSTestData.TESTDATA, 0, this)};
		fThread.SetStackFrames(stackframes);
		suspended(DebugEvent.BREAKPOINT);
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
				IPath path = fAddonDevUtil.getPath(filename);
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

	/**
	 * Returns the current value of the given variable.
	 * 
	 * @param variable
	 * @return variable value
	 * @throws DebugException if the request fails
	 */
	protected IValue getVariableValue(JSVariable variable) throws DebugException {
//		fRequestWriter.println("var " + variable.getStackFrame().getIdentifier() + " " + variable.getName());
//		fRequestWriter.flush();
//		try {
//			String value = fRequestReader.readLine();
//			return new PDAValue(this, value);
//		} catch (IOException e) {
//			abort(MessageFormat.format("Unable to retrieve value for variable {0}", new String[]{variable.getName()}), e);
//		}
//		return null;
		return null;
	}
		
	protected IVariable[] getVariables(String stackFramedepth, String parent, String name) {
		IVariable[] variables = null;
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
	
	private String VariablesXML = null;
	private HashMap<String, String> childVariablesDataCash = new HashMap<String, String>();
	
	protected IVariable[] getChildVariables(String stackFramedepth, String parent, String name) {
		IVariable[] variables = null;
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
	        
	        //AdapterDebug.printDontKnow(this, adapter);
	        return super.getAdapter(adapter);      
	 }
	 
	@Override
	public String getModelIdentifier() {
		// TODO Auto-generated method stub
		//return AddonDevPlugin.ID_DEBUG_MODEL;
		return JSDebugModelPresentation.ADDON_DEBUG_MODEL_ID;
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
	
	private String getBreakPoint(List<IBreakpoint> breakpoints)
	{
		String xml = "";
		for (IBreakpoint breakpoint : breakpoints) {
			if (breakpoint instanceof ILineBreakpoint) {
				IProject project = ((JSLineBreakpoint)breakpoint).getProject();
				String path = fAddonDevUtil.convertChrome(project, ((JSLineBreakpoint)breakpoint).getPath().toPortableString());
				int line = 0;

				try {
					path = URLEncoder.encode(path, "UTF-8");
					line = ((JSLineBreakpoint)breakpoint).getLineNumber();
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
	
}
