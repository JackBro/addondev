package org.addondev.templates;

import org.addondev.plugin.AddonDevPlugin;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Image;

public class JavaScriptTemplateCompletionProcessor extends
		TemplateCompletionProcessor {

	@Override
	protected TemplateContextType getContextType(ITextViewer viewer,
			IRegion region) {
		// TODO Auto-generated method stub
		//return null;
		return AddonDevPlugin.getDefault()
		.getContextTypeRegistry().getContextType(JavaScriptTemplateContextType.JAVASCRIPT_CONTEXT_TYPE);
	}

	@Override
	protected Image getImage(Template template) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Template[] getTemplates(String contextTypeId) {
		// TODO Auto-generated method stub
		//return null;
		return AddonDevPlugin.getDefault().getTemplateStore().getTemplates();
	}

}
