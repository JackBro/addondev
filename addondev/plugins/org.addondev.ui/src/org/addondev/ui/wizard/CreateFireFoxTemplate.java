package org.addondev.ui.wizard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.template.ExtensionTemplateContextType;
import org.addondev.util.FileUtil;
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
import org.osgi.framework.Bundle;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CreateFireFoxTemplate {
	
	private class TemplateFile
	{
		private String src, dist;
		
		public String getSrc() {
			return src;
		}

		public String getDist() {
			return dist;
		}

		public TemplateFile(String src, String dist) {
			this.src = src;
			this.dist = dist;
		}
	}
	
	private class TemplateOption
	{
		private String src, id;

		public String getSrc() {
			return src;
		}

		public String getId() {
			return id;
		}

		public TemplateOption(String src, String id) {
			this.src = src;
			this.id = id;
		}
	}
	
	private class XMLHandler extends DefaultHandler{

		private ArrayList<TemplateFile> files;
		private HashMap<String, ArrayList<TemplateOption>> options;

//		public XMLHandler()
//		{
//			files = new ArrayList<TemplateFile>();
//			options = new ArrayList<TemplateOption>();
//		}

		public XMLHandler(ArrayList<TemplateFile> files,
				HashMap<String, ArrayList<TemplateOption>> options) {
			this.files = files;
			this.options = options;
		}
		
		@Override
		public void startElement(String uri,
                String localName,
                String qName,
                Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			//uper.startElement(arg0, arg1, arg2, arg3);
			if (qName.equals("file"))
			{
				String src = attributes.getValue("src");
				String dist = attributes.getValue("dist");
				files.add(new TemplateFile(src, dist));
			}
			else if(qName.equals("option"))
			{
				String src = attributes.getValue("src");
				String id = attributes.getValue("id");
				if(options.containsKey(id))
				{
					options.get(id).add(new TemplateOption(src, id));
				}
				else
				{
					ArrayList<TemplateOption> list = new ArrayList<TemplateOption>();
					list.add(new TemplateOption(src, id));
					options.put(id, list);
				}
			}
		}		
	}
	
	public void createPtoject(IProject project, 
			Map<String, String> param, List<String> templatefiles, IProgressMonitor monitor) throws BadLocationException, TemplateException, CoreException, ParserConfigurationException, SAXException, IOException
	{
		//TemplateStore store = AddonDevUIPlugin.getDefault().getTemplateStore();
		String typeid = ExtensionTemplateContextType.EXTENSION;
		Bundle bundle = AddonDevUIPlugin.getDefault().getBundle();
		ArrayList<TemplateFile> files = new ArrayList<TemplateFile>();
		HashMap<String, ArrayList<TemplateOption>> options = new HashMap<String, ArrayList<TemplateOption>>();

		SAXParserFactory spfactory = SAXParserFactory.newInstance();
	    SAXParser parser = spfactory.newSAXParser();
	    for (String template : templatefiles) {
	    	InputStream input = bundle.getEntry(template).openStream();
	    	//String xml = FileUtil.getContent(input);
	    	//String xml = FileUtil.getContent(input);
	    	parser.parse(input, new XMLHandler(files, options));
		}
		
		TemplateContextType contextType = AddonDevUIPlugin.getDefault()
				.getContextTypeRegistry().getContextType(typeid);
		IDocument document = new Document();
		TemplateContext context = new DocumentTemplateContext(contextType, document, 0, 0);
		String[] defaults = new String[]{
				"overlay_xul",
			"overlay_props_js",
			"overlay_onunload_js",
			"overlay_onload_js",
			"overlay_dtd"};
		for (String string : defaults) {
			context.setVariable(string, "");
		}
		for (Entry<String, String> template2 : param.entrySet()) {
			String key = template2.getKey();
			String value = template2.getValue();
			context.setVariable(key, value);
		}
		
		for (Entry<String, ArrayList<TemplateOption>> option : options.entrySet()) {
			StringBuffer buf = new StringBuffer();
			String id = option.getKey();
			ArrayList<TemplateOption> list = option.getValue();
			for (TemplateOption templateOption : list) {
				InputStream input = bundle.getEntry(templateOption.src).openStream();
				String text = FileUtil.getContent(input);
				Template template = new Template(templateOption.getSrc(), "", typeid, text, false);
				TemplateBuffer buffer = context.evaluate(template);	
				buf.append(buffer.getString()).append("\n");
			}
			context.setVariable(id, buf.toString());
		}
		
		for (TemplateFile templatefile : files) {
			InputStream input = bundle.getEntry(templatefile.src).openStream();
			String text = FileUtil.getContent(input);
			Template template = new Template(templatefile.getSrc(), "", typeid, text, false);
			TemplateBuffer buffer = context.evaluate(template);
			
			IPath dirpath = new Path(templatefile.getDist()).removeLastSegments(1);
			int cnt = dirpath.segmentCount();
			for (int i = cnt-1; i >= 0; i--) {
				IPath dpath = dirpath.removeLastSegments(i);
				IFolder folder = project.getFolder(dpath);
				if(!folder.exists())
				{
					folder.create(false, true, monitor);
				}	
			}
			IFile file = project.getFile(templatefile.getDist());
			file.create(new ByteArrayInputStream(buffer.getString().getBytes()), true, monitor);
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
