package org.addondev.ui;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public interface IColorManager {
	public void setPreferenceStore(IPreferenceStore store);
	public Color getColor(String key);
	public RGB getRGB(String key);
}
