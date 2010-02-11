package org.addondev.ui.wizard;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.template.ExtensionTemplateContextType;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.persistence.TemplateStore;

public class CreateFireFoxTemplate {
	
	public void createPtoject(IProject project, Map<String, String> param, List<Template> templates, IProgressMonitor monitor) throws BadLocationException, TemplateException, CoreException
	{
		TemplateStore store = AddonDevUIPlugin.getDefault().getTemplateStore();
		
		// AddonDevUIPlugin.getDefault().getTemplateStore().
		TemplateContextType contextType = AddonDevUIPlugin.getDefault()
				.getContextTypeRegistry().getContextType(ExtensionTemplateContextType.EXTENSION);
		IDocument document = new Document();
		final TemplateContext context = new DocumentTemplateContext(contextType, document, 0, 0);
		for (Entry<String, String> template2 : param.entrySet()) {
			String key = template2.getKey();
			String value = template2.getValue();
			context.setVariable(key, value);
		}
		
		
		for (Template template : templates) {
			TemplateBuffer buffer = context.evaluate(template);
			IFile file = project.getFile(template.getName());
			file.create(new ByteArrayInputStream(buffer.toString().getBytes()), true, monitor);
		}
		
//		for (Template tem : option) {
//			String name = tem.getName();
//
//			String templateString = null;
//			try {
//				
//				context.setVariable("name", "value");
//				TemplateBuffer buffer = context.evaluate(template);
//				templateString = buffer.getString();
//			} catch (BadLocationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (TemplateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}		
	}
}
