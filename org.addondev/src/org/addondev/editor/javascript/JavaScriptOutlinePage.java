package org.addondev.editor.javascript;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;


import org.addondev.parser.javascript.JsNode;
import org.addondev.parser.javascript.Lexer;
import org.addondev.parser.javascript.Parser;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class JavaScriptOutlinePage extends ContentOutlinePage {

	JavaScriptEditor editorView;
    IDocument document;
	
	public JavaScriptOutlinePage(JavaScriptEditor editor) {
		this.editorView = editor;
	}
	
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		super.createControl(parent);
		
        final TreeViewer tree = getTreeViewer();
        IDocumentProvider provider = editorView.getDocumentProvider();
        document = provider.getDocument(editorView.getEditorInput());
        
        //model = getParsedModel();
        //tree.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
        tree.setContentProvider(new JavaScriptContentProvider());
        tree.setLabelProvider(new JavaScriptLabelProvider());
        tree.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				//JavaScriptNode jsnode = (JavaScriptNode)sel.getFirstElement();
				//int offset = jsnode.getOffset();
				JsNode node = (JsNode)sel.getFirstElement();
				
				int offset = node.getOffset();
				if(offset<0)
				{		
				}
				//jsnode.setOffset(offset);
				editorView.selectAndReveal(offset, 0);	
			}
        });
        
        Parser parser = null;
//        try
//        {
//        String text = document.get();        
//		Lexer lex = new Lexer(text);
//		parser = new Parser(); // パーサーを作成。
//		parser.parse(lex);
//		tree.setInput(parser.root);
//        }catch(Exception e)
//        {
//        	return;
//        }
		//tree.setInput(parser.root);
	}

	
	private class StringInputStream extends InputStream {
	    StringReader in;
	    private StringInputStream() {}

	    /** build input stream from given string.
	     * @param source input stream source
	     */
	    public StringInputStream(String source) {
	    	in = new StringReader(source);
	    }

	    public int read() throws IOException { return in.read(); }
	    
	    public void close() throws IOException { in.close(); }

	    public synchronized void mark(int readlimit) {
		try {
		    in.mark(readlimit);
		} catch(IOException e) {
		    throw new RuntimeException("IOException : StringInputStream["+
					       toString()+"]");
		}
	    }
	 
	    public synchronized void reset() throws IOException { in.reset(); }
	    public boolean markSupported() { return true; }
	}

}
