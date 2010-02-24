package org.addondev.ui.editor.hover;

import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;

public interface IJavaScriptTextHover extends ITextHover, ITextHoverExtension {
	public void setContentType(String contentType);
}
