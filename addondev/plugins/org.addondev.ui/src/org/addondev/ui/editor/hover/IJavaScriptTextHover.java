package org.addondev.ui.editor.hover;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;

public interface IJavaScriptTextHover extends ITextHover, ITextHoverExtension {
	public void setContentType(String contentType);
	//public void getHoverInfo(ITextViewer textViewer, IRegion hoverRegion, String contentType;
}
