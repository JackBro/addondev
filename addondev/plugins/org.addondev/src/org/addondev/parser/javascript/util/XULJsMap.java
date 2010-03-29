package org.addondev.parser.javascript.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.addondev.core.AddonDevPlugin;
import org.addondev.util.ChromeURLMap;
import org.addondev.util.FileUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

public class XULJsMap {
	//<script type="application/x-javascript;version=1.8" src="chrome://stacklink/content/stacklink.js" />
	private static Pattern fScriptPattern = Pattern.compile("<script\\s+type\\s*=\\s*\"([^\"]+)\"\\s+src\\s*=\"([^\"]+)\"\\s*\\/>", Pattern.MULTILINE);
	private Map<IProject, List<Map<String, List<String>>>> fXULJsMap;
	
	public XULJsMap()
	{
		fXULJsMap = new HashMap<IProject, List<Map<String,List<String>>>>();
	}
	
	private void make(IProject project) throws CoreException, IOException
	{
		if(!fXULJsMap.containsKey(project))
		{
			final ArrayList<Map<String, List<String>>> xuljsmaplist = new ArrayList<Map<String,List<String>>>();
			
			project.accept(new IResourceVisitor() {
				
				@Override
				public boolean visit(IResource resource) throws CoreException {
					// TODO Auto-generated method stub
				    switch (resource.getType()) {
				    case IResource.FILE:
				    	IFile file = (IFile)resource;
						if("xul".equals(file.getFileExtension()))
						{
							ArrayList<String> srclist = new ArrayList<String>();
							String text = null;
							try {
								text = FileUtil.getContent(file.getContents());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								break;
							}
							Matcher m = fScriptPattern.matcher(text);
							while(m.find())
							{
								//if(m.groupCount()==3)
								{
									String jssrc =m.group(2);
									srclist.add(jssrc);
								}
							}							
							
							HashMap<String, List<String>> xuljslistmap = new HashMap<String, List<String>>();
							xuljslistmap.put(file.getFullPath().toPortableString(), srclist);
							
							xuljsmaplist.add(xuljslistmap);
						}				    	
				    	break;
				    }
					return true;
				}
			});
			fXULJsMap.put(project, xuljsmaplist);
			
//			IResource[] resources = project.members();
//			for (IResource resource : resources) {
//				if(resource instanceof IFile)
//				{
//					IFile file = (IFile)resource;
//					if("xul".equals(file.getFileExtension()))
//					{
//						ArrayList<String> srclist = new ArrayList<String>();
//						String text = FileUtil.getContent(file.getFullPath());
//						Matcher m = fScriptPattern.matcher(text);
//						while(m.find())
//						{
//							if(m.groupCount()==3)
//							{
//								String jssrc =m.group(2);
//								srclist.add(jssrc);
//							}
//						}							
//						
//						HashMap<String, List<String>> xuljslistmap = new HashMap<String, List<String>>();
//						xuljslistmap.put(file.getFullPath().toPortableString(), srclist);
//						
//						xuljsmaplist.add(xuljslistmap);
//					}
//				}
//			}
//			fXULJsMap.put(project, xuljsmaplist);
		}
	}
	
	public void updateXUL()
	{
		
	}
	
	/**
	 * 
	 * @param file js file
	 * @return js files(xul)
	 */
	public List<String> getList(IFile file)
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
			
			//String bpath = "D:/data/src/PDE/workrepository/work/stacklink"; 
			//String dd = p.convertLocal2Chrome(new Path(bpath).append("/chrome/content/stacklink.js"));
			
			
			for (Map<String, List<String>> xulmap : xuls) {
				Set<String> keys = xulmap.keySet();
				for (String key : keys) {
					List<String> jslist = xulmap.get(key);
					if(jslist.contains(chromeurl))
					{
						return jslist;
					}
				}
			}
		}
		return null;
	}
}
