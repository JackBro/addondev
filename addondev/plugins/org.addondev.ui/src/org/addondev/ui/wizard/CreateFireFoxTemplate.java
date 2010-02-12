package org.addondev.ui.wizard;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.template.ExtensionTemplateContextType;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
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
	
	public void createPtoject(IProject project, Map<String, String> param, List<Template> templates, List<Template> optiontemplates, IProgressMonitor monitor) throws BadLocationException, TemplateException, CoreException
	{
		TemplateStore store = AddonDevUIPlugin.getDefault().getTemplateStore();
		
		// AddonDevUIPlugin.getDefault().getTemplateStore().
		TemplateContextType contextType = AddonDevUIPlugin.getDefault()
				.getContextTypeRegistry().getContextType(ExtensionTemplateContextType.EXTENSION);
		IDocument document = new Document();
		TemplateContext context = new DocumentTemplateContext(contextType, document, 0, 0);
		for (Entry<String, String> template2 : param.entrySet()) {
			String key = template2.getKey();
			String value = template2.getValue();
			context.setVariable(key, value);
		}
		
		//HashMap<String,String> options = new HashMap<String, String>();
		for (Template template : optiontemplates) {
			TemplateBuffer buffer = context.evaluate(template);
			//options.put(template.getName(), buffer.toString());
			context.setVariable(template.getName(), buffer.toString());
		}
		
		for (Template template : templates) {
			TemplateBuffer buffer = context.evaluate(template);
			IFile file = project.getFile(template.getName());
			file.create(new ByteArrayInputStream(buffer.toString().getBytes()), true, monitor);
		}		
	}
	
	public void createPtoject(IProject project, Map<String, String> param, Map<String, String> fileparam, Map<String, String> optionparam, IProgressMonitor monitor) throws BadLocationException, TemplateException, CoreException
	{
		String typeid = ExtensionTemplateContextType.EXTENSION;
		TemplateStore store = AddonDevUIPlugin.getDefault().getTemplateStore();
		//Template[] tmps =  store.getTemplates(typeid);
		
		TemplateContextType contextType = AddonDevUIPlugin.getDefault()
			.getContextTypeRegistry().getContextType(ExtensionTemplateContextType.EXTENSION);
		IDocument document = new Document();
		TemplateContext context = new DocumentTemplateContext(contextType, document, 0, 0);
		for (Entry<String, String> template2 : param.entrySet()) {
			String key = template2.getKey();
			String value = template2.getValue();
			context.setVariable(key, value);
		}
		
		for (Entry<String, String> option : optionparam.entrySet()) {
			String key = option.getKey();
			
			Template template = store.findTemplate(key, typeid);
			TemplateBuffer buffer = context.evaluate(template);
			context.setVariable(template.getName(), buffer.getString());
		}
		
		for (Entry<String, String> files : fileparam.entrySet()) {
			String name = files.getKey();
			String path = files.getValue();

			IPath dirpath = new Path(path).removeLastSegments(1);
			int cnt = dirpath.segmentCount();
			for (int i = cnt-1; i >= 0; i--) {
				IPath dpath = dirpath.removeLastSegments(i);
				IFolder folder = project.getFolder(dpath);
				if(!folder.exists())
				{
					folder.create(false, true, monitor);
				}	
			}
			
			Template template = store.findTemplate(name, typeid);	
			TemplateBuffer buffer = context.evaluate(template);
				
			IFile file = project.getFile(path);
			file.create(new ByteArrayInputStream(buffer.getString().getBytes()), true, monitor);
		}		
	}
}
