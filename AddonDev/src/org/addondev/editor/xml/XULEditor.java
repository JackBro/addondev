package org.addondev.editor.xml;

import org.eclipse.ui.editors.text.TextEditor;

public class XULEditor extends TextEditor {

	private ColorManager colorManager;

	public XULEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XULConfiguration(colorManager));
		setDocumentProvider(new XULDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
