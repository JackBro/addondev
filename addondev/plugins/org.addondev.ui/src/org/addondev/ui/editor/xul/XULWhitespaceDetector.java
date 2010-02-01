package org.addondev.ui.editor.xul;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class XULWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
