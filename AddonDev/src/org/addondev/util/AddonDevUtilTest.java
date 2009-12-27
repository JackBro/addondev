package org.addondev.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


import org.addondev.plugin.AddonDevPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Test;

public class AddonDevUtilTest {

	@Test
	public void testGetPath() {
		//fail("Not yet implemented");
//		try {
//			IPath uri = new Path("D:\\program\\firefox35\\35chomebugtest\\extensions\\hello@xuldev.org");
//			String test = new u
//			URL test2 = new URL(uri);
//			String stest2 = test2.getPath();
//			int i=0;
//			i++;
//			
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		IProject project = AddonDevPlugin.getWorkspace().getRoot().getProject("stacklink");
		ChromeURLMap cm = new ChromeURLMap();
		//		String content ="content 	stacklink	chrome/content/ xpcnativewrappers=yes";
//		String skin ="skin	stacklink	classic/1.0	skin/classic/";
//		String locate ="locale	stacklink	en-US		locale/en-US/";
//		cm.makeContentMap(content);
//		//cm.makeSkinMap(skin);
//		//cm.makeLocaleMap(locate);
//		
//		String chromepath = "chrome://stacklink/content/stacklink.js";
//		String local = cm.convertChrome2Local(chromepath);
//		System.out.println(local);
		String cntent = cm.convertChrome2Local("chrome://stacklink/content/stacklink.js");
		String skin = cm.convertChrome2Local("chrome://stacklink/skin/preference.css");
		String locale = cm.convertChrome2Local("chrome://stacklink/locale/stacklink.dtd");
		
		System.out.println(cntent);
		System.out.println(skin);
		System.out.println(locale);
		
	}

}
