package org.addondev.debug.ui.actions;

import org.addondev.editor.javascript.JavaScriptEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.AbstractRulerActionDelegate;
import org.eclipse.ui.texteditor.ITextEditor;

public class SelectLineRulerActionDelegate extends AbstractRulerActionDelegate {

	private ITextEditor fEditor;
	private IVerticalRulerInfo fRuler;

	
	@Override
	protected IAction createAction(ITextEditor editor,
			IVerticalRulerInfo rulerInfo) {
		// TODO Auto-generated method stub
		fEditor = editor;
		fRuler = rulerInfo;			

		return null;
	}

	@Override
	public void mouseDown(MouseEvent e) {
		// TODO Auto-generated method stub
		if(fEditor instanceof JavaScriptEditor)
		{
			IDocument document= fEditor.getDocumentProvider().getDocument(fEditor.getEditorInput());
			int line = fRuler.getLineOfLastMouseButtonActivity();
			int offset = -1;
			int linelength = -1;
			try {
				offset = document.getLineOffset(line);
				linelength = document.getLineLength(line);
			} catch (BadLocationException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			if(offset >= 0 && linelength >= 0)
			{
				JavaScriptEditor jseditor = (JavaScriptEditor)fEditor;
				jseditor.setSelection(offset, linelength);
			}
		}
		super.mouseDown(e);
	}

}
