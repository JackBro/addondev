package gef.example.helloworld.util;

import org.addondev.core.AddonDevPlugin;
import org.addondev.util.ChromeURLMap;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

public class Util {
	public static IFile getFile(IProject project, String chromeurl){
		ChromeURLMap map = AddonDevPlugin.getDefault().getChromeURLMap(project, false);
		return map.convertChrome2File(chromeurl);
	}
}
