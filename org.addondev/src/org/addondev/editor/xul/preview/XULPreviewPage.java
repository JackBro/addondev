package org.addondev.editor.xul.preview;

import java.io.File;
import java.util.List;

import org.eclipse.ui.part.Page;

public abstract class XULPreviewPage extends Page {
	public abstract void setPreviewFile(File file);
	public abstract void setDocument(List<String> xuls);
}
