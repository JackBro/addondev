package org.addondev.editor.xul;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import jp.aonir.fuzzyxml.FuzzyXMLNode;

import org.addondev.editor.xul.formeditor.BrowserFormPage;
import org.addondev.editor.xul.formeditor.XULFormEditor;
import org.addondev.parser.xul.XULParser;
import org.addondev.plugin.AddonDevPlugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class XULEditor extends TextEditor {

	private ColorManager colorManager;
	private XULOutlinePage outline;

	public XULEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XULConfiguration(colorManager));
		setDocumentProvider(new XULDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		// TODO Auto-generated method stub
		super.doSave(progressMonitor);
		IEditorInput in = getEditorInput();
		IProject pro = ((FileEditorInput)in).getFile().getProject();
		getEditorPart(pro, ((FileEditorInput)in).getPath());
		
//		if(outline!=null)
//		{
//
//			ISelection editorsel = getSelectionProvider().getSelection();
//			ITextSelection textSelection = (ITextSelection)editorsel;
//			int offset = textSelection.getOffset();
//			
//			outline.update(offset);
//						
////			IStructuredSelection sel = (IStructuredSelection)outline.getSelection();
////			
////			Object element = sel.getFirstElement();
////			 if(element instanceof FuzzyXMLNode)
////			 {
////				 String text = ((FuzzyXMLNode)element).toXMLString();
////				 //File file = getXULTmpFile("tmp.xul");
////				 
////				 getXUL(outline.getPreviewElementXML()); 
////			 }
//		}
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		super.doSaveAs();
	}
	
	
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		if (IContentOutlinePage.class.equals(adapter)) 
		{
			if (outline == null) 
			{
				outline = new XULOutlinePage(this);
			}
			return outline;
		}
		return super.getAdapter(adapter);
	}
	
	public int getl(int offset)
	{
		int line = -1;
		try {
			line = getSourceViewer().getDocument().getLineOfOffset(offset);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return line;
	}

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
						for (XULFormEditor xulform : xulforms) {			
							xulform.settest(previewxml);
						}	
					}						
				}
			});	
			
		}
	}
	

}
