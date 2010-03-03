package org.addondev.ui.editor.xul;

import java.util.HashMap;
import java.util.Map;

import jp.aonir.fuzzyxml.event.FuzzyXMLErrorEvent;
import jp.aonir.fuzzyxml.event.FuzzyXMLErrorListener;

import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.editor.xml.XMLEditor;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.addondev.util.Locale;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.MarkerUtilities;

public class XULEditor extends XMLEditor implements FuzzyXMLErrorListener {

	//private ColorManager colorManager;
	//private XULOutlinePage outline;

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
		
		final Job job = new Job("start") {
			
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
				if(curtime - lasttime >= 2000)
				{
					if(job.getState() != Job.RUNNING)
					{
						job.schedule(1500);
					}
				}
				else
				{
					if(job.getState() == Job.WAITING || job.getState() == Job.SLEEPING)
					{
						job.cancel();
						job.schedule(1500);
					}
				}
				
				lasttime = System.currentTimeMillis();	
			}
			
			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
				// TODO Auto-generated method stub
				
			}
		});

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
		//getEditorPart(project, ((FileEditorInput)in).getPath());
		
		String strlocale = null;
		Locale locale = null;
		try {
			strlocale = project.getPersistentProperty(new QualifiedName(AddonDevUIPrefConst.LOCALE , "LOCALE"));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(strlocale != null)
		{
			locale = Locale.getLocale(strlocale);
		}
		if(locale == null)
		{
			IStatus status = new Status(IStatus.ERROR, AddonDevUIPlugin.PLUGIN_ID, 
					IStatus.OK, "メッセージ１", new Exception(
			        "エラーメッセージ１"));
			Shell shell = getSite().getWorkbenchWindow().getShell();
			ErrorDialog.openError(shell, null, null, status);
			return;
		}
				
		IDocument document = getSourceViewer().getDocument();
		HashMap<Integer, Integer> errormap = XULChecker.checkEntity(project, document, locale.getName());
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
	
	public String getText()
	{
		return getSourceViewer().getDocument().get();
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
	
//	private void getEditorPart(final IProject project, final IPath path)
//	{
//		final ArrayList<XULPreviewForm> xulforms = new ArrayList<XULPreviewForm>();
//		
//		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
//		for (IWorkbenchWindow iWorkbenchWindow : windows) {
//			//iWorkbenchWindow.getActivePage().
//			//IEditorReference[] editorref = iWorkbenchWindow.getActivePage().getEditorReferences();
//			final IEditorPart editor = iWorkbenchWindow.getActivePage().getActiveEditor();
//			
//			IEditorPart[] editorref = iWorkbenchWindow.getActivePage().getEditors();
//			
//			for (IEditorPart editorpart : editorref) {
//			//for (IEditorReference iEditorReference : editorref) {
//				//IEditorPart editorpart = iEditorReference.getEditor(false);			
//				//FileEditorInput fin = (FileEditorInput) editorpart.getEditorInput();
//				//fin.getFile().getFullPath()	
//				if(editorpart instanceof XULPreviewForm)
//				{
//					FileEditorInput fileinput = (FileEditorInput)editorpart.getEditorInput();
//					IPath editorpath = fileinput.getPath();
//					if(editorpath.equals(path))
//					{
//						try {
//							String text = FileUtil.getContent(fileinput.getFile().getContents());
//							((XULPreviewForm) editorpart).setDocument(text);
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (CoreException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						//xulforms.add((XULPreviewForm) editorpart);
//					}
//				}
//			}
//			
////			Display.getDefault().asyncExec(new Runnable() {
////			//editor.getEditorSite().getShell().getDisplay().asyncExec(new Runnable() {
////				@Override
////				public void run() {
////					// TODO Auto-generated method stub
////					ISelectionProvider provider= editor.getEditorSite().getSelectionProvider();
////					ISelection selection = provider.getSelection();
////					if (selection instanceof ITextSelection) {
////						ITextSelection textSelection= (ITextSelection) selection;
////						int offset = textSelection.getOffset();
//////						String previewxml = XULParser.parse(path, offset);
//////						for (XULPreviewForm xulform : xulforms) {			
//////							xulform.settest(previewxml);
//////						}	
////					}						
////				}
////			});	
//			
//		}
//	}

	public void Validator()
	{
//		FuzzyXMLParser parser = new FuzzyXMLParser();
//		parser.addErrorListener(this);	
//		FuzzyXMLDocument document = parser.parse(text);
	}
	
	@Override
	public void error(FuzzyXMLErrorEvent event) {
		// TODO Auto-generated method stub
		
	}
}
