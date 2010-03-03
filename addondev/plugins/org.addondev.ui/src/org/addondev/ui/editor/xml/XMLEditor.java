package org.addondev.ui.editor.xml;

import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.editor.PropertyChangeSourceViewerConfiguration;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class XMLEditor extends TextEditor implements IPropertyChangeListener{

	//public static final String XML_EDIT_CONTEXT = "#AddonDevJavascriptEditContext";
	public static final String ID = "org.addondev.ui.editor.xml";
	
	private PropertyChangeSourceViewerConfiguration fSourceViewerConfiguration;
	private XMLOutlinePage outline;
	//private ColorManager colorManager;

	public XMLEditor() {
		super();
		fSourceViewerConfiguration = new XMLConfiguration();
		setSourceViewerConfiguration(fSourceViewerConfiguration);
		setDocumentProvider(new XMLDocumentProvider());
		AddonDevUIPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
	}
	public void dispose() {
		super.dispose();
		AddonDevUIPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		if (IContentOutlinePage.class.equals(adapter)) 
		{
			if (outline == null) 
			{
				outline = new XMLOutlinePage(this);
			}
			return outline;
		}
		return super.getAdapter(adapter);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		if(fSourceViewerConfiguration.update(event))
		{
			getSourceViewer().invalidateTextPresentation();
		}		
	}
}
