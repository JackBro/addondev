package org.addondev.debug.core.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;

import net.arnx.jsonic.JSONException;

import org.addondev.debug.net.SendRequest;
import org.addondev.debug.net.SimpleServer;
import org.addondev.debug.ui.model.AddonDevDebugModelPresentation;
import org.addondev.debug.util.XMLUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
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
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.xml.sax.SAXException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class AddonDevDebugTarget extends PlatformObject implements IDebugTarget, ILaunchListener, IDebugEventSetListener {
	
	class DebugHttpHandler implements HttpHandler
	{

		@Override
		public void handle(HttpExchange exchange) {
			// TODO Auto-generated method stub
//			String query = exchange.getRequestURI().getQuery();     
			
			StringBuilder data = new StringBuilder();
            InputStream in = exchange.getRequestBody(); 
            byte buff[] = new byte[1024];
            int len;
            try {
				while((len = in.read(buff)) != -1) {
					//out.write(buff, 0, len);
					data.append(new String(buff));
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			finally
			{
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//StringBuilder sb = new StringBuilder();
            //sb.append("<html></html>");
            //sb.append("accept");
			//JsonData writejsondata = new JsonData();
			//writejsondata.setCmd("accept");
			//StringBuilder sb = new StringBuilder(JsonUtil.getJsonText(writejsondata));
			
			StringBuilder sb = new StringBuilder("<accept/>");
			
            byte[] response = sb.toString().getBytes();
            OutputStream output = null;
            try {
				exchange.sendResponseHeaders(200, response.length);
	            output = exchange.getResponseBody();
	            output.write(response);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally{
	            if(output != null) 
	            {
	            	try {
						output.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
			}
            
			JsonData jsondata = JsonUtil.getJsonData(data.toString());
			if(jsondata == null) return;

			String cmd = jsondata.getCmd();
			if("suspend".equals(cmd))
			{
				breakpointHit(jsondata);
			}
        	else if("ready".equals(cmd))
        	{
        		startDebug();
        	}
        	else if("closebrowser".equals(cmd))
        	{
        		setCloseBrowser(true);
        	}
        	else if("error".equals(cmd))
        	{
        		int i=0;
        		i++;
        	}			
		}
	}
	
	private ILaunch fLaunch;
	//private ILaunchConfiguration fConfiguration;
	private IPreferenceStore fStore;
	
	// program name
	private String fName;
	
	private IProcess fProcess;
	private boolean fSuspended = true;
	private boolean fTerminated = false;
	private boolean fCloseBrowser = true;
	
	// threads
	private AddonDevThread fThread;
	private IThread[] fThreads;
	
	//private AddonDevUtil fAddonDevUtil;
	//private List<IProject> fDebugProjects;

	private ArrayList<IBreakpoint> fAddBreakpointList = new ArrayList<IBreakpoint>();
	private ArrayList<IBreakpoint> fRemoveBreakpointList = new ArrayList<IBreakpoint>();
	
	public boolean isCloseBrowser() {
		return fCloseBrowser;
	}

	public void setCloseBrowser(boolean closeBrowser) {
		this.fCloseBrowser = closeBrowser;
	}

	public AddonDevDebugTarget(ILaunchConfiguration configuration, ILaunch launch)
	{
		// TODO Auto-generated constructor stub
		//fConfiguration = configuration;
		fLaunch = launch;	
		//fStore = AddonDevPlugin.getDefault().getPreferenceStore();	

		//fAddonDevUtil = new AddonDevUtil(configuration);
		DebugPlugin.getDefault().addDebugEventListener(this);
	}
	
	public void startPrcess(int eclispport, int chromebugport, String[] commandline) throws CoreException, IOException
	{
		SimpleServer.getInstance().start(new DebugHttpHandler(), eclispport);
		
		SendRequest.setDebuggerPort(chromebugport); //store.getString(PrefConst.DEBUGGER_PORT);

		Process process = DebugPlugin.exec(commandline, null);
		fProcess = DebugPlugin.newProcess(fLaunch, process, commandline[0]);
		
		fTerminated = false;
		
		fThread = new AddonDevThread(this);
		fThreads = new IThread[] {fThread};
		
		DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(this);
		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(this);

		started();		
		fThread.setBreakpoints(null);
		fThread.setStepping(false);
		fThread.setStackFrames(null);
	}
	
	public void startDebug()
	{
		
		//String xml = getBreakPoint(fBreakpointList);
		try {			
			//SendRequest.setBreakPoint(xml);
			List<IBreakpoint> list = Arrays.asList(getBreakPoints());
			SendRequest.setBreakPoint(list);
			
			SendRequest.open();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		try {
//			SendRequest.open("");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		this.fCloseBrowser = false;
	}
	
	public void restart() throws Exception
	{
		started();		
		//EventInit();
		fThread.setStackFrames(null);
		SendRequest.reload();
	}

	
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
		return breakpoint.getModelIdentifier().equals(AddonDevBreakpoint.BREAKPOINT_ID);
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
		if(fCloseBrowser)
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
			SimpleServer.getInstance().stop();
			//fireTerminateEvent();
			//fThread.fireTerminateEvent();
		}
		else
		{
			try {
				
				fCloseBrowser = true;
				fAddBreakpointList.clear();
				fRemoveBreakpointList.clear();
				
				//SendRequest.closeBrowser();
				SendRequest.closeWindow();
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
		if (isTerminated())
			return false;
		
		if(fCloseBrowser && !isTerminated())
			return true;
		
		return isSuspended();
	}

	@Override
	public boolean canSuspend() {
		// TODO Auto-generated method stub	
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
		
		fSuspended = false;
		//fireResumeEvent(DebugEvent.STEP_OVER);

			//resumed(DebugEvent.RESUME);
			//installDeferredBreakpoints();
			//String data = getBreakPoint(fBreakpointList);
		try {
			
			if(fAddBreakpointList.size()>0) SendRequest.setBreakPoint(fAddBreakpointList);
			if(fRemoveBreakpointList.size()>0) SendRequest.removeBreakPoint(fRemoveBreakpointList);
			
			//SendRequest.setBreakPoint(data);		
			//SendRequest.open("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			fAddBreakpointList.clear();
			fRemoveBreakpointList.clear();			
		}

		if(fCloseBrowser)
		{
			try {
				//SendRequest.open("");
				SendRequest.open();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				
			} 
		}
		else
		{
			try {
				SendRequest.resume();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				
			} 
		}
	}
	
	@Override
	public synchronized void suspend() throws DebugException {
		// TODO Auto-generated method stub
		fSuspended = true;
		suspended(DebugEvent.BREAKPOINT);
	}

	
//	@Override
//	public void breakpointAdded(IBreakpoint breakpoint) {
//		// TODO Auto-generated method stub
//		//if (breakpoint.getModelIdentifier().equals("org.eclipse.debug.core.breakpointMarker")){ //IJSConstants.ID_JS_DEBUG_MODEL)) {
//		//if (breakpoint.getModelIdentifier().equals(IJSConstants.ID_JS_DEBUG_MODEL)) {
//		if(!(breakpoint instanceof AddonDevBreakpoint)) return;
//		
//		if (fSuspended) {
//			try {
//				if (breakpoint.isEnabled()) {
//					if(!fBreakpointList.contains(breakpoint))
//					{
//						fBreakpointList.add(breakpoint);
//					}
//				}
//			} catch (CoreException e) {
//			}			
//		}
//		else
//		{
//			try {
//				if (breakpoint.isEnabled()) {
//					if(!fBreakpointList.contains(breakpoint))
//					{
//						fBreakpointList.add(breakpoint);
//						String data = getBreakPoint(breakpoint);
//						SendRequest.setBreakPoint(data);
//					}
//				}
//			} catch (CoreException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}			
//		}
//	}
	
	@Override
	public void breakpointAdded(IBreakpoint breakpoint) {
		//if (breakpoint.getModelIdentifier().equals("org.eclipse.debug.core.breakpointMarker")){ //IJSConstants.ID_JS_DEBUG_MODEL)) {
		//if (breakpoint.getModelIdentifier().equals(IJSConstants.ID_JS_DEBUG_MODEL)) {
		if (supportsBreakpoint(breakpoint)) 
		{		
			if(!fAddBreakpointList.contains(breakpoint))
			{
				fAddBreakpointList.add(breakpoint);
			}		
		}
	}
	
	@Override
	public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
		if (supportsBreakpoint(breakpoint)) {

			try {
				if (breakpoint.isEnabled()) {
					breakpointAdded(breakpoint);
				} else {
					if(fAddBreakpointList.contains(breakpoint)){
						fAddBreakpointList.remove(breakpoint);
					}else{
						fRemoveBreakpointList.add(breakpoint);
					}
				}
			} catch (CoreException e) {
			}
		}
	}
	
	@Override
	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
		if (supportsBreakpoint(breakpoint)) {
			if(!fRemoveBreakpointList.contains(breakpoint)){
				fRemoveBreakpointList.add(breakpoint);
			}
		}
	}
	
//	@Override
//	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
//		// TODO Auto-generated method stub
//		if (!(breakpoint instanceof AddonDevBreakpoint )) return;
//		
//		if (fSuspended) 
//		{
//			//IMarker marker =breakpoint.getMarker();
//			//JSLineBreakpoint jb = (JSLineBreakpoint)breakpoint;
//			if(fBreakpointList.contains(breakpoint))
//			{
//				fBreakpointList.remove(breakpoint);
//			}
//			else
//			{
//				fRemoveBreakpointList.add(breakpoint);
//			}
//		}
//		else
//		{
//			if(fBreakpointList.contains(breakpoint))
//			{
//				fBreakpointList.remove(breakpoint);
//				String data = getBreakPoint(breakpoint);
//				try {
//					SendRequest.removeBreakPoint(data);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			else
//			{
//				fRemoveBreakpointList.add(breakpoint);
//			}			
//		}
//	}

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
		//installDeferredBreakpoints();
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
	
//	private void installDeferredBreakpoints() {
//		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints();
//		for (IBreakpoint iBreakpoint : breakpoints) {
//			if(iBreakpoint instanceof AddonDevBreakpoint)
//			{
//				breakpointAdded(iBreakpoint);
//			}
//		}
//	}
	
	protected void stepOver() throws DebugException {
		fSuspended = false;
		try {
			SendRequest.stepOver();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

//	public void breakpointHit(String event, String data) throws DebugException{
//		childVariablesDataCash.clear();
//		
//		AddonDevStackFrame[] stackframes = null;
//		try {
//			stackframes = XMLUtils.stackFramesFromXML(this, data);
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//		
//		fThread.setStackFrames(stackframes);
//		fThread.suspend();
//	}
	
	public void breakpointHit(JsonData jsondata){
		childVariablesDataCash.clear();
		
		try {
			//IStackFrame[] stackframes = JsonUtil.getStackFrames(this, jsondata);
			List<IStackFrame> stackframes = JsonUtil.getStackFrames(this, jsondata);
			fThread.setStackFrames(stackframes.toArray(new IStackFrame[stackframes.size()]));
			fThread.suspend();
		} catch (DebugException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 
	 public IVariable getVariable(String stackFramedepth, String parent, String name) {
		List<IVariable> variables = null;
		String json = "";
		JsonData jsondata = null;
		String path = "";
		if(parent != null)
			path = parent + "." + name;
		else
		{
			//parent = name;
			path = name;
		}
		
		if(path == null) path = "";
		
		try {
			json =  SendRequest.getValue(stackFramedepth, path);
			jsondata = JsonUtil.getJsonData(json);
			variables = JsonUtil.getVariables(this, jsondata, stackFramedepth, parent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return null;
		} catch (DebugException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(variables == null) return null; 
		return variables.get(0);
	}
		
	//public ArrayList<IVariable> getVariables(String stackFramedepth, String parent, String name) {
	public List<IVariable> getVariables(String stackFramedepth, String parent, String name) {
		List<IVariable> variables = null;
		String json = "";
		String path = "";
		
		if(parent != null){
			path = parent + "." + name;
		} else {
			//parent = name;
			path = name;
		}
		
		if(path == null) path = "";
		
		try {
			//xmldata =  SendRequest.getValues(stackFramedepth, path);
			json =  SendRequest.getValues(stackFramedepth, path);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return null;
		}
		try {
			//variables = XMLUtils.variablesFromXML(this, stackFramedepth, xmldata, parent);
			JsonData jsondata = JsonUtil.getJsonData(json);
			variables = JsonUtil.getVariables(this, jsondata, stackFramedepth, parent);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			variables = new ArrayList<IVariable>();
		}
		
		return variables;
	}
	
	private HashMap<String, String> childVariablesDataCash = new HashMap<String, String>();
	
	protected List<IVariable> getChildVariables(String stackFramedepth, String parent, String name) {
		List<IVariable> variables = null;
		String json = "";
		String path = "";
		
		if(parent != null)
			path = parent + "." + name;
		else
		{
			parent = name;
			path = name;
		}
		
		if(path == null) path = "";
		
		try {
			if(childVariablesDataCash.containsKey(path))
			{
				json = childVariablesDataCash.get(path);
			}
			else
			{
				json =  SendRequest.getValues(stackFramedepth, path);
				childVariablesDataCash.put(path, json);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JsonData jsondata = JsonUtil.getJsonData(json);
			variables = JsonUtil.getVariables(this, jsondata, stackFramedepth, parent);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			variables = new ArrayList<IVariable>();
		}	
		return variables;
	}	
//	protected ArrayList<IVariable> getChildVariables(String stackFramedepth, String parent, String name) {
//		ArrayList<IVariable> variables = null;
//		String xmldata = "";
//		String path = "";
//		try {
//			if(parent != null)
//				path = parent + "." + name;
//			else
//			{
//				parent = name;
//				path = name;
//			}
//			
//			if(path == null) path = "";
//			
//			if(childVariablesDataCash.containsKey(path))
//			{
//				xmldata = childVariablesDataCash.get(path);
//			}
//			else
//			{
//				xmldata =  SendRequest.getValues(stackFramedepth, path);
//				//VariablesXML = xmldata;
//				childVariablesDataCash.put(path, xmldata);
//				//fstateChange = false;
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			variables = XMLUtils.variablesFromXML(this, stackFramedepth, xmldata, parent);
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//		return variables;
//	}	
	
	
	
//	public void EventInit()
//	{
//
//	}
	
	 public Object getAdapter(Class adapter) {
		 
	        if (adapter.equals(ILaunch.class)){
	            return fLaunch;
	        }
//	        else if (adapter.equals(IResource.class)) {
//	            // used by Variable ContextManager, and Project:Properties menu item
////	            if( file!=null ) {
////	                IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(file[0]);
////	                
////	                if (files != null && files.length > 0){
////	                    return files[0];
////
////	                }else{
////	                    return null;
////	                    
////	                }
////	            }
//	        } else if (adapter.equals(IPropertySource.class)){
//	            return fLaunch.getAdapter(adapter);
//	            
//	        } else if (adapter.equals(ITaskListResourceAdapter.class) 
//	                || adapter.equals(org.eclipse.debug.ui.actions.IRunToLineTarget.class) 
//	                || adapter.equals(org.eclipse.debug.ui.actions.IToggleBreakpointsTarget.class) 
//	                ){
//	            return  super.getAdapter(adapter);
//	        }
//	        else if (adapter == IDebugElement.class) {
//				return this;
//			}
	        
	        //AdapterDebug.printDontKnow(this, adapter);
	        return super.getAdapter(adapter);      
	 }
	 
	@Override
	public String getModelIdentifier() {
		// TODO Auto-generated method stub
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
	
//	private String getBreakPoint(IBreakpoint breakpoint)
//	{
//		ArrayList<IBreakpoint> breakpoints = new ArrayList<IBreakpoint>();
//		breakpoints.add(breakpoint);
//		return getBreakPoint(breakpoints);
//	}
	
	private IBreakpoint[] getBreakPoints()
	{
		return DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(AddonDevBreakpoint.BREAKPOINT_ID);
	}
	
//	private JsonData convertBreakPoints2Json(List<IBreakpoint> breakpoints){
//
//		ArrayList<Map<String, String>> propertylist = new ArrayList<Map<String,String>>();
//		
//		for (IBreakpoint breakpoint : breakpoints) {
//			if (breakpoint instanceof AddonDevBreakpoint) {
//				
//				AddonDevBreakpoint addonbreakpoint = (AddonDevBreakpoint)breakpoint;
//				IProject project = addonbreakpoint.getProject();
//				ChromeURLMap chromeurlmap = AddonDevPlugin.getDefault().getChromeURLMap(project, false);
//				String chromeurl = chromeurlmap.convertLocal2Chrome(addonbreakpoint.getFile());
//				int line;
//				try {
//					line = addonbreakpoint.getLineNumber();
//				} catch (CoreException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					continue;
//				}
//				
//				HashMap<String, String> prop = new HashMap<String, String>();
//				prop.put("filename", chromeurl);
//				prop.put("line", StringConverter.asString(line));
//				
//				propertylist.add(prop);
//			}			
//		}
//		JsonData json = new JsonData();
//		json.setPropertylist(propertylist);
//		return json;		
//	}
	
//	private String getBreakPoint(List<IBreakpoint> breakpoints)
//	{
//		String xml = "";
//		for (IBreakpoint breakpoint : breakpoints) {
//			if (breakpoint instanceof ILineBreakpoint) {
//				AddonDevBreakpoint addonbreakpoint = (AddonDevBreakpoint)breakpoint;
//				IProject project = addonbreakpoint.getProject();
//				//String path = fAddonDevUtil.convertChrome(project, ((AddonDevLineBreakpoint)breakpoint).getPath().toPortableString());
//				ChromeURLMap chromeurlmap = AddonDevPlugin.getDefault().getChromeURLMap(project, false);
//				//String path = chromeurlmap.convertLocal2Chrome(addonbreakpoint.getLocation());
//				String path = chromeurlmap.convertLocal2Chrome(addonbreakpoint.getFile());
//				int line = 0;
//
//				try {
//					path = URLEncoder.encode(path, "UTF-8");
//					line = addonbreakpoint.getLineNumber();
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (CoreException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}	
//
//				xml += String.format("<breakpoint filename=\"%s\" line=\"%s\"/>",  path, line);
//			}			
//		}
//		xml = "<xml>" +xml + "</xml>";
//		return xml;
//	}

	@Override
	public void handleDebugEvents(DebugEvent[] events) {
		// TODO Auto-generated method stub
		for (int i = 0; i < events.length; i++) {
			DebugEvent event = events[i];
			if (event.getKind() == DebugEvent.TERMINATE) {
				Object source = event.getSource();
				if (source instanceof AddonDevDebugTarget)
				{
					SimpleServer.getInstance().stop();
					fCloseBrowser = true;
					fTerminated = true;					
				}else if (source instanceof IProcess) {
					SimpleServer.getInstance().stop();
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

			}
		}		
	}	
}
