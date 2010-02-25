package org.addondev.ui.editor.javascript.actions;

//import jp.addondev.debug.ui.PseudoSplitView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.TextSelection;
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
	
	private static Pattern rnp = Pattern.compile("\r\n");
	private static Pattern rp = Pattern.compile("\r");
	private static Pattern np = Pattern.compile("\n");

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IEditorPart ep = HandlerUtil.getActiveEditor(event);
		//ep.
		ITextEditor js  = (ITextEditor)ep;
		IDocument document = js.getDocumentProvider().getDocument(js.getEditorInput());
		String ss = ep.getEditorInput().getName();
		
		TextSelection sel = (TextSelection)js.getSelectionProvider().getSelection();

		int offset = sel.getOffset();
		int length = sel.getLength();
		//int orgffset = offset;
		//int orglength = length;
		
		int res = 0;
		try {
			res = check(document, offset-1);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		offset -= res;
		length += res;
		
		String text = null;
		try {
			text = document.get(offset, length);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} //sel.getText();
		String rnt = rn(text);
		String[] lines = text.split(rnt);

		
		StringBuffer buf = new StringBuffer();
		
		if(isStartWithComment(lines))
		{
			for (String line : lines) {
				buf.append(line.replaceFirst("//", "")).append(rnt);;
			}			
		}
		else
		{
			for (String string : lines) {
				buf.append("//").append(string).append(rnt);
			}
		}
		char lc = text.charAt(text.length()-1);
		
		if(lc!='\n' && lc!='\r' )
		{
			buf = buf.delete(buf.length()-rnt.length(), buf.length());
		}
		
		try {
			document.replace(offset, length, buf.toString());
			js.selectAndReveal(offset, buf.length());	
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private String rn(String text) {
		Matcher m = rnp.matcher(text);
		if (m.find()) {
			return "\r\n";
		}

		m = rp.matcher(text);
		if (m.find()) {
			return "\r";
		}

		m = np.matcher(text);
		if (m.find()) {
			return "\n";
		}
		
		return "";
	}
	
	private int check(IDocument doc, int offset) throws BadLocationException
	{
		int resoffset = offset;
		char c = doc.getChar(resoffset);
		while((c=='\t' || c==' ') && resoffset>=0)
		{
			resoffset--;
			c = doc.getChar(resoffset);
		}
		if(c=='\r' || c=='\n')
		{
			return offset - resoffset;
		}
		else
		{
			return 0;
		}
	}
	
	private boolean isStartWithComment(String[] lines)
	{
		for (String line : lines) {
			if(!line.trim().startsWith("//"))
			{
				return false;
			}
		}
		return true;
	}
}
