package org.addondev.parser.javascript.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.addondev.core.AddonDevPlugin;
import org.addondev.parser.javascript.Parser;
import org.addondev.parser.javascript.ScopeManager;
import org.addondev.util.ChromeURLMap;
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
		List<String> jslist = fXULJsMap.getList(file);
		//ChromeURLMap p = AddonDevPlugin.getDefault().getChromeURLMap(project, false);
		//String path = p.convertLocal2Chrome(file);
		String path = file.getFullPath().toPortableString();
		
		ScopeManager sm = getScopeManager(project);
		sm.setJsLis(jslist);
		Parser parser = new Parser(path, sm);
		parser.parse(src);			
	}
	
	public void getNodes()
	{
		
	}
}
