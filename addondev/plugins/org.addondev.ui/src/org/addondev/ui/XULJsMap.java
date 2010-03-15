package org.addondev.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.addondev.util.FileUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

public class XULJsMap {
	//<script type="application/x-javascript;version=1.8" src="chrome://stacklink/content/stacklink.js" />
	private static Pattern fScriptPattern = Pattern.compile("<script\\s+type\\s*=\\s*\"([^\"]+)\"\\s+src\\s*=\"([^\"]+)\"\\s*\\/>", Pattern.MULTILINE);
	private Map<IProject, List<Map<String, List<String>>>> fXULJsMap;
	
	public void make(IProject project)
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
						String text = FileUtil.getContent(file);
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
	
}
