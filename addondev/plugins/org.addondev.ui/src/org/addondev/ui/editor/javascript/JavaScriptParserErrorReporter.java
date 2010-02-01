package org.addondev.ui.editor.javascript;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

class JavaScriptParserErrorReporter implements ErrorReporter {

	private JavaScriptEditor fEditor;
	IResource resource;
	
	public void setEditor(JavaScriptEditor editor)
	{
		fEditor = editor;
		
		IEditorInput in = editor.getEditorInput();
		resource = (IResource)in.getAdapter(IResource.class);
	}
	
	@Override
	public void error(String message, String sourceName, int line,
            String lineSource, int lineOffset) {
		// TODO Auto-generated method stub
		System.out.println("error : " + line +" : " +lineOffset + " : " +message);

		IDocument document = fEditor.getDocument();
		line--;
		int start = -1;
		try {
			start = document.getLineOffset(line) + lineOffset;
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(start < 0) return;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(IMarker.LINE_NUMBER, line);
		map.put(IMarker.CHAR_START, start);
		map.put(IMarker.CHAR_END, start + 1);
		map.put(IMarker.MESSAGE, message);
		map.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		try {
			MarkerUtilities.createMarker(resource, map, IMarker.PROBLEM);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
        //reportErrorMessage(message, sourceName, line, lineSource, lineOffset, false);
	}

	@Override
	public EvaluatorException runtimeError(String message, String sourceName,
            int line, String lineSource,
            int lineOffset) {
		// TODO Auto-generated method stub
        return new EvaluatorException(message, sourceName, line,
                lineSource, lineOffset);
	}

	@Override
	public void warning(String message, String sourceName, int line,
            String lineSource, int lineOffset) {
		// TODO Auto-generated method stub
		System.out.println("warning : " + line +" : "+lineOffset + " : " +message);
	}

}
