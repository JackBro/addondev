package org.addondev.ui.editor.javascript;

import org.addondev.editorinput.SeqEditorInput;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class JavaScriptDocumentProvider extends FileDocumentProvider {

	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		// TODO Auto-generated method stub
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new JavaScriptPartitionScanner(),
					new String[] {
						JavaScriptPartitionScanner.JS_COMMENT
						});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		else
		{
			if(element instanceof SeqEditorInput)
			{
				SeqEditorInput seqinput = (SeqEditorInput)element;
				
				document = super.createEmptyDocument();
				//document = super.createDocument(element);
				document.set(seqinput.getFn());
				IDocumentPartitioner partitioner =
					new FastPartitioner(
						new JavaScriptPartitionScanner(),
						new String[] {
							JavaScriptPartitionScanner.JS_COMMENT
							});
				partitioner.connect(document);
				document.setDocumentPartitioner(partitioner);
			}
		}
		return document;
	}

}
