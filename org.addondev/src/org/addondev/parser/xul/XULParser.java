package org.addondev.parser.xul;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.aonir.fuzzyxml.FuzzyXMLDocument;
import jp.aonir.fuzzyxml.FuzzyXMLElement;
import jp.aonir.fuzzyxml.FuzzyXMLNode;
import jp.aonir.fuzzyxml.FuzzyXMLParser;

import org.addondev.util.FileUtil;
import org.eclipse.core.runtime.IPath;

public class XULParser {
	//<!DOCTYPE overlay SYSTEM "chrome://dendzones/locale/dendzones.dtd">
	//<?xml-stylesheet href="chrome://dendzones/skin/dendzones.css" type="text/css"?>
	
	private static Pattern doctypeOverlayPattern = Pattern.compile("<!DOCTYPE\\s+overlay\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>");
	private static Pattern doctypeprefwindowPattern = Pattern.compile("<!DOCTYPE\\s+prefwindow\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>");
	private static Pattern stylesheetPattern = Pattern.compile("<\\?xml-stylesheet\\s+href=\\s*\"([^\"]+)\"\\s*.*?>");
	
	//private static Pattern entryPattern = Pattern.compile("\"&(*+);\"");
	
	//private IProject fProject;
	//private String fLocale;
	
	//private HashSet<String> NodeSet = new HashSet<String>();
	//private ArrayList<Pattern>
	
//	public XULParser(IProject project, String locale)
//	{
//		//fProject = project;
//		//fLocale = locale;
//		
//		//NodeSet.add("dialog");
//		//NodeSet.add("prefpane");
//		//NodeSet.add("window");
//	}

	public static String parse(IPath fullpath, int offset)
	{
		//ChromeURLMap chromemap = AddonDevPlugin.getDefault().getChromeURLMap(fProject, false);
		String text = FileUtil.getContent(fullpath.toFile());
		//text = FileUtils.readFileToString(fullpath.toFile(), "UTF-8");
		//String dtd = parseCSS(chromemap, text);
		
		String previewData = text;
		
		FuzzyXMLDocument document = new FuzzyXMLParser().parse(text);
		FuzzyXMLElement element = document.getElementByOffset(offset);
		FuzzyXMLElement preview = getPreviewNode(document, element);
		
		if(preview != null && preview.getName().equals("prefpane"))
		{
			FuzzyXMLElement pelement = (FuzzyXMLElement) document.getDocumentElement().getChildren()[0];
			
			class cl
			{
				public int start;
				public int len;
				public cl(int start, int len)
				{
					this.start = start;
					this.len = len;
				}
			}
			ArrayList<cl> tt = new ArrayList<cl>();
			
			String pname = pelement.getName();
			if("prefwindow".equals(pname))
			{
				//pelement.removeAllChildren();
				
				FuzzyXMLNode[] nodes = pelement.getChildren();
				for (FuzzyXMLNode fuzzyXMLNode : nodes) {
					if(fuzzyXMLNode instanceof FuzzyXMLElement)
					{
						FuzzyXMLElement celem = (FuzzyXMLElement)fuzzyXMLNode;
						if(celem.getName().equals("prefpane") && !celem.equals(preview))
						{
							tt.add(new cl(celem.getOffset(), celem.getLength()));
							//pelement.removeChild(celem);
							//break;
						}
					}
				}
				
				String com1= "<!-- ";
				String com2= " -->";
				StringBuffer sb = new StringBuffer(text);
				int inoffset = 0;
				for (cl t : tt) {
					sb = sb.insert( t.start+inoffset, com1);
					inoffset += com1.length();
					
					sb = sb.insert( t.start+t.len + inoffset, com2);
					inoffset += com2.length();	 
				}
				
				String mm = sb.toString();
				
////dataurl
//				//pelement.appendChild(preview);
//				String css = getCSS(text);
//				String xml = pelement.toXMLString();
//				xml = xml.replaceAll("&amp;", "&");
//				//xml = xml.replaceAll("&apos;", "\\\"");
//				previewData = "<?xml version=\"1.0\"?>\n" + css + xml;
				
//domparser
				previewData = mm.replaceAll("'", "&apos;").replaceAll("\n", "\\\\n");
				//previewData = css + xml;
			}
		}		
		
		return previewData;
		
		
//		Matcher m = xmlPattern.matcher(text);
//		int index = 0;
//		while(m.find())
//		{
//			String url = m.group(1);
//			
//			index = m.regionEnd() + 1;
//		}

	}
	
	private static FuzzyXMLElement getPreviewNode(FuzzyXMLDocument document, FuzzyXMLElement element)
	{
		FuzzyXMLElement previewElement = null;
		FuzzyXMLElement fnode = document.getDocumentElement();
		if(fnode.hasChildren())
		{			
			if("prefpane".equals(element.getName()))
			{
				previewElement = element;
			}
			else
			{
				FuzzyXMLElement node = element;
				do		
				{
					node = (FuzzyXMLElement) node.getParentNode();
				}while(node != null && !isEnablePreview(node) && !node.equals(fnode));
				previewElement = (FuzzyXMLElement) node;
			}				
		}
		return previewElement;
	}
	
	private static boolean isEnablePreview(FuzzyXMLNode node)
	{
		if(node instanceof FuzzyXMLElement)
		{
			FuzzyXMLElement elem = (FuzzyXMLElement)node;
			if("prefpane".equals(elem.getName()))
			{
				return true;
			}
		}
		return false;
	}
	
	public static String getCSS(String xml)
	{
		ArrayList<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(doctypeOverlayPattern);
		patterns.add(doctypeprefwindowPattern);
		patterns.add(stylesheetPattern);
		
		String data = ""; 		
		for (Pattern pattern : patterns) {
			Matcher m = pattern.matcher(xml);
			while(m.find())
			{
				data += m.group(0) + "\n";
			}			
		}

		return data;
	}
//	
//	public List<String> parseDTD(ChromeURLMap map, String text, String previewXML)
//	{
//		ArrayList<String> list = new ArrayList<String>();
//		Matcher m = doctypeOverlayPattern.matcher(text);
//		String xml = ""; 
//		while(m.find())
//		{
//			String dtd = m.group(0); //all
//			String url = m.group(1); //dtd
//			String local = map.convertChrome2Local(url);
//			if(local != null)
//			{
//				list.add(local);
//			}
//		}
//		
//		DTDMap dtdmap = AddonDevPlugin.getDefault().getDTDMap(fProject, false);
//		dtdmap.setLocate(fLocale);
//		for (String dtdpath : list) {
//			if(dtdmap.hasLocate(fLocale))
//			{
//				
//				
//			}
//			else
//			{
//				dtdmap.parse(FileUtil.getPath(dtdpath));
//			}			
//		}
//		
//		m = entryPattern.matcher(previewXML);
//		while(m.find())
//		{
//			String dtd = m.group(0); //all
//			String url = m.group(1); //dtd
//			String local = map.convertChrome2Local(url);
//			if(local != null)
//			{
//				list.add(local);
//			}
//		}
//		
//		
//		return list;
//	}
}
