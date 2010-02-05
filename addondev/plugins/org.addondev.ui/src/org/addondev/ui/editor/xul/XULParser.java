package org.addondev.ui.editor.xul;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.aonir.fuzzyxml.FuzzyXMLDocument;
import jp.aonir.fuzzyxml.FuzzyXMLElement;
import jp.aonir.fuzzyxml.FuzzyXMLNode;
import jp.aonir.fuzzyxml.FuzzyXMLParser;

import org.addondev.core.AddonDevPlugin;
import org.addondev.ui.editor.xml.XMLPartitionScanner;
import org.addondev.ui.editor.xul.preview.OffsetInfo;
import org.addondev.util.FileUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class XULParser {
		
	private static Pattern doctypeOverlayPattern = Pattern.compile("<!DOCTYPE\\s+overlay\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>");
	private static Pattern doctypeprefwindowPattern = Pattern.compile("<!DOCTYPE\\s+prefwindow\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>");
	private static Pattern stylesheetPattern = Pattern.compile("<\\?xml-stylesheet\\s+href=\\s*\"([^\"]+)\"\\s*.*?>");
	private static Pattern entryPattern = Pattern.compile("\"&(.+);\"");

	public static ArrayList<String> parse(String text)
	{
		ArrayList<String> xuldatalist = new ArrayList<String>();
		
		//if(text == null)
		
		String ElementName = null;
		FuzzyXMLElement targetelement = null;
		//String text = FileUtil.getContent(fullpath.toFile());
		FuzzyXMLParser parser = new FuzzyXMLParser();
		FuzzyXMLDocument document = parser.parse(text);
		FuzzyXMLElement element = document.getDocumentElement();
		if(element.hasChildren())
		{
			HashSet<String> set = new HashSet<String>();
			set.add("prefwindow");
			set.add("window");
			set.add("dialog");
			for (FuzzyXMLNode node : element.getChildren()) {
				if(node instanceof FuzzyXMLElement)
				{
					String name = ((FuzzyXMLElement)node).getName();
					name = name.toLowerCase();
					if(set.contains(name))
					{
						ElementName = name;
						targetelement = ((FuzzyXMLElement)node);
						break;
					}
				}
			}
			
			
			if(ElementName == null)
			{
				
			}
			else if(ElementName.equals("prefwindow"))
			{
				ArrayList<OffsetInfo> elemoffsetlist = new ArrayList<OffsetInfo>();
				//ArrayList<FuzzyXMLElement> prefpanellist = new ArrayList<FuzzyXMLElement>();
				FuzzyXMLNode[] nodes = targetelement.getChildren();
				for (FuzzyXMLNode fuzzyXMLNode : nodes) {
					if(fuzzyXMLNode instanceof FuzzyXMLElement)
					{
						FuzzyXMLElement chelem = (FuzzyXMLElement)fuzzyXMLNode;
						if(chelem.getName().equals("prefpane"))
						{
							elemoffsetlist.add(new OffsetInfo(chelem.getOffset(), chelem.getLength()));
							//prefpanellist.add(chelem);
							//pelement.removeChild(celem);
							//break;
						}
					}
				}
				
				
				//ArrayList<OffsetInfo> elemoffsetlist2 = (ArrayList<OffsetInfo>) elemoffsetlist.clone();
				for (OffsetInfo elemoffset : elemoffsetlist) {
					int inoffset = 0;
					OffsetInfo tmp = elemoffset;
					StringBuffer sb = new StringBuffer(text);
					for (OffsetInfo cl2 : elemoffsetlist) {
						if(cl2.start != tmp.start)
						{
							int start = cl2.start - inoffset;
							//sb = sb.delete(start, start + cl2.len);//( t.start+inoffset, com1);
							sb = sb.delete(start, start + cl2.len);
							inoffset += cl2.len; 							
						}
					}
					xuldatalist.add(convertText(sb.toString()));
				}			
			}
			else
			{
				xuldatalist.add(convertText(text));
			}
		}
		
		return xuldatalist;
	}
	
	private static String convertText(String text)
	{
		//return text.replaceAll("&amp;", "&").replaceAll("\n", "");
		
		//if()
		//{
			text = text.replaceFirst("chrome://stacklink/skin/preference.css", "file:///D:/data/src/PDE/workrepository/work/stacklink/skin/classic/preference.css");
		//}
		return text.replaceAll("'", "&apos;").replaceAll("\n", "\\\\n");
		//	return	text.replaceAll("&amp;", "&").replaceAll("\n", "");
	}
	
//	public String parse(IPath fullpath, int offset) throws IOException
//	{
//		//ChromeURLMap chromemap = AddonDevPlugin.getDefault().getChromeURLMap(fProject, false);
//		String text = FileUtil.getContent(fullpath.toFile());
//		//text = FileUtils.readFileToString(fullpath.toFile(), "UTF-8");
//		//String dtd = parseCSS(chromemap, text);
//		
//		String previewData = text;
//		
//		FuzzyXMLDocument document = new FuzzyXMLParser().parse(text);
//
//		FuzzyXMLElement element = document.getElementByOffset(offset);
//		FuzzyXMLElement preview = getPreviewNode(document, element);
//		
//		if(preview != null && preview.getName().equals("prefpane"))
//		{
//			FuzzyXMLElement pelement = (FuzzyXMLElement) document.getDocumentElement().getChildren()[0];
//			
//
//			ArrayList<OffsetInfo> tt = new ArrayList<OffsetInfo>();
//			
//			String pname = pelement.getName();
//			if("prefwindow".equals(pname))
//			{
//				//pelement.removeAllChildren();
//				
//				FuzzyXMLNode[] nodes = pelement.getChildren();
//				for (FuzzyXMLNode fuzzyXMLNode : nodes) {
//					if(fuzzyXMLNode instanceof FuzzyXMLElement)
//					{
//						FuzzyXMLElement celem = (FuzzyXMLElement)fuzzyXMLNode;
//						if(celem.getName().equals("prefpane") && !celem.equals(preview))
//						{
//							tt.add(new OffsetInfo(celem.getOffset(), celem.getLength()));
//							//pelement.removeChild(celem);
//							//break;
//						}
//					}
//				}
//				
////				String com1= "<!-- ";
////				String com2= " -->";
////				StringBuffer sb = new StringBuffer(text);
////				int inoffset = 0;
////				for (cl t : tt) {
////					sb = sb.insert( t.start+inoffset, com1);
////					inoffset += com1.length();
////					
////					sb = sb.insert( t.start+t.len + inoffset, com2);
////					inoffset += com2.length();	 
////				}
////				
////				String mm = sb.toString();
//				
//				
//				int inoffset = 0;
//				StringBuffer sb = new StringBuffer(text);
//				for (OffsetInfo t : tt) {
//					int start = t.start - inoffset;
//					sb = sb.delete(start, start + t.len);//( t.start+inoffset, com1);
//					inoffset += t.len; 
//				}
//				
//				String mm = sb.toString();
//				previewData = mm.replaceAll("'", "&apos;").replaceAll("\n", "");
//				
//////dataurl
////				//pelement.appendChild(preview);
////				String css = getCSS(text);
////				String xml = pelement.toXMLString();
////				xml = xml.replaceAll("&amp;", "&");
////				//xml = xml.replaceAll("&apos;", "\\\"");
////				previewData = "<?xml version=\"1.0\"?>\n" + css + xml;
//				
//				//previewData = mm.replaceAll("&amp;", "&").replaceAll("\n", "");
//				
//////domparser
////				previewData = mm.replaceAll("'", "&apos;").replaceAll("\n", "\\\\n");
////				//previewData = css + xml;
//			}
//		}		
//		
//		return previewData;
//	}
	
	private FuzzyXMLElement getPreviewNode(FuzzyXMLDocument document, FuzzyXMLElement element)
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
	
	private boolean isEnablePreview(FuzzyXMLNode node)
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
	
	private static ArrayList<String> getDTDList(String xml)
	{
		ArrayList<String> dtdlist = new ArrayList<String>();
		
		ArrayList<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(doctypeOverlayPattern);
		patterns.add(doctypeprefwindowPattern);
	
		for (Pattern pattern : patterns) {
			Matcher m = pattern.matcher(xml);
			while(m.find())
			{
				dtdlist.add(m.group(1));
			}			
		}

		return dtdlist;
	}
	
	public static HashMap<Integer, Integer> checkEntity(IProject project, IDocument docment, String locale)
	{
		HashMap<Integer, Integer> errormap= new HashMap<Integer, Integer>();
		
		String xml = docment.get();
		ArrayList<String> dtdlist = getDTDList(xml);
		for (String dtd : dtdlist) {
			Map<String, String> entitymap = AddonDevPlugin.getDefault().getEntityMap(project, dtd, locale, false);
			//if(entitymap == null ) continue;
			
			Matcher m = entryPattern.matcher(xml);
			while(m.find())
			{
				int offset = m.start();
				String contentype = null;
				try {
					contentype = docment.getContentType(offset);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(contentype != null && !XMLPartitionScanner.XML_COMMENT.equals(contentype))
				{
					String entity = m.group(0); //all
					String entitykey = m.group(1);
					if(!entitymap.containsKey(entitykey))
					{
						errormap.put(m.start(), entity.length());
					}
				}
			}			
		}
		
		return errormap;
	}
}
