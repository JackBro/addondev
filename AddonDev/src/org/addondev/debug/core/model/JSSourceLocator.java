package org.addondev.debug.core.model;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.ui.ISourcePresentation;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class JSSourceLocator implements ISourceLocator, ISourcePresentation {

	@Override
	public Object getSourceElement(IStackFrame stackFrame) {
		// TODO Auto-generated method stub
		return stackFrame;
		//String filename = ((JSStackFrame) stackFrame).getSourceName();
		//return filename;
	}

	@Override
	public String getEditorId(IEditorInput input, Object element) {
		// TODO Auto-generated method stub
		//Object oo = element;
		String path = ((JSStackFrame)element).getSourceName();
		//String path ="D:/data/src/PDE/eclipsePDE/runtime-EclipseApplication/webtest/WebContent/index.html";
		String id = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(path).getId();
		return id;
		//return PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor((String) element).getId();
		//return "org.eclipse.wst.jsdt.ui.CompilationUnitEditor";
		//return "org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaEditor";
	}

	@Override
	public IEditorInput getEditorInput(Object element) {
		// TODO Auto-generated method stub
		IPath path = new Path(((JSStackFrame)element).getSourceName());	
		//IPath path = new Path("C:/workspace/src/PDE/eclipsePDE/runtime-EclipseApplication/test0/test.js");	
		IWorkspace w = ResourcesPlugin.getWorkspace();      
        IFile fileForLocation = w.getRoot().getFileForLocation(path);
        if(fileForLocation != null && fileForLocation.exists()){
            return new FileEditorInput(fileForLocation);
        }
		
		return null;
	}

}
