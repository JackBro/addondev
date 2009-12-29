package org.addondev.debug.core.model;

import java.util.HashMap;
import java.util.Map;


import org.addondev.debug.ui.model.AddonDevDebugModelPresentation;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.LineBreakpoint;

public class AddonDevLineBreakpoint extends LineBreakpoint {

	//static public final String ADDON_BREAK_MARKER = "jp.addondev.debug.addonStopBreakpointMarker";
	static public final String BREAKPOINT_MARKER = "org.addondev.debug.marktype.linebreakpoint";//"org.addondev.debug.markertype.linebreakpoint";
	static public final String BREAKPOINT_ID = "org.addondev.debug.linebreakpoint";
	
	public AddonDevLineBreakpoint()
	{
	}
	public AddonDevLineBreakpoint(final IResource resource, final int lineNumber) throws CoreException {
		 final Map<String, Object> map = new HashMap<String, Object>();
		 
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
//				IMarker marker = resource.createMarker(IJSConstants.ID_JS_BREAK_MARKER);
//				//IMarker marker = resource.createMarker("org.eclipse.debug.core.breakpointMarker");
//				marker.setAttribute(IBreakpoint.ENABLED, Boolean.TRUE);
//				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
//				marker.setAttribute(IBreakpoint.ID, IJSConstants.ID_JS_DEBUG_MODEL);
//				//marker.setAttribute(IBreakpoint.ID, "org.eclipse.debug.core.breakpointMarker");
//				marker.setAttribute(IMarker.MESSAGE, "Line Breakpoint: " + resource.getName() + " [line: " + lineNumber + "]");
//				setMarker(marker);
				
				//IMarker marker = resource.createMarker("org.eclipse.debug.core.breakpointMarker");

				map.put(IBreakpoint.ENABLED, Boolean.TRUE);
				map.put(IMarker.LINE_NUMBER, lineNumber);
				//map.put(IBreakpoint.ID, AddonDevPlugin.ID_DEBUG_MODEL);
				map.put(IBreakpoint.ID, BREAKPOINT_ID);
				//marker.setAttribute(IBreakpoint.ID, "org.eclipse.debug.core.breakpointMarker");
				map.put(IMarker.MESSAGE, "Line Breakpoint: " + resource.getName() + " [line: " + lineNumber + "]");	
				
				//String pname = resource.getProject().getName();
				//map.put("projectName", resource.getProject().getName());
				
				//IMarker marker = resource.createMarker(AddonDevPlugin.ID_BREAK_MARKER);
				IMarker marker = resource.createMarker(BREAKPOINT_MARKER);
                marker.setAttributes(map);
                setMarker(marker);
			}
		};
		//run(getMarkerRule(resource), runnable);
		resource.getWorkspace().run(runnable, null);
	}
	
	@Override
	public String getModelIdentifier() {
		// TODO Auto-generated method stub
		//return null;
		//return AddonDevPlugin.ID_DEBUG_MODEL;
		return AddonDevDebugModelPresentation.DEBUG_MODEL_ID;
		//return "org.eclipse.debug.core.breakpointMarker";
	}
	
//    protected IMarker ensureMarker() {
////        IMarker m = getMarker();
////        if (m == null || !m.exists()) {
////            throw new DebugException(new Status(IStatus.ERROR, DebugPlugin.getUniqueIdentifier(), DebugException.REQUEST_FAILED,
////                "Breakpoint_no_associated_marker", null));
////        }
////        return m;
//    	 IMarker m = getMarker();
//    	 return m;
//    }
	
	public IPath getPath()
	{
		//IPath /helloworld-0.0.6/chrome/content/helloworld/helloWorld.js
		//IPath pth = getMarker().getResource().getFullPath();
		return getMarker().getResource().getFullPath();
		
		//String path = getMarker().getResource().getFullPath().toOSString();
		//String path = getMarker().getResource().getLocationURI().getPath();	
		//return path;
	}
	
	public IPath getFullPath()
	{
		//IPath /helloworld-0.0.6/chrome/content/helloworld/helloWorld.js
		//IPath pth = getMarker().getResource().getFullPath();
		//return getMarker().getResource().getFullPath();
//		String url ="";
//		try {
//			url = getMarker().getResource().getLocationURI().toURL().toString();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//String path = getMarker().getResource().getFullPath().toOSString();
		//String path2 = getMarker().getResource().getLocationURI().getPath();
		IPath path = getMarker().getResource().getLocation();
		//String path4 = path3.toPortableString();
		//String path5 = path3.toString();
		//String path6 = path3.toOSString();
		return path;
	}
	
	public String getName()
	{
		return getMarker().getResource().getName();
	}
	
	public IProject getProject() {
		return getMarker().getResource().getProject();
	}

}
