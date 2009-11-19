package jp.addondev.debug.ui.actions;

import java.util.HashMap;
import java.util.Map;

import jp.addondev.debug.core.model.JSLineBreakpoint;
import jp.addondev.debug.ui.model.JSDebugModelPresentation;
import jp.addondev.debug.ui.model.JSToggleBreakpointAdapter;
import jp.addondev.plugin.AddonDevPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.IUpdate;


public class ToggleBreakpointRulerAction extends Action {

	private IVerticalRulerInfo fRuler;
	private ITextEditor fEditor;
	
	public ToggleBreakpointRulerAction(ITextEditor editor,
			IVerticalRulerInfo rulerInfo) throws CoreException {
		// TODO Auto-generated constructor stub
		fEditor = editor;
		fRuler = rulerInfo;
	}
	
	public void run() {
		
		addMarker();
		
//		int line = fRuler.getLineOfLastMouseButtonActivity()+1;
//		IEditorInput editorInput = fTargetPart.getEditorInput();
//		IResource resource;
//		resource = ((IFileEditorInput) editorInput).getFile();
//		
//		//IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints("org.eclipse.debug.core.breakpointMarker");
//		//IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(AddonDevPlugin.ID_DEBUG_MODEL);
//		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(JSDebugModelPresentation.ADDON_DEBUG_MODEL_ID);
//		for (int i = 0; i < breakpoints.length; i++) {
//			IBreakpoint breakpoint = breakpoints[i];
//			if (resource.equals(breakpoint.getMarker().getResource())) {
//				try {
//					if (((ILineBreakpoint)breakpoint).getLineNumber() == (line)) {
//						//DebugPlugin.getDefault().getBreakpointManager().removeBreakpoint(breakpoint, true);
//						breakpoint.delete();
//						return;
//					}
//				} catch (CoreException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		try {
//			JSLineBreakpoint b = new JSLineBreakpoint(resource, line);
//			DebugPlugin.getDefault().getBreakpointManager().addBreakpoint(b);
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
	}
    public static IResource getResourceForDebugMarkers(ITextEditor textEditor) {
        IEditorInput input= textEditor.getEditorInput();
        IResource resource= (IResource) input.getAdapter(IFile.class);
        if (resource == null) {
            resource= (IResource) input.getAdapter(IResource.class);
        }        
        if(resource == null){
            resource = ResourcesPlugin.getWorkspace().getRoot();
        }
        return resource;
    }	
	protected void addMarker() {
		int line = fRuler.getLineOfLastMouseButtonActivity()+1;
		//IEditorInput editorInput = fEditor.getEditorInput();
		//final IResource resource= (IResource) editorInput.getAdapter(IFile.class);
		final IResource resource= getResourceForDebugMarkers(fEditor);

		
		//IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints("org.eclipse.debug.core.breakpointMarker");
		//IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(AddonDevPlugin.ID_DEBUG_MODEL);
		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(JSDebugModelPresentation.ADDON_DEBUG_MODEL_ID);
		for (int i = 0; i < breakpoints.length; i++) {
			IBreakpoint breakpoint = breakpoints[i];
			if (resource.equals(breakpoint.getMarker().getResource())) {
				try {
					if (((ILineBreakpoint)breakpoint).getLineNumber() == (line)) {
						//DebugPlugin.getDefault().getBreakpointManager().removeBreakpoint(breakpoint, true);
						breakpoint.delete();
						return;
					}
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
//		try {
//			JSLineBreakpoint b = new JSLineBreakpoint(resource, line);
//			DebugPlugin.getDefault().getBreakpointManager().addBreakpoint(b);
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
		
		//IMarker marker = resource.createMarker("org.eclipse.debug.core.breakpointMarker");
		final Map<String, Object> map = new HashMap<String, Object>();
		
		map.put(IBreakpoint.ENABLED, Boolean.TRUE);
		map.put(IMarker.LINE_NUMBER, line);
		//map.put(IBreakpoint.ID, AddonDevPlugin.ID_DEBUG_MODEL);
		map.put(IBreakpoint.ID, JSDebugModelPresentation.ADDON_DEBUG_MODEL_ID);
		//marker.setAttribute(IBreakpoint.ID, "org.eclipse.debug.core.breakpointMarker");
		map.put(IMarker.MESSAGE, "Line Breakpoint: " + resource.getName() + " [line: " + line + "]");	
		
		//String pname = resource.getProject().getName();
		//map.put("projectName", resource.getProject().getName());
		
		//IMarker marker = resource.createMarker(AddonDevPlugin.ID_BREAK_MARKER);
		//IMarker marker = resource.createMarker(ADDON_BREAK_MARKER);
        //marker.setAttributes(map);
        //setMarker(marker);
		
        IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
            public void run(IProgressMonitor monitor) throws CoreException {
                IMarker marker = resource.createMarker(JSLineBreakpoint.ADDON_BREAK_MARKER);
                marker.setAttributes(map);
                JSLineBreakpoint br = new JSLineBreakpoint();
                br.setMarker(marker);
                IBreakpointManager breakpointManager = DebugPlugin.getDefault().getBreakpointManager();
                breakpointManager.addBreakpoint(br);
            }
        };
        try {
			resource.getWorkspace().run(runnable, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
