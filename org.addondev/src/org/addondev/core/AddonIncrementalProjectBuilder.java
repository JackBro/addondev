package org.addondev.core;

import java.util.ArrayList;
import java.util.Map;

import org.addondev.editor.xul.preview.XULPreviewForm;
import org.addondev.parser.xul.XULParser;
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

	class IncrementalBuildVisitor implements IResourceDeltaVisitor {

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			// TODO Auto-generated method stub
			IResource resource = delta.getResource();
			switch (delta.getKind()) 
			{
			case IResourceDelta.CHANGED:
				if(resource instanceof IFile )
				{
					IFile file = (IFile)resource;
					
					//getEditorPart(getProject(), file.getLocation());
				}
				break;
			}
			
			return true;
		}
		
	}
	
	
	public static final String BUILDER_ID = "org.addondev.core.AddonIncrementalProjectBuilder";
	
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
//				int kk = delta.getKind();
//				switch (delta.getKind()) {
//				case IResourceDelta.CHANGED:
//					//getEditorPart();
////					IFile file = getProject().getFile("chrome.manifest");
////					IPath fBasePath = file.getLocation().removeLastSegments(1);
////					IPath fBasePath2 = file.getFullPath().removeLastSegments(1);
////					int i=0;
////					i++;
//					//IPath resource = delta.getResource().getFullPath();
//					IResource resource = delta.getResource();
//					if(resource instanceof IFile )
//					{
//						
//						getEditorPart(getProject(), delta.getFullPath());
//					}
//					break;
//
//				default:
//					break;
//				}
			}
		}
		
		return null;
	}
	String ptext;
	@SuppressWarnings("deprecation")
	private void getEditorPart(final IProject project, final IPath path)
	{
		final ArrayList<XULPreviewForm> xulforms = new ArrayList<XULPreviewForm>();
		
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (IWorkbenchWindow iWorkbenchWindow : windows) {
			//iWorkbenchWindow.getActivePage().
			//IEditorReference[] editorref = iWorkbenchWindow.getActivePage().getEditorReferences();
			final IEditorPart editor = iWorkbenchWindow.getActivePage().getActiveEditor();
			
			IEditorPart[] editorref = iWorkbenchWindow.getActivePage().getEditors();
			
			for (IEditorPart editorpart : editorref) {
			//for (IEditorReference iEditorReference : editorref) {
				//IEditorPart editorpart = iEditorReference.getEditor(false);			
				//FileEditorInput fin = (FileEditorInput) editorpart.getEditorInput();
				//fin.getFile().getFullPath()	
				if(editorpart instanceof XULPreviewForm)
				{
					
					IPath editorpath = ((FileEditorInput)editorpart.getEditorInput()).getPath();
					if(editorpath.equals(path))
					{
						xulforms.add((XULPreviewForm) editorpart);
					}
				}
			}
			
			Display.getDefault().asyncExec(new Runnable() {
			//editor.getEditorSite().getShell().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ISelectionProvider provider= editor.getEditorSite().getSelectionProvider();
					ISelection selection = provider.getSelection();
					if (selection instanceof ITextSelection) {
						ITextSelection textSelection= (ITextSelection) selection;
						int offset = textSelection.getOffset();
						String previewxml = XULParser.parse(path, offset);
						ptext = previewxml;
						for (XULPreviewForm xulform : xulforms) {			
							xulform.settest(previewxml);
						}	
					}						
				}
			});	
			
		}
	}
}
