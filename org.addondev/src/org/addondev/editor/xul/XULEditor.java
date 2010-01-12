package org.addondev.editor.xul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.addondev.editor.xml.XMLEditor;
import org.addondev.editor.xul.preview.XULPreviewForm;
import org.addondev.parser.xul.XULParser;
import org.addondev.plugin.AddonDevPlugin;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IDocumentPartitioningListener;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.MarkerUtilities;
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
	
	private long lasttime = 0;
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		super.createPartControl(parent);
		
		final Job job = new Job("お仕事開始") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO Auto-generated method stub
				System.out.println("job");
				return Status.OK_STATUS;
			}
		};

		
		
		ISourceViewer sourceViewer = getSourceViewer();
		IDocument doc = sourceViewer.getDocument();
		doc.addDocumentListener(new IDocumentListener() {
			
			@Override
			public void documentChanged(DocumentEvent event) {
				// TODO Auto-generated method stub
				long curtime = System.currentTimeMillis();
				if(curtime - lasttime >= 800)
				{
					job.schedule(500);
				}
				else
				{
					job.cancel();
					job.schedule(500);
				}
				
				lasttime = System.currentTimeMillis();	
			}
			
			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
	}


//	@Override
//	protected void doSetInput(IEditorInput input) throws CoreException {
//		// TODO Auto-generated method stub
//		super.doSetInput(input);
//		
//
//	}



	@Override
	protected ISourceViewer createSourceViewer(Composite parent,
			IVerticalRuler ruler, int styles) {
		// TODO Auto-generated method stub
		//ISourceViewer sourceViewer =  super.createSourceViewer(parent, ruler, styles);
//		
//		final Job job = new Job("お仕事開始") {
//		
//					@Override
//					protected IStatus run(IProgressMonitor monitor) {
//						// TODO Auto-generated method stub
//						System.out.println("job");
//						return Status.OK_STATUS;
//					}
//				};
		
//		StyledText textWidget = sourceViewer.getTextWidget();
//		textWidget.addKeyListener(new KeyListener() {
//			
//			@Override
//			public void keyReleased(KeyEvent e) {
//				// TODO Auto-generated method stub
//				//System.out.println("keyReleased");
//				long curtime = System.currentTimeMillis();
//				if(curtime - lasttime >= 800)
//				{
//					job.schedule(500);
//				}
//				else
//				{
//					job.cancel();
//					job.schedule(500);
//				}
//				
//				lasttime = System.currentTimeMillis();
//			}
//			
//			@Override
//			public void keyPressed(KeyEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		return super.createSourceViewer(parent, ruler, styles);
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
		IProject project = ((FileEditorInput)in).getFile().getProject();
		getEditorPart(project, ((FileEditorInput)in).getPath());
		
		IDocument document = getSourceViewer().getDocument();
		HashMap<Integer, Integer> errormap = XULParser.checkEntity(project, document, "en-US");
		IResource resource = (IResource)in.getAdapter(IResource.class);
		
		try {
			resource.deleteMarkers(IMarker.PROBLEM, false, 0);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		for (Integer start : errormap.keySet()) {
			int len = errormap.get(start);
			int line = -1;
			int startoffset = 0;
			try {

				line = document.getLineOfOffset(start)+1;
				//startoffset = document.getLineOffset(line);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put(IMarker.LINE_NUMBER, line);
			map.put(IMarker.CHAR_START, start);
			map.put(IMarker.CHAR_END, start + len);
			map.put(IMarker.MESSAGE, "entity not find");
			map.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			try {
				MarkerUtilities.createMarker(resource, map, IMarker.PROBLEM);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

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
