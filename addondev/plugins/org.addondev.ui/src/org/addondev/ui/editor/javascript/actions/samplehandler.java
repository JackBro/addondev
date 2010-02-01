package org.addondev.ui.editor.javascript.actions;

//import jp.addondev.debug.ui.PseudoSplitView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.jface.dialogs.Dialog;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.texteditor.ITextEditor;

public class samplehandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IEditorPart ep = HandlerUtil.getActiveEditor(event);
		//ep.
		ITextEditor js  = (ITextEditor)ep;
		IDocument document = js.getDocumentProvider().getDocument(js.getEditorInput());
		String ss = ep.getEditorInput().getName();

		//event.getCommand().
		//JavaScriptEditor dd = (JavaScriptEditor)ep;
		

		//org.eclipse.jface.window.ApplicationWindow
		
//		Dialog d = new Dialog(ep.getSite().getShell(), SWT.DIALOG_TRIM | SWT.MODELESS) {
//			@Override
//			public void setText(String string) {
//				// TODO Auto-generated method stub
//				super.setText("string");
//			}
//			
//			 public void open() {
//				 final Shell shell = new Shell(getParent(), getStyle());
//			      shell.setText("検索");
//			      shell.pack();
//			      shell.open();
//			      Display display = getParent().getDisplay();
//			      while (!shell.isDisposed()) {
//			         if (!display.readAndDispatch()) {
//			            display.sleep();
//			         }
//			      }
//
//			 }
//		};

//		Shell shell = new Shell();
////		MyDialog dialog = new MyDialog(shell);
//////
////		if(dialog.open() == IDialogConstants.OK_ID){
////			//ダイアログをＯＫを押して閉じたときの処理
////		}
//		//shell.getParent()
//		
//		IViewPart previewerPart = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().
//		findView("jp.addondev.debug.ui.PseudoSplitView");
//		if(previewerPart != null)
//		{
//		PseudoSplitView previewer = (PseudoSplitView)previewerPart.getAdapter(PseudoSplitView.class);
//		
//		previewer.setDoc(document);	
//		
//		IWorkbenchPage page = previewer.getViewSite().getPage();
//		
//		if(page.isPartVisible(previewer))
//		{
//			page.hideView(previewer); 
//		}
//		else
//		{
//			IWorkbenchWindow window = HandlerUtil
//			.getActiveWorkbenchWindowChecked(event);
//			
//			//IWorkbenchWindow activeWorkbenchWindow = WorkbenchPlugin.getDefault().getViewRegistry();
//			IWorkbenchPage activePage = window.getActivePage();
//			try {
//				//previewer.get
//				activePage.showView("jp.addondev.debug.ui.PseudoSplitView");
//			} catch (PartInitException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			//previewer.getViewSite().getPage().
////			try {
////				page.showView("jp.addondev.debug.ui.PseudoSplitView");//previewer.getViewSite().getId());
////			} catch (PartInitException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} 
//		}
//		//previewer.getViewSite().getPage().hideView(previewer); //getActionBars().getToolBarManager().getItems()[0].
//		
//		}
//		else
//		{
//			IWorkbenchWindow window = HandlerUtil
//			.getActiveWorkbenchWindowChecked(event);
//			
//			//IWorkbenchWindow activeWorkbenchWindow = WorkbenchPlugin.getDefault().getViewRegistry();
//			IWorkbenchPage activePage = window.getActivePage();
//			try {
//				//previewer.get
//				PseudoSplitView previewer = (PseudoSplitView) activePage.showView("jp.addondev.debug.ui.PseudoSplitView");
//				previewer.setDoc(document);
//			} catch (PartInitException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}			
//		}
		
		
		//Shell shell = new Shell();
		//ApplicationWindow w = new SampleWindow(shell);
		//w.open();
		//js.get
		
//		JavaScriptEditor jsp  = (JavaScriptEditor)js.getAdapter(JavaScriptEditor.class);
//		ISourceViewer view = jsp.getview();
//		IDocument doc = js.getDocumentProvider().getDocument(js.getEditorInput());
//		SourceViewerTest window = new SourceViewerTest(shell, doc, view);
//	    window.setBlockOnOpen(true);
//	    
//	    window.open();


		//DefaultInformationControl wt = new DefaultInformationControl(ep.getEditorSite().getShell());
		//wt.setInformation("content");
		
//		new IInformationControlCreator() {
//			@Override
//			public IInformationControl createInformationControl(Shell parent) {
//				// TODO Auto-generated method stub
//				return new DefaultInformationControl(parent, EditorsUI.getTooltipAffordanceString());
//			}
//		};
		//wt.
		//MessageDialog.openInformation(ep.getSite().getShell(), "title", "message");
		//IWorkbenchWindow ww = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		//Button button = new Button(ep.getEditorSite().getShell(), SWT.HORIZONTAL);
		//button.setText("OK");
		
		return null;
	}

}
