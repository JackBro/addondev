package org.addondev.ui.editor.xul.preview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import org.addondev.util.ChromeURLMap;
import org.addondev.util.FileUtil;
import org.addondev.util.Locale;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class XULParser {
		
	private static Pattern doctypePattern = Pattern.compile("<!DOCTYPE\\s+.*\\s+\"([^\"]+)\"\\s*>", Pattern.MULTILINE);
	private static Pattern doctypepackPattern = Pattern.compile("(chrome:\\/\\/[^\\/]+)\\/.+", Pattern.MULTILINE);
	private static Pattern stylesheetPattern = Pattern.compile("<\\?xml-stylesheet\\s+href=\\s*\"([^\"]+)\"\\s*.*\\?>", Pattern.MULTILINE);

	private EnumXULWindow fWindowType;
	private IProject fProject;
	private Locale fLocale;
	private IFile fFile;
	
	private ArrayList<String> fXULs;
	
	public EnumXULWindow getWindowType()
	{
		return fWindowType;
	}
	
	public List<String> getXULs()
	{
		return fXULs;
	}
	
	public void parse(IProject project, Locale locale, IFile file) throws IOException, CoreException
	{
		fProject = project;
		fLocale = locale;
		fFile = file;
		fWindowType = EnumXULWindow.NONE;
		
		String text = FileUtil.getContent(file.getContents());
		
		fXULs = new ArrayList<String>();
		
		int decstart = 0;
		int decend = 0;
		
		String ElementName = null;
		FuzzyXMLElement targetelement = null;
		FuzzyXMLParser parser = new FuzzyXMLParser();
		FuzzyXMLDocument document = parser.parse(text);
		FuzzyXMLElement element = document.getDocumentElement();
		
		if(element.hasChildren())
		{
			HashSet<String> set = new HashSet<String>();
			set.add("prefwindow");
			set.add("window");
			set.add("dialog");
			set.add("page");
			for (FuzzyXMLNode node : element.getChildren()) {
				if(node instanceof FuzzyXMLElement)
				{
					String name = ((FuzzyXMLElement)node).getName();
					name = name.toLowerCase();
					if(set.contains(name))
					{
						ElementName = name;
						targetelement = ((FuzzyXMLElement)node);
						fWindowType = EnumXULWindow.getXULWindow(name);
							
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
					fXULs.add(convertText(sb.toString(), decstart, decend));
				}			
			}
			else
			{
				fXULs.add(convertText(text, decstart, decend));
			}
		}
		
		//return xuldatalist;
	}
	
	private String convertText(String xml, int decstart, int decend) throws FileNotFoundException
	{
		//if()
		//{
		//	text = text.replaceFirst("chrome://stacklink/skin/preference.css", "file:///D:/data/src/PDE/workrepository/work/stacklink/skin/classic/preference.css");
		//}
		//return text;
		
		xml = convertChrome2LocalDec(xml, decstart, decend);
		//data url
		return xml.replaceAll("'", "&apos;").replaceAll("\n", " ");
		
		//dom
		//return text.replaceAll("'", "&apos;").replaceAll("\n", "");//replaceAll("\n", "\\\\n");
	}
	
	private String convertChrome2LocalDec(String xml, int decstart, int decend) throws FileNotFoundException
	{
		ArrayList<Pattern> patterns = new ArrayList<Pattern>();
		//patterns.add(doctypeOverlayPattern);
		//patterns.add(doctypeprefwindowPattern);
		patterns.add(doctypePattern);
		patterns.add(stylesheetPattern);
		
		String dectext = xml.substring(decstart, decend);
		String ret = dectext;
		ChromeURLMap map = AddonDevPlugin.getDefault().getChromeURLMap(fProject, false);
		
		for (Pattern pattern : patterns) {
			Matcher m = pattern.matcher(dectext);
			while(m.find())
			{
				if(m.groupCount() == 1)
				{
					if(m.pattern().equals(doctypePattern)) //dtd
					{
						if(m.group(1) != null) //path
						{
							String dtdpath = map.convertChrome2Local(m.group(1));
							if(!new File(dtdpath).exists())
							{
								throw new FileNotFoundException("notfild");
							}
						}
						
						Matcher pm = doctypepackPattern.matcher(m.group(0));
						if(pm.find())
						{
							String restr = pm.group(1) + fLocale.getName();
							String res = m.group(0).replaceFirst(pm.group(1), restr);
							ret = ret.replaceAll(m.group(0), res);
						}						
					}
					else //css
					{
						String path = m.group(1);
						if(!path.contains("/"))
						{
							String local = fFile.getLocation().removeLastSegments(1).append(path).toPortableString();
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
}
