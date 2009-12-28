package org.addondev.unittest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.addondev.plugin.AddonDevPlugin;
import org.addondev.util.ChromeURLMap;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AddonDevTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void ChromeURLMapTest() {
		//IProject[] s = ResourcesPlugin.getWorkspace().getRoot().getProjects();

		//IProject project = AddonDevPlugin.getWorkspace().getRoot().getProject("stacklink");
		//boolean pe = project.exists();
		ChromeURLMap cm = new ChromeURLMap();	
		try {
			URL url = AddonDevPlugin.getDefault().getBundle().getEntry("stacklink/chrome.manifest");
			//project.getFile(ChromeURLMap.MANIFEST_FILENAME).
			//cm.readManifest(project.getFile(ChromeURLMap.MANIFEST_FILENAME));
			String bpath = "D:/data/src/PDE/workrepository/plugins/org.addondev.unittest/stacklink"; 
			cm.readManifest(new Path(bpath), url.openStream());
			
			//String pp = cm.convertChrome2Local("chrome://stacklink/content/stacklink.js");
			assertEquals("file:///" + bpath + "/chrome/content/stacklink.js", cm.convertChrome2Local("chrome://stacklink/content/stacklink.js"));
			assertEquals("file:///" + bpath + "/skin/classic/preference.css", cm.convertChrome2Local("chrome://stacklink/skin/preference.css"));
			
			cm.setLocale("en-US");
			assertEquals("file:///" + bpath + "/locale/en-US/stacklink.dtd", cm.convertChrome2Local("chrome://stacklink/locale/stacklink.dtd"));
			cm.setLocale("ja-JP");
			assertEquals("file:///" + bpath + "/locale/ja-JP/stacklink.dtd", cm.convertChrome2Local("chrome://stacklink/locale/stacklink.dtd"));
			
			assertEquals("chrome://stacklink/content/stacklink.js", cm.convertLocal2Chrome(new Path(bpath).append("/chrome/content/stacklink.js")));
			assertEquals("chrome://stacklink/content/tmp/tmp.js", cm.convertLocal2Chrome(new Path(bpath).append("chrome/content/tmp/tmp.js")));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
