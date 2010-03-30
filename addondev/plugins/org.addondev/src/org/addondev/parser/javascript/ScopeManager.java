package org.addondev.parser.javascript;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.addondev.core.AddonDevPlugin;
import org.addondev.util.ChromeURLMap;
import org.addondev.util.FileUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class ScopeManager {
	
	private List<String> fJsList;
	private Map<String, String> fJsLocationMap;
	//private static ScopeManager instance;	
	private HashMap<String, ScopeStack> fScopeStackMap;

	public void setJsLis(List<String> jslist)
	{
		fJsList = jslist;
	}
	
	public List<String> getJsLis()
	{
		return fJsList;
	}
	
	public void setMap(Map<String, String> map)
	{
		fJsLocationMap = map;
	}
//	public static ScopeManager instance()
//	{
//		if(instance == null)
//			instance = new ScopeManager();
//		
//		return instance;
//	}	
	
	public ScopeManager()
	{
		fScopeStackMap = new HashMap<String, ScopeStack>();
	}
	
	public void setScopeStack(String name, ScopeStack scopestack)
	{
		if(fScopeStackMap.containsKey(name))
		{
			//fScopeStackMap.get(name).clear();
			fScopeStackMap.remove(name);
		}	
		fScopeStackMap.put(name, scopestack);		
	}
	
//	public JsNode getGlobalNode(String name)
//	{
//		JsNode node = null;
//		for(ScopeStack n : fScopeStackMap.values()) {
//			node = n.getScope(0).getNode(name);
//			if(node != null)
//				break;
//		}
//		return node;		
//	}
	
	public JsNode getGlobalNode(String name)
	{
		JsNode node = null;

		for(String key : fScopeStackMap.keySet()) {
			if(fJsList.contains(key))
			{
				if(fScopeStackMap.containsKey(key))
				{
					node = fScopeStackMap.get(key).getScope(0).getNode(name);
					if(node != null)
						break;
				}
				else
				{
					
				}
			}
			
			else
			{


			}
		}
		return node;		
	}
	
	public List<Scope> getGlobalScopes()
	{
		ArrayList<Scope> scopelist = new ArrayList<Scope>();

		for(String key : fScopeStackMap.keySet()) {
			if(fJsList.contains(key))
			{
				scopelist.add(fScopeStackMap.get(key).getScope(0));
			}
		}
		return scopelist;		
	}
	
	public Scope getScope(String name, int offset)
	{
		if(fScopeStackMap.containsKey(name))
		{
			ScopeStack stack =  fScopeStackMap.get(name);
			ArrayList<Scope> scopes = stack.getScopeList();
			if(offset == 0) return scopes.get(0);
			
			Scope minscope = null;
			int spminlen = -1;
			int splen;
			for (Scope scope : scopes) {
				if(offset >= scope.getStart() && offset<=scope.getEnd()){
					splen = scope.getEnd() - scope.getStart(); 
					if(spminlen < 0){
						spminlen = splen;
						minscope = scope;
					}
					else if(spminlen > splen){
						spminlen = splen;
						minscope = scope;
					}
				}
			}
			
			return minscope;
		}

		return null;
	}
	
	public List<Scope> getUpScopes(String srcname, Scope targetscope)
	{
		ArrayList<Scope> scopes = new ArrayList<Scope>();
		ScopeStack stack =  fScopeStackMap.get(srcname);

		for (Scope scope : stack.getScopeList()) {
			if((scope.getStart() < targetscope.getStart()) 
					&& scope.getEnd() >= targetscope.getEnd())
			{
				scopes.add(scope);
			}
		}
		return scopes;
	}
}
