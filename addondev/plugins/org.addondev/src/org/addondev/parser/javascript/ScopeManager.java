package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.HashMap;

public class ScopeManager {
	
	private static ScopeManager instance;	
	private HashMap<String, ScopeStack> fScopeStackMap;

	
	public static ScopeManager instance()
	{
		if(instance == null)
			instance = new ScopeManager();
		
		return instance;
	}	
	
	private ScopeManager()
	{
		fScopeStackMap = new HashMap<String, ScopeStack>();
	}
	
	public void setScopeStack(String name, ScopeStack scopestack)
	{
		if(fScopeStackMap.containsKey(name))
		{
			fScopeStackMap.remove(name);
		}	
		fScopeStackMap.put(name, scopestack);		
	}
	
	public JsNode getGlobalNode(String image)
	{
		JsNode node = null;
		for(ScopeStack n : fScopeStackMap.values()) {
			node = n.getScope(0).getNode(image);
			if(node != null)
				break;
		}
		return node;		
	}
	
	public Scope getScope(String name, int offset)
	{
		if(fScopeStackMap.containsKey(name))
		{
			Scope tmp = null;
			ScopeStack stack =  fScopeStackMap.get(name);
			ArrayList<Scope> scopes = stack.getScopeList();
			if(offset == 0) return scopes.get(0);
			
			for (Scope scope : scopes) {
				if(scope.getStart() == scope.getEnd()) continue;
				
				if(offset >= scope.getStart() && offset<=scope.getEnd())
				{
					tmp = scope;
				}
				else
				{
					return tmp;
				}
			}
			return tmp;
		}

		return null;
	}
}
