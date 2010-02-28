package org.addondev.ui.editor.javascript;

import java.io.IOException;

import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.util.FileUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.IStorageEditorInput;
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
						JavaScriptPartitionScanner.JS_COMMENT,
						JavaScriptPartitionScanner.JS_STRING});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		else
		{
			//if(element instanceof SeqEditorInput)
			if(element instanceof IStorageEditorInput)
			{
				IStorageEditorInput seqinput = (IStorageEditorInput)element;
				
				document = super.createEmptyDocument();
				//document = super.createDocument(element);
				String text = "";
				try {
					text = FileUtil.getContent(seqinput.getStorage().getContents());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					AddonDevUIPlugin.log("JavaScriptDocumentProvider", e);
				}
				//document.set(seqinput.getFn());
				document.set(text);
				IDocumentPartitioner partitioner =
					new FastPartitioner(
						new JavaScriptPartitionScanner(),
						new String[] {
							JavaScriptPartitionScanner.JS_COMMENT,
							JavaScriptPartitionScanner.JS_STRING});
				partitioner.connect(document);
				document.setDocumentPartitioner(partitioner);
			}
		}
		return document;
	}

}
