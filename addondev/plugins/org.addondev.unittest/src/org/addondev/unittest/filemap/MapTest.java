package org.addondev.unittest.filemap;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.addondev.core.AddonDevPlugin;
import org.addondev.parser.dtd.DTDMap;
import org.addondev.unittest.Activator;
import org.addondev.util.ChromeURLMap;
import org.addondev.util.FileUtil;
import org.addondev.util.Locale;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MapTest {

	private IPath basepath = new Path("D:/data/src/PDE/workrepositry/plugins/org.addondev.unittest");
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void ChromeURLMapTest() throws CoreException, IOException {
		IWorkspace work = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root =  work.getRoot();
		IProject project = root.getProject("stacklink");
		
		if (!project.exists()) project.create(null);
		if (!project.isOpen()) project.open(null);

		ChromeURLMap cm = new ChromeURLMap();	
		cm.readManifest(project);
		
//		String bpath = "D:/data/src/PDE/work/"; 
//		String filepath = "stacklink/chrome.manifest"; 
//		cm.readManifest(new File(bpath + filepath));
//		
		String pp = cm.convertChrome2Local("chrome://stacklink/content/stacklink.js");
		
		String css = cm.convertChrome2Local("chrome://stacklink/skin/preference.css");
		
		IFile file = cm.convertChrome2File("chrome://stacklink/content/stacklink.js");
		assertTrue(file.exists());
		
//		assertEquals("file:///" + bpath + "/chrome/content/stacklink.js", cm.convertChrome2Local("chrome://stacklink/content/stacklink.js"));
//		assertEquals("file:///" + bpath + "/skin/classic/preference.css", cm.convertChrome2Local("chrome://stacklink/skin/preference.css"));
//		
//		//cm.setLocale("en-US");
//		assertEquals("file:///" + bpath + "/locale/en-US/stacklink.dtd", cm.convertChrome2Local("chrome://stacklink/locale/stacklink.dtd"));
//		//cm.setLocale("ja-JP");
//		assertEquals("file:///" + bpath + "/locale/ja-JP/stacklink.dtd", cm.convertChrome2Local("chrome://stacklink/locale/stacklink.dtd"));
	}
	
	@Test
	public void DTDMapTest() {
		DTDMap dtdmap = new DTDMap();

		String enfullpath = "stacklink/locale/en-US/stacklink.dtd"; 
		//dtdmap.setLocate("en-US");
		dtdmap.parse("en-US", basepath.append(enfullpath));
		
		assertEquals("Preference", dtdmap.getWord("stacklink.pref"));
		assertEquals("View label", dtdmap.getWord("stacklink.pref.label"));
		
		String jafullpath = "stacklink/locale/ja-JP/stacklink.dtd"; 
		//dtdmap.setLocate("ja-JP");
		dtdmap.parse("ja-JP", basepath.append(jafullpath));
		assertEquals("stacklinkの設定", dtdmap.getWord("stacklink.pref"));
		assertEquals("タイトルの表示", dtdmap.getWord("stacklink.pref.label"));
		
		dtdmap.setLocate("en-US");
		assertEquals("Preference", dtdmap.getWord("stacklink.pref"));
		assertEquals("View label", dtdmap.getWord("stacklink.pref.label"));
	}
	
	@Test
	public void XULTest() {
		//XULParser p = new XULParser();
		//String fullpath = "stacklink/chrome/content/preference.xul"; 
		//p.parse(basepath.append(fullpath), 0);
		
		//--register-global 
		//--unregister-global 
		//--register-user
		//--unregister-user 
		
		String xulpath = "D:/program/xulrunner";
		IPath path = new Path(xulpath ).append("xulrunner");
		String fullpath = path.toOSString();

		ProcessBuilder pb = new ProcessBuilder(fullpath,"--register-global");
		
		try {
			Process p = pb.start();
			int ret = p.waitFor();
			
			IPath filepath = new Path(xulpath ).append("global.reginfo");
			File file = filepath.toFile();
			if(file.exists())
			{
				System.out.println("exists");
			}
			else
			{
				System.out.println("not exists");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void dtdregtest()
	{
		Locale l = Locale.getLocale("da-DK");
		
		Pattern doctypePattern = Pattern.compile("<!DOCTYPE\\s+.*\\s+\"([^\"]+)\"\\s*>", Pattern.MULTILINE);
		Pattern doctypepackPattern = Pattern.compile("(chrome:\\/\\/[^\\/]+)\\/.+", Pattern.MULTILINE);
		Pattern stylesheetPattern = Pattern.compile("<\\?xml-stylesheet\\s+href=\\s*\"([^\"]+)\"\\s*.*\\?>", Pattern.MULTILINE);
		
		ArrayList<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(doctypePattern);
		patterns.add(stylesheetPattern);
		
		String text = 
			"<?xml-stylesheet href=\"chrome://mozapps/content/preferences/preferences.css\"?>\n"
			+"<?xml-stylesheet href=\"chrome://stacklink/skin/preference.css\" type=\"text/css\"?>\n"
			+ "<!DOCTYPE prefwindow SYSTEM \"chrome://stacklink/locale/stacklink.dtd\" >";
		String ret = text;
		for (Pattern pattern : patterns) {
			Matcher m = pattern.matcher(text);
			//
			System.out.println("cnt = " + m.groupCount());
			while(m.find())
			{
				if(m.pattern().equals(doctypePattern))
				{
					Matcher m2 = doctypepackPattern.matcher(m.group(0));
					if(m2.find())
					{
						//ret = ret.replaceAll(m2.group(1), "local");
						System.out.println("m2.group(0) = " + m2.group(0));
						System.out.println("m2.group(1) = " + m2.group(1));
						String restr = m2.group(1) + "JP";
						String res = m.group(0).replaceFirst(m2.group(1), restr);
						System.out.println("lacate path = " + res);
					}
				}
				else
				{
					System.out.println(m.group(0));
					System.out.println(m.group(1));
					//ret = ret.replaceAll(m.group(1), "local");
				}
			}
		}
		System.out.println("ret = " + ret);
//		Pattern entryPattern = Pattern.compile("\"&(.+);\"");
//		String text = "<prefpane id=\"appearance\" label=\"&stacklink.pref.appearance;\" flex=\"1\">";
//		Matcher m = entryPattern.matcher(text);
//		while(m.find())
//		{
//			System.out.println(m.group(0));
//			System.out.println(m.group(1));
//		}
	}
	
//	@Test
//	public void parserTest()
//	{
//		String text = null;
//		try {
//			text = FileUtil.getContent(MapTest.class.getResourceAsStream("test.xml"));
//			FuzzyXMLParser parser = new FuzzyXMLParser();
//			
//			parser.addErrorListener( new Validator());
//			FuzzyXMLDocument document = parser.parse(text);
//			System.out.println("FuzzyXMLDocument document = parser.parse(text);");
//			int i=0;
//			i++;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
