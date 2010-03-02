package org.addondev.ui.editor;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public abstract class PropertyChangeSourceViewerConfiguration extends TextSourceViewerConfiguration{
	public abstract boolean update(PropertyChangeEvent event);
	public abstract void update();
	
	protected void setSyntax(PropertyChangeEvent event, Token tolen, String boldsuffix, String italicsuffix)
	{
		String property = event.getProperty();
		Object newvalue = event.getNewValue();
		
		if(newvalue instanceof RGB) 
		{
			setSyntaxColor(tolen, property, (RGB)newvalue);
		}
		else if(newvalue instanceof Boolean)
		{
			if(property.endsWith(boldsuffix))
				setSyntaxStyle(tolen, property, (Boolean)newvalue, SWT.BOLD);
			if(property.endsWith(italicsuffix))
				setSyntaxStyle(tolen, property, (Boolean)newvalue, SWT.ITALIC);
		}		
		
	}
	
	protected void setSyntaxColor(Token tolen, String property, RGB newvalue)
	{
		ColorRegistry cr = JFaceResources.getColorRegistry();
		cr.put(property, newvalue);
		Color foreground = cr.get(property);
		
		TextAttribute attr = (TextAttribute) tolen.getData();
		//Color foreground = attr.getForeground();
		Color background = attr.getBackground();
		int style = attr.getStyle();
		
		tolen.setData(new TextAttribute(foreground, background, style));
	}
	
	protected void setSyntaxStyle(Token tolen, String property, Boolean newvalue, int styletype)
	{		
		TextAttribute attr = (TextAttribute) tolen.getData();
		int style = attr.getStyle();
		style = newvalue? style|styletype : style&~styletype;
		
		Color foreground = attr.getForeground();
		Color background = attr.getBackground();
		
		tolen.setData(new TextAttribute(foreground, background, style));
	}
}
