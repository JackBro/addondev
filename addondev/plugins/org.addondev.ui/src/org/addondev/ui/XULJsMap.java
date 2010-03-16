package org.addondev.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.addondev.core.AddonDevPlugin;
import org.addondev.util.ChromeURLMap;
import org.addondev.util.FileUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class XULJsMap {
	//<script type="application/x-javascript;version=1.8" src="chrome://stacklink/content/stacklink.js" />
	private static Pattern fScriptPattern = Pattern.compile("<script\\s+type\\s*=\\s*\"([^\"]+)\"\\s+src\\s*=\"([^\"]+)\"\\s*\\/>", Pattern.MULTILINE);
	private Map<IProject, List<Map<String, List<String>>>> fXULJsMap;
	
	private void make(IProject project) throws CoreException, IOException
	{
		if(!fXULJsMap.containsKey(project))
		{
			ArrayList<Map<String, List<String>>> xuljsmaplist = new ArrayList<Map<String,List<String>>>();
			IResource[] resources = project.members();
			for (IResource resource : resources) {
				if(resource instanceof IFile)
				{
					IFile file = (IFile)resource;
					if("xul".equals(file.getFileExtension()))
					{
						ArrayList<String> srclist = new ArrayList<String>();
						String text = FileUtil.getContent(file.getFullPath());
						Matcher m = fScriptPattern.matcher(text);
						while(m.find())
						{
							if(m.groupCount()==3)
							{
								String jssrc =m.group(2);
								srclist.add(jssrc);
							}
						}							
						
						HashMap<String, List<String>> xuljslistmap = new HashMap<String, List<String>>();
						xuljslistmap.put(file.getFullPath().toPortableString(), srclist);
						
						xuljsmaplist.add(xuljslistmap);
					}
				}
			}
			fXULJsMap.put(project, xuljsmaplist);
		}
	}
	
	public void updateXUL()
	{
		
	}
	
	public Collection<List<String>> getList(IFile file)
	{
		IProject project = file.getProject();
		try {
			make(project);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Map<String, List<String>>> xuls = fXULJsMap.get(project);
		if(xuls != null)
		{
			ChromeURLMap p = AddonDevPlugin.getDefault().getChromeURLMap(project, false);
			String chromeurl = p.convertLocal2Chrome(file);
			
			for (Map<String, List<String>> xulmap : xuls) {
				Collection<List<String>> jslists = xulmap.values();
				if(jslists.contains(chromeurl))
				{
					//ArrayList<String> re = new ArrayList<String>();
					return jslists;
				}
			}
		}
		return null;
	}
}
