package org.addondev.ui.editor.javascript;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;

public class JavaScriptAutoIndentStrategy extends
		DefaultIndentLineAutoEditStrategy {

	@Override
	public void customizeDocumentCommand(IDocument d, DocumentCommand c) {
		// TODO Auto-generated method stub
		super.customizeDocumentCommand(d, c);
	}

	@Override
	protected int findEndOfWhiteSpace(IDocument document, int offset, int end)
			throws BadLocationException {
		// TODO Auto-generated method stub
		return super.findEndOfWhiteSpace(document, offset, end);
	}

}
