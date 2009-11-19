package jp.addondev.debug.ui.model;

import java.util.Map;

import jp.addondev.debug.core.model.AddonDebugTarget;
import jp.addondev.debug.core.model.JSLineBreakpoint;
import jp.addondev.debug.core.model.JSStackFrame;
import jp.addondev.debug.core.model.JSThread;
import jp.addondev.plugin.AddonDevPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IWatchExpression;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.IValueDetailListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;


public class JSDebugModelPresentation  implements IDebugModelPresentation {

	static public String ADDON_DEBUG_MODEL_ID = "jp.addondev.debug";
	
	 protected ListenerList fListeners = new ListenerList(ListenerList.IDENTITY);
	 
	 protected boolean displayVariableTypeNames = false; // variables display attribute
	
	@Override
	public void computeDetail(IValue value, IValueDetailListener listener) {
		// TODO Auto-generated method stub
		String detail = "";
		try {
			detail = value.getValueString();
		} catch (DebugException e) {
		}
		listener.detailComputed(value, detail);
	}

	@Override
	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		//return null;
		 if (element instanceof JSLineBreakpoint) {
			 
			 JSLineBreakpoint breakpoint = (JSLineBreakpoint)element;
			 try {
				if(breakpoint.isEnabled())
				 { 
					return AddonDevPlugin.getDefault().getImage(AddonDevPlugin.getDefault().IMG_BP_ENABLE);
				 }
				 else
				 {
					 return AddonDevPlugin.getDefault().getImage(AddonDevPlugin.getDefault().IMG_BP_DISABLE);
				 }
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 //return AddonDevPlugin.getDefault().getImage("icons/bp_enable.png");
			 //return null;
			 //return JavaScriptDebugPlugin.getDefault().getImageRegistry().getDescriptor(JavaScriptDebugPlugin.IMG_BP_ENABLE).createImage();
		 }
		 return null;
	}

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		//return null;
        if (element instanceof JSLineBreakpoint) {
        	JSLineBreakpoint jsBreakpoint = (JSLineBreakpoint) element;
            IMarker marker = ((JSLineBreakpoint) element).getMarker();
            try {
                Map attrs = marker.getAttributes();
                //attrs.
                //get the filename
//                String ioFile = pyBreakpoint.getFile();
//                String fileName = "unknown";
//                if(ioFile != null){
//                    File file = new File(ioFile);
//                    fileName = file.getName();
//                }
//                String path = jsBreakpoint.getFullPath();
//                File file = new File(path);
//                String fileName = file.getName();
                String fileName = jsBreakpoint.getName();
                
                //get the line number
                Object lineNumber = attrs.get(IMarker.LINE_NUMBER);
                //String functionName = pyBreakpoint.getFunctionName();
                
                if (lineNumber == null){
                    lineNumber = "unknown";
                }
                
                //get the location
                String location = fileName + ":" + lineNumber.toString();
                return location;        
            } catch (CoreException e) {
                //PydevDebugPlugin.log(IStatus.ERROR, "error retreiving marker attributes", e);
                return "error";
            }
        } else if (element instanceof AddonDebugTarget || element instanceof JSStackFrame){// || element instanceof JSThread) {
            return null; // defaults work
            
        } else if (element instanceof JSThread ) {
        	JSThread th = (JSThread) element;
        	if(th.isTerminated()) //debugger close
        	{
        		try {
					th.terminate();
				} catch (DebugException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		int i=0;
        		i++;
        	}
           return null; // defaults are fine
           
        } else if (element instanceof IWatchExpression) {
            try {
                IWatchExpression watch_expression = (IWatchExpression) element;
                IValue value = watch_expression.getValue();
                if (value != null) {
                    return "\"" + watch_expression.getExpressionText() + "\"= " + value.getValueString();
                } else {
                    return null;
                }
            } catch (DebugException e) {
                return null;
            }
            
        }else if(element == null){
            //PydevDebugPlugin.log(IStatus.ERROR, "PyDebugModelPresentation: element == null", null);
            return null;
            
        }else{
            //PydevDebugPlugin.log(IStatus.ERROR, "PyDebugModelPresentation:\nclass not expected for presentation:"+element.getClass()+"\n(returning default presentation).", null);
            return null;
        }
	}

	@Override
	public void setAttribute(String attribute, Object value) {
		// TODO Auto-generated method stub
        if (attribute.equals(IDebugModelPresentation.DISPLAY_VARIABLE_TYPE_NAMES)){
            displayVariableTypeNames = ((Boolean) value).booleanValue();
        }else{
            System.err.println("setattribute");
        }
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		fListeners.remove(listener);
	}

	@Override
	public String getEditorId(IEditorInput input, Object element) {
		// TODO Auto-generated method stub
//		if (element instanceof IFile || element instanceof ILineBreakpoint) {
//			//return "org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaEditor";
//			return "org.eclipse.wst.jsdt.ui.CompilationUnitEditor";
//		}
		return null;
	}

	@Override
	public IEditorInput getEditorInput(Object element) {
		// TODO Auto-generated method stub
		if (element instanceof IFile) {
			return new FileEditorInput((IFile)element);
		}
		if (element instanceof ILineBreakpoint) {
			return new FileEditorInput((IFile)((ILineBreakpoint)element).getMarker().getResource());
		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		 fListeners.add(listener);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
