package org.addondev.ui.editor;

import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public abstract class PropertyChangeSourceViewerConfiguration extends TextSourceViewerConfiguration{
	public abstract boolean update(PropertyChangeEvent event);
	public abstract void update();
}
