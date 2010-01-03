package org.addondev.parser.xul;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.aonir.fuzzyxml.FuzzyXMLDocument;
import jp.aonir.fuzzyxml.FuzzyXMLElement;
import jp.aonir.fuzzyxml.FuzzyXMLNode;
import jp.aonir.fuzzyxml.FuzzyXMLParser;

import org.addondev.parser.dtd.DTDMap;
import org.addondev.plugin.AddonDevPlugin;
import org.addondev.util.ChromeURLMap;
import org.addondev.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;

public class XULParser {
	//<!DOCTYPE overlay SYSTEM "chrome://dendzones/locale/dendzones.dtd">
	//<?xml-stylesheet href="chrome://dendzones/skin/dendzones.css" type="text/css"?>
	
	private static Pattern doctypeOverlayPattern = Pattern.compile("<!DOCTYPE\\s+overlay\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>");
	private static Pattern doctypeprefwindowPattern = Pattern.compile("<!DOCTYPE\\s+prefwindow\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>");
	
	private static Pattern stylesheetPattern = Pattern.compile("<?xml-stylesheet\\s+href=\\s*\"([^\"]+)\"\\s*.*?>");
	
	private static Pattern entryPattern = Pattern.compile("\"&(*+);\"");
	
	private IProject fProject;
	private String fLocale;
	
	private HashSet<String> NodeSet = new HashSet<String>();
	//private ArrayList<Pattern>
	
	public XULParser(IProject project, String locale)
	{
		fProject = project;
		fLocale = locale;
		
		NodeSet.add("dialog");
		NodeSet.add("prefpane");
		NodeSet.add("window");
	}

	public void parse(IPath fullpath, int offset)
	{
		ChromeURLMap chromemap = AddonDevPlugin.getDefault().getChromeURLMap(fProject, false);
		
		String text = null;
		//text = FileUtils.readFileToString(fullpath.toFile(), "UTF-8");
		text = FileUtil.getContent(fullpath.toFile());
		String dtd = parseCSS(chromemap, text);
		
		

		//rep dtd
		
		
		FuzzyXMLDocument document = new FuzzyXMLParser().parse(text);
		FuzzyXMLElement element = document.getElementByOffset(offset);
		FuzzyXMLElement preview = getPreviewNode(document, element);
		
		String xml = preview.toXMLString();
		
		
		
		
		Matcher m = xmlPattern.matcher(text);
		int index = 0;
		while(m.find())
		{
			String url = m.group(1);
			
			index = m.regionEnd() + 1;
		}

	}
	
	private FuzzyXMLElement getPreviewNode(FuzzyXMLDocument document, FuzzyXMLElement element)
	{
		FuzzyXMLElement previewElement = null;
		FuzzyXMLElement fnode = document.getDocumentElement();
		if(fnode.hasChildren())
		{
			FuzzyXMLNode firstnode = fnode.getChildren()[0];
			
			if(NodeSet.contains(element.getName()))
			{
				previewElement = element;
			}
			else
			{
				FuzzyXMLElement node = element;
				do		
				{
					String tmp = node.toString();
					node = (FuzzyXMLElement) node.getParentNode();
				}while(node != null && !isEnablePreview(node) && !node.equals(fnode));
				previewElement = (FuzzyXMLElement) node;
			}				
		}
		return previewElement;
	}
	
	private boolean isEnablePreview(FuzzyXMLNode node)
	{
		if(node instanceof FuzzyXMLElement)
		{
			FuzzyXMLElement elem = (FuzzyXMLElement)node;
			if(NodeSet.contains(elem.getName()))
			{
				return true;
			}
		}
		return false;
	}
	
	public String parseCSS(ChromeURLMap map, String text)
	{
		Matcher m = stylesheetPattern.matcher(text);
		String xml = ""; 
		while(m.find())
		{
			String dtd = m.group(0); //all
			String url = m.group(1); //chrome
			String local = map.convertChrome2Local(url);
			if(local != null)
			{
				String rep = dtd.replaceFirst(url, local);
				xml += rep + "\n";
			}
		}
		
		return xml;
	}
	
	public List<String> parseDTD(ChromeURLMap map, String text, String previewXML)
	{
		ArrayList<String> list = new ArrayList<String>();
		Matcher m = doctypeOverlayPattern.matcher(text);
		String xml = ""; 
		while(m.find())
		{
			String dtd = m.group(0); //all
			String url = m.group(1); //dtd
			String local = map.convertChrome2Local(url);
			if(local != null)
			{
				list.add(local);
			}
		}
		
		DTDMap dtdmap = AddonDevPlugin.getDefault().getDTDMap(fProject, false);
		dtdmap.setLocate(fLocale);
		for (String dtdpath : list) {
			if(dtdmap.hasLocate(fLocale))
			{
				
				
			}
			else
			{
				dtdmap.parse(FileUtil.getPath(dtdpath));
			}			
		}
		
		m = entryPattern.matcher(previewXML);
		while(m.find())
		{
			String dtd = m.group(0); //all
			String url = m.group(1); //dtd
			String local = map.convertChrome2Local(url);
			if(local != null)
			{
				list.add(local);
			}
		}
		previewXML
		
		return list;
	}
}
