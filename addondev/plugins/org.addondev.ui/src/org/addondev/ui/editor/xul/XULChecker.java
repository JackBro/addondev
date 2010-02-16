package org.addondev.ui.editor.xul;

import java.io.File;
import java.io.FileNotFoundException;
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
import org.addondev.util.ChromeURLMap;
import org.addondev.util.FileUtil;
import org.addondev.util.Locale;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class XULChecker {
		
	private static Pattern doctypeOverlayPattern = Pattern.compile("<!DOCTYPE\\s+overlay\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>", Pattern.MULTILINE);
	private static Pattern doctypeprefwindowPattern = Pattern.compile("<!DOCTYPE\\s+prefwindow\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>", Pattern.MULTILINE);
	private static Pattern stylesheetPattern = Pattern.compile("<\\?xml-stylesheet\\s+href=\\s*\"([^\"]+)\"\\s*.*\\?>", Pattern.MULTILINE);
	private static Pattern entryPattern = Pattern.compile("\"&(.+);\"");
	
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
