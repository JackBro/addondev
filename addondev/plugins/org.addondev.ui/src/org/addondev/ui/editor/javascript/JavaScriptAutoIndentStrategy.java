package org.addondev.ui.editor.javascript;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;

public class JavaScriptAutoIndentStrategy extends
		DefaultIndentLineAutoEditStrategy {

	private String del;
	@Override
	public void customizeDocumentCommand(IDocument d, DocumentCommand c) {
		// TODO Auto-generated method stub
		if(c.length == 0 && c.text != null && isDelimiter(d, c.text))
		{	
			del = c.text;
			try {
				int line = d.getLineOfOffset(c.offset);
				int start = d.getLineOffset(line);
				int end =  start + d.getLineLength(line) - 1;
				int offset = findEndOfWhiteSpace(d, start, end);
				char pre = d.getChar(end-del.length());
				switch (pre) {
				case '{':
					StringBuffer buf = new StringBuffer(c.text);
					buf.append(d.get(start, offset-start));
					buf.append("\t");
					c.text = buf.toString();					
					break;
				default:
					super.customizeDocumentCommand(d, c);
					break;
				}

			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		else
//		{
//			super.customizeDocumentCommand(d, c);
//		}
	}

	@Override
	protected int findEndOfWhiteSpace(IDocument document, int offset, int end)
			throws BadLocationException {
		// TODO Auto-generated method stub
		return super.findEndOfWhiteSpace(document, offset, end);
	}
	
	private boolean isDelimiter(IDocument d, String text)
	{
		String[] delimiters = d.getLegalLineDelimiters();
		
		for (String delimiter : delimiters) {
			if(text.endsWith(delimiter))
			{
				del = delimiter;
				return true;
			}
		}
		
		return false;
	}

}
