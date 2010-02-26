package org.addondev.ui.editor.xml.action;

import org.addondev.ui.editor.xml.XMLPartitionScanner;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

public class ToggleCommentHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IEditorPart ep = HandlerUtil.getActiveEditor(event);
		ITextEditor editor  = (ITextEditor)ep;
		IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		TextSelection sel = (TextSelection)editor.getSelectionProvider().getSelection();

		int offset = sel.getOffset();
		int length = sel.getLength();		
		String contenttype = null;
		try {
			contenttype = document.getContentType(offset);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(XMLPartitionScanner.XML_CDATA.equals(contenttype))
		{
			org.addondev.ui.editor.javascript.actions.ToggleCommentHandler tg = 
				new org.addondev.ui.editor.javascript.actions.ToggleCommentHandler();
			return tg.execute(event);
		}

		
		//sel.getText()
		
		

		return null;
	}

}
