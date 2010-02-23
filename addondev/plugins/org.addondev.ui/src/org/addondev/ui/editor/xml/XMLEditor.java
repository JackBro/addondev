package org.addondev.ui.editor.xml;

import org.eclipse.ui.editors.text.TextEditor;

public class XMLEditor extends TextEditor {

	//public static final String XML_EDIT_CONTEXT = "#AddonDevJavascriptEditContext";
	public static final String ID = "org.addondev.ui.editor.xml";
	
	private ColorManager colorManager;

	public XMLEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
