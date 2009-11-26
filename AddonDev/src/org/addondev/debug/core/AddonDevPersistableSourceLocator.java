package org.addondev.debug.core;

import org.addondev.debug.core.model.JSStackFrame;
import org.addondev.editor.javascript.JavaScriptEditor;
import org.addondev.plugin.AddonDevPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IPersistableSourceLocator;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.ui.ISourcePresentation;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;

public class AddonDevPersistableSourceLocator implements
		IPersistableSourceLocator, ISourcePresentation {

	@Override
	public String getMemento() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initializeDefaults(ILaunchConfiguration configuration)
			throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void initializeFromMemento(String memento) throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getSourceElement(IStackFrame stackFrame) {
		// TODO Auto-generated method stub
		//return null;
		String filefullpath = ((JSStackFrame)stackFrame).getFileFullPath();
		return filefullpath;
	}

	@Override
	public String getEditorId(IEditorInput input, Object element) {
		// TODO Auto-generated method stub
		//return null;
		return JavaScriptEditor.ID;
	}

	@Override
	public IEditorInput getEditorInput(Object element) {
		// TODO Auto-generated method stub
		//return null;
		String filefullpath = (String)element;
		IWorkspaceRoot root = AddonDevPlugin.getWorkspace().getRoot();
		IPath path = new Path(filefullpath);
		//IPath path = new Path(filefullpath);
		//Path.fromPortableString(filefullpath);
		IFile file = root.getFileForLocation(path);
		//file.
		//IFile file = root.getFileForLocation(path);
		
		if(file != null && file.exists())
		//if(file != null)
		{
			return new FileEditorInput(file);
		}
		else
		{
			return null;
		}
	}

}
