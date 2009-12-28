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
			//url.
			URI ui = url.toURI();
			//project.getFile(ChromeURLMap.MANIFEST_FILENAME).
			//cm.readManifest(project.getFile(ChromeURLMap.MANIFEST_FILENAME));
			cm.readManifest("stacklink", url.openStream());
			String lo = cm.convertChrome2Local("chrome://stacklink/content/stacklink.js");
			File f = new File(lo);
			String ap = f.getAbsolutePath();
			//IFile lofile = project.getFile("stacklink.js");
			//String ch = cm.convertLocal2Chrome(lofile);
			String ch = cm.convertLocal2Chrome("stacklink/chrome/content/stacklink.js");

			String ch2 = cm.convertLocal2Chrome("stacklink/chrome/content/tmp/tmp.js");
			
			int i=0;
			i++;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
