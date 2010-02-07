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
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.addondev.util.ChromeURLMap;
import org.addondev.util.FileUtil;
import org.addondev.util.Locale;
import org.addondev.util.StringUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class XULParser {
		
	private static Pattern doctypeOverlayPattern = Pattern.compile("<!DOCTYPE\\s+overlay\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>", Pattern.MULTILINE);
	private static Pattern doctypeprefwindowPattern = Pattern.compile("<!DOCTYPE\\s+prefwindow\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>", Pattern.MULTILINE);
	private static Pattern doctypePattern = Pattern.compile("<!DOCTYPE\\s+.*\\s+\"([^\"]+)\"\\s*>", Pattern.MULTILINE);
	private static Pattern doctypepackPattern = Pattern.compile("(chrome:\\/\\/[^\\/]+)\\/.+", Pattern.MULTILINE);
	private static Pattern stylesheetPattern = Pattern.compile("<\\?xml-stylesheet\\s+href=\\s*\"([^\"]+)\"\\s*.*\\?>", Pattern.MULTILINE);
	private static Pattern entryPattern = Pattern.compile("\"&(.+);\"");

	public static ArrayList<String> parse(IProject project, Locale locale, IFile file) throws IOException, CoreException
	{
		String text = FileUtil.getContent(file.getContents());
		
		ArrayList<String> xuldatalist = new ArrayList<String>();
		int decstart = 0;
		int decend = 0;
		
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
						
						decstart = 0;
						decend = node.getOffset();
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
					xuldatalist.add(convertText(project, locale, file, sb.toString(), decstart, decend));
				}			
			}
			else
			{
				xuldatalist.add(convertText(project, locale, file, text, decstart, decend));
			}
		}
		
		return xuldatalist;
	}
	
	private static String convertText(IProject project, Locale locale,  IFile file, String xml, int decstart, int decend)
	{
		//if()
		//{
		//	text = text.replaceFirst("chrome://stacklink/skin/preference.css", "file:///D:/data/src/PDE/workrepository/work/stacklink/skin/classic/preference.css");
		//}
		//return text;
		xml = convertChrome2LocalDec(project, locale, file, xml, decstart, decend);
		//data url
		return xml.replaceAll("'", "&apos;").replaceAll("\n", " ");
		
		//dom
		//return text.replaceAll("'", "&apos;").replaceAll("\n", "");//replaceAll("\n", "\\\\n");
	}
	
	public static String convertChrome2LocalDec(IProject project, Locale locale, IFile file, String xml, int decstart, int decend)
	{
		ArrayList<Pattern> patterns = new ArrayList<Pattern>();
		//patterns.add(doctypeOverlayPattern);
		//patterns.add(doctypeprefwindowPattern);
		patterns.add(doctypePattern);
		patterns.add(stylesheetPattern);
		
		String dectext = xml.substring(decstart, decend);
		String ret = dectext;
		ChromeURLMap map = AddonDevPlugin.getDefault().getChromeURLMap(project, false);
		
//		String strlocale = null;
//		try {
//			strlocale = project.getPersistentProperty(new QualifiedName(AddonDevUIPrefConst.LOCALE , "LOCALE"));
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			//e.printStackTrace();
//		}
//		if(strlocale == null)
//		{
//			strlocale = map.getLocaleList().get(0).getName();
//		}
//		Locale locale = Locale.getLocale(strlocale);
		
		for (Pattern pattern : patterns) {
			Matcher m = pattern.matcher(dectext);
			while(m.find())
			{
				if(m.groupCount() == 1)
				{
					if(m.pattern().equals(doctypePattern))
					{
						Matcher pm = doctypepackPattern.matcher(m.group(0));
						if(pm.find())
						{
							String restr = pm.group(1) + locale.getName();
							String res = m.group(0).replaceFirst(pm.group(1), restr);
							ret = ret.replaceAll(m.group(0), res);
							//System.out.println("lacate path = " + res);
						}						
					}
					else
					{
						String path = m.group(1);
						if(!path.contains("/"))
						{
							String local = file.getLocation().removeLastSegments(1).append(path).toPortableString();
							local = "file:///" + local;
							ret = ret.replaceAll(path, local);
							int i=0;
							i++;
						}
						else
						{
							String local = map.convertChrome2Local(path, "file:///");
							if(local !=null)
							{
								//m.appendReplacement(buf, local);
								ret = ret.replaceAll(path, local);
							}							
						}
					}
				}
				else
				{
					break;
				}
			}			
		}
//		String mm = xml.replace(dectext, ret);
//		return xml;
		
		return xml.replace(dectext, ret);
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
