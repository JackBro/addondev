package org.addondev.parser.javascript.util;

import java.util.HashMap;
import java.util.Map;

import org.addondev.parser.javascript.Parser;
import org.addondev.parser.javascript.ScopeManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

public class JavaScriptManager {
	
	private Map<IProject, ScopeManager> projectscope = new HashMap<IProject, ScopeManager>();;
	private Map<String, ScopeManager> globalscope;
	
	public void getGlobalScope()
	{
		
	}
	
	public ScopeManager getScopeManager(IProject project)
	{
		if(!projectscope.containsKey(project))
		{
			ScopeManager sm = new ScopeManager();
			
		}
	}
	
	public void parse(IProject project, IFile file)
	{
		
	}
	
	public void parse(IProject project, String chromepath, String src)
	{
		Parser parser = new Parser(chromepath);
		parser.parse(src);	
		
	}
}
