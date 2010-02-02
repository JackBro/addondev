package org.addondev.core;

import java.util.List;
import java.util.Map;

import org.addondev.builder.IAddonDevBuilder;
import org.addondev.editor.IAddonDevEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class AddonIncrementalProjectBuilder extends IncrementalProjectBuilder {
	
	public static final String BUILDER_ID = "org.addondev.core.AddonIncrementalProjectBuilder";
	
	class IncrementalBuildVisitor implements IResourceDeltaVisitor {

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			// TODO Auto-generated method stub
			IResource resource = delta.getResource();
			
			List<IAddonDevBuilder> visitors = ExtensionLoader.getExtensions(ExtensionLoader.EXTENSION_POINT_ID);
			for (IAddonDevBuilder iAddonDevBuilder : visitors) {
				iAddonDevBuilder.visit(delta);
			}
			
//			switch (delta.getKind()) 
//			{
//			case IResourceDelta.CHANGED:
//				if(resource instanceof IFile )
//				{
//					IFile file = (IFile)resource;
//					int i=0;
//					getEditorPart(getProject(), file);
//				}
//				break;
//			}
			
			return true;
		}
		
		private void checkFile(IResource resource)
		{
			if (resource instanceof IFile && resource.getName().equals("chrome.manifest")) 
			{
				
			}
		}
		
	}
	

	
	public AddonIncrementalProjectBuilder() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		// TODO Auto-generated method stub
		if(kind != FULL_BUILD)
		{
			IResourceDelta delta = getDelta(getProject());
			
			if(delta != null)
			{
				delta.accept(new IncrementalBuildVisitor());
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("deprecation")
	private void getEditorPart(final IProject project, final IFile file)
	{
		//final ArrayList<XULPreviewForm> xulforms = new ArrayList<XULPreviewForm>();
		
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (IWorkbenchWindow iWorkbenchWindow : windows) {
			//final IEditorPart editor = iWorkbenchWindow.getActivePage().getActiveEditor();		
			IEditorPart[] editors = iWorkbenchWindow.getActivePage().getEditors();	
			for (final IEditorPart editor : editors) {
				if(editor instanceof IAddonDevEditor)
				{
					IProject editorproject = ((FileEditorInput)editor.getEditorInput()).getFile().getProject();
					if(editorproject != null && editorproject.getName().equals(project.getName()))
					{
						
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub	
								((IAddonDevEditor)editor).Changed(file);
							}
						});
					}
				}
			}
		}
	}
}
