package org.addondev.editor.xul;

import java.util.ArrayList;

import org.addondev.editor.xml.XMLEditor;
import org.addondev.editor.xul.preview.XULPreviewForm;
import org.addondev.parser.xul.XULParser;
import org.addondev.plugin.AddonDevPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class XULEditor extends XMLEditor {

	//private ColorManager colorManager;
	private XULOutlinePage outline;

	public XULEditor() {
		super();
		//colorManager = new ColorManager();
		//setSourceViewerConfiguration(new XULConfiguration(colorManager));
		//setDocumentProvider(new XULDocumentProvider());
	}
	public void dispose() {
		//colorManager.dispose();
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
						for (XULPreviewForm xulform : xulforms) {			
							xulform.settest(previewxml);
						}	
					}						
				}
			});	
			
		}
	}
	

}
