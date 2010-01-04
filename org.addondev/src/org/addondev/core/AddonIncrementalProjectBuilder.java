package org.addondev.core;

import java.util.ArrayList;
import java.util.Map;

import org.addondev.editor.xul.formeditor.XULFormEditor;
import org.addondev.parser.xul.XULParser;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class AddonIncrementalProjectBuilder extends IncrementalProjectBuilder {

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
				//delta.getResource().getName()
				int kk = delta.getKind();
				switch (delta.getKind()) {
				case IResourceDelta.CHANGED:
					//getEditorPart();
					IFile file = getProject().getFile("chrome.manifest");
					IPath fBasePath = file.getLocation().removeLastSegments(1);
					IPath fBasePath2 = file.getFullPath().removeLastSegments(1);
					int i=0;
					i++;
					
					getEditorPart(getProject(), delta.getFullPath());
					break;

				default:
					break;
				}
			}
		}
		
		return null;
	}


	
	@SuppressWarnings("deprecation")
	private void getEditorPart(final IProject project, final IPath path)
	{
		final ArrayList<XULFormEditor> xulforms = new ArrayList<XULFormEditor>();
		
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
				if(editorpart instanceof XULFormEditor)
				{
					
					IPath editorpath = ((FileEditorInput)editorpart.getEditorInput()).getPath();
					if(editorpath.equals(path))
					{
						xulforms.add((XULFormEditor) editorpart);
					}
					
				}


//				if(editorpart instanceof XULFormEditor)
//				{
//					XULFormEditor xulformeditor = (XULFormEditor)editorpart;
//					String text = getSourceViewer().getDocument().get();
//					IEditorInput editorinput = getEditorInput();
//					
//					IEditorInput formeditorinput = xulformeditor.getEditorInput();
//					
//					if(editorinput instanceof FileEditorInput
//							&& formeditorinput instanceof FileEditorInput)
//					{
//						String editorpath = ((FileEditorInput)editorinput).getPath().toPortableString();
//						String formeditorpath = ((FileEditorInput)formeditorinput).getPath().toPortableString();
//						if(editorpath != null && formeditorpath != null
//								&& editorpath.equals(formeditorpath))
//						{
//							xulformeditor.settest(xml);
//						}
//					}
//					
////					if(input instanceof IFileEditorInput)
////					{
////						xulformeditor.setFile(((IFileEditorInput)input).getFile());
////					}
//					//xulformeditor.settest(text);
//					//xulformeditor.setFile(file);
//					//xulformeditor.settest(xml);
////					BrowserFormPage formpage = (BrowserFormPage)xulformeditor.setActivePage(BrowserFormPage.ID);
////					
////					String text = getSourceViewer().getDocument().get();
////					formpage.setDocument(text);
////					
////					IEditorInput input = getEditorInput();
////					if(input instanceof IFileEditorInput)
////					{
////						
////						formpage.setFile(((IFileEditorInput)input).getFile());
////					}
//					//iWorkbenchWindow.getActivePage().findView(viewId)
//				}
			}
			
			//
			editor.getEditorSite().getShell().getDisplay().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ISelectionProvider provider= editor.getEditorSite().getSelectionProvider();
					ISelection selection = provider.getSelection();
					if (selection instanceof ITextSelection) {
						ITextSelection textSelection= (ITextSelection) selection;
						int offset = textSelection.getOffset();
						int i=0;
						i++;
						for (XULFormEditor xulform : xulforms) {
							
							XULParser xulp = new XULParser(project, "en-US");
							String previewxml = xulp.parse(path, offset);
							
						}
					}						
				}
			});			
		}
		
	}
	private void build()
	{
		
	}
}
