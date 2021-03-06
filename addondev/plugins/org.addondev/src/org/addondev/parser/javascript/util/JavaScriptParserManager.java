package org.addondev.parser.javascript.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.addondev.core.AddonDevPlugin;
import org.addondev.parser.javascript.IFunction;
import org.addondev.parser.javascript.ImportFunction;
import org.addondev.parser.javascript.InterfaceFunction;
import org.addondev.parser.javascript.Parser;
import org.addondev.parser.javascript.ScopeManager;
import org.addondev.util.ChromeURLMap;
import org.addondev.util.FileUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.osgi.framework.Bundle;

public class JavaScriptParserManager {
	
	private static JavaScriptParserManager instance;
	
	private Map<IProject, ScopeManager> projectscope = new HashMap<IProject, ScopeManager>();;
	private Map<String, ScopeManager> globalscope;
	private ScopeManager globalsm;
	private XULJsMap fXULJsMap = new XULJsMap();
	
	private Map<String, IFunction> functions;// = new HashMap<String, IFunction>();
	
	
	public Map<String, IFunction> getFunctions() {
		return functions;
	}

	public static JavaScriptParserManager instance()
	{
		if(instance == null)
			instance = new JavaScriptParserManager();
		
		return instance;
	}	
	
	private JavaScriptParserManager()
	{
//		functions.put("interfaces", new InterfaceFunction());
//		functions.put("import", new ImportFunction());
//		
//		globalsm = new ScopeManager();
//		Bundle bundle = AddonDevPlugin.getDefault().getBundle();
//		List<String> list = Arrays.asList("system.js");
//		for (String string : list) {
//			InputStream input;
//			try {
//				input = bundle.getEntry("lib/javascript/" + string).openStream();
//				String src = FileUtil.getContent(input);
//				Parser parser = new Parser(string, globalsm);
//				parser.parse(src);		
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		init();
	}
	
	public void init(){
		
		if(functions != null) return;
		functions = new HashMap<String, IFunction>();
		functions.put("interfaces", new InterfaceFunction());
		functions.put("import", new ImportFunction());
		
		globalsm = new ScopeManager();
		Bundle bundle = AddonDevPlugin.getDefault().getBundle();
		List<String> list = Arrays.asList("system.js");
		for (String string : list) {
			InputStream input;
			try {
				input = bundle.getEntry("lib/javascript/" + string).openStream();
				String src = FileUtil.getContent(input);
				Parser parser = new Parser(string, globalsm);
				parser.parse(src);		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		globalsm.setJsLis(list);
	}
	
	public ScopeManager getGlobalScopeManager(){
		return globalsm;
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
	
	public void parse(String name, String src){
		
	}
	
	public void parse(IProject project, IFile file, String src)
	{
		ChromeURLMap p = AddonDevPlugin.getDefault().getChromeURLMap(project, false);
		String path = p.convertLocal2Chrome(file);
		String[] jss = fXULJsMap.getList(file);
		List<String> jslist =new ArrayList<String>(Arrays.asList(jss));
		jslist.remove(path);
		
		HashMap<String, String> filemap = new HashMap<String, String>();
		for (String js : jslist) {
			String location = p.convertChrome2Local(js);
			filemap.put(js, location);
			
			ScopeManager sm = getScopeManager(project);
			sm.setJsLis(jslist);
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
		Parser parser = new Parser(path, sm);
		parser.parse(src);			
	}
}
