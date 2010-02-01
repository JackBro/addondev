package org.addondev.debug.core;

import java.io.File;
import java.io.IOException;

import org.addondev.debug.core.model.AddonDevStackFrame;
import org.addondev.editor.javascript.JavaScriptEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IPersistableSourceLocator;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.sourcelookup.containers.LocalFileStorage;
import org.eclipse.debug.ui.ISourcePresentation;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;

public class AddonDevPersistableSourceLocator implements
		IPersistableSourceLocator, ISourcePresentation {
	
	private File dir;

	@Override
	public String getMemento() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initializeDefaults(ILaunchConfiguration configuration)
			throws CoreException {
		// TODO Auto-generated method stub
		IPath path = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		dir = path.append("tmp").toFile();
		if(!dir.exists())
		{
			dir.mkdir();
		}
	}

	@Override
	public void initializeFromMemento(String memento) throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getSourceElement(IStackFrame stackFrame) {
		// TODO Auto-generated method stub
		//return null;
		//String filefullpath = ((JSStackFrame)stackFrame).getFileFullPath();
		//return filefullpath;
		return stackFrame;
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
		AddonDevStackFrame frame = (AddonDevStackFrame)element;
		
		String filefullpath = frame.getFileFullPath();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
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
//			//fLaunchConfiguration.
			SeqEditorInput f = new SeqEditorInput(dir, filefullpath, frame.getURL(), frame.getFn());
			try {
				f.createFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//IPath tmppath = new Path(dir.getAbsolutePath()).append(f.getName());
			//IFile tmpfile = root.getFileForLocation(tmppath);
			File ff = new File(dir, f.getName());
//			return new FileEditorInput(tmpfile);
//			//return new SeqEditorInput(filefullpath, frame.getURL(), frame.getFn());
			
			LocalFileStorage lfile = new LocalFileStorage(ff);
			//filePath = ((LocalFileStorage) element).getFullPath();
			return new SeqStorageEditorInput(lfile);

		}
	}

}
