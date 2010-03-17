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
	
	public Scope getScope(String name, String nodename)
	{
		ScopeStack stack =  fScopeStackMap.get(name);
		return stack.getScope(nodename);
	}
}
