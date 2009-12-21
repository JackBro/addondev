package org.addondev.editor.xul;

import jp.aonir.fuzzyxml.FuzzyXMLNode;

import org.addondev.editor.xul.formeditor.BrowserFormPage;
import org.addondev.editor.xul.formeditor.XULFormEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
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
		
		IStructuredSelection sel = (IStructuredSelection)outline.getSelection();
		Object element = sel.getFirstElement();
		 if(element instanceof FuzzyXMLNode)
		 {
			 
		 }
		
		getXUL();
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
	private void getXUL()
	{
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (IWorkbenchWindow iWorkbenchWindow : windows) {
			//iWorkbenchWindow.getActivePage().
			//IEditorReference[] editorref = iWorkbenchWindow.getActivePage().getEditorReferences();
			IEditorPart[] editorref = iWorkbenchWindow.getActivePage().getEditors();
			for (IEditorPart editorpart : editorref) {
			//for (IEditorReference iEditorReference : editorref) {
				//IEditorPart editorpart = iEditorReference.getEditor(false);
				if(editorpart instanceof XULFormEditor)
				{
					XULFormEditor xulformeditor = (XULFormEditor)editorpart;
					String text = getSourceViewer().getDocument().get();
					IEditorInput input = getEditorInput();
//					if(input instanceof IFileEditorInput)
//					{
//						xulformeditor.setFile(((IFileEditorInput)input).getFile());
//					}
					xulformeditor.settest(text);
//					BrowserFormPage formpage = (BrowserFormPage)xulformeditor.setActivePage(BrowserFormPage.ID);
//					
//					String text = getSourceViewer().getDocument().get();
//					formpage.setDocument(text);
//					
//					IEditorInput input = getEditorInput();
//					if(input instanceof IFileEditorInput)
//					{
//						
//						formpage.setFile(((IFileEditorInput)input).getFile());
//					}
					//iWorkbenchWindow.getActivePage().findView(viewId)
				}
			}
		}

	}
}
