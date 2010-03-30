package org.addondev.parser.javascript.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.addondev.core.AddonDevPlugin;
import org.addondev.parser.javascript.Parser;
import org.addondev.parser.javascript.ScopeManager;
import org.addondev.util.ChromeURLMap;
import org.addondev.util.FileUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

public class JavaScriptParserManager {
	
	private static JavaScriptParserManager instance;
	
	private Map<IProject, ScopeManager> projectscope = new HashMap<IProject, ScopeManager>();;
	private Map<String, ScopeManager> globalscope;
	private XULJsMap fXULJsMap = new XULJsMap();
	
	public static JavaScriptParserManager instance()
	{
		if(instance == null)
			instance = new JavaScriptParserManager();
		
		return instance;
	}	
	
	private JavaScriptParserManager()
	{
		
	}
	
	public void getGlobalScope()
	{
		
	}
	
	public ScopeManager getScopeManager(IProject project)
	{
		if(!projectscope.containsKey(project))
		{
			ScopeManager sm = new ScopeManager();
			projectscope.put(project, sm);
		}
		
		return projectscope.get(project);
	}
	
	public void parse(IProject project, IFile file, String src)
	{
		ChromeURLMap p = AddonDevPlugin.getDefault().getChromeURLMap(project, false);
		String path = p.convertLocal2Chrome(file);
		//String path = file.getFullPath().toPortableString();
		String[] jss = fXULJsMap.getList(file);
		List<String> jslist =new ArrayList<String>(Arrays.asList(jss));
		jslist.remove(path);
		
		HashMap<String, String> filemap = new HashMap<String, String>();
		for (String js : jslist) {
			String location = p.convertChrome2Local(js);
			//IFile lfile = project.getFile(location);
			filemap.put(js, location);
			
			ScopeManager sm = getScopeManager(project);
			sm.setJsLis(jslist);
			//sm.setMap(filemap);
			try {
				String text = FileUtil.getContent(new File(location));
				Parser parser = new Parser(js, sm);
				parser.parse(text);	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
		
		ScopeManager sm = getScopeManager(project);
		sm.setJsLis(jslist);
		//sm.setMap(filemap);
		Parser parser = new Parser(path, sm);
		parser.parse(src);			
	}
	
	public void getNodes(String text)
	{
		
	}
}
