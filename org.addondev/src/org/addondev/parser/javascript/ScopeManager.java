package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

public class ScopeManager {
	
	private static ScopeManager instance;

	private HashMap<String, Scope> fScopeMap;

	
	public static ScopeManager instance()
	{
		if(instance == null)
			instance = new ScopeManager();
		
		return instance;
	}	
	
	private ScopeManager()
	{
		fScopeMap = new HashMap<String, Scope>();
	}
	
	public void setScope(String name, Scope scope)
	{
		if(fScopeMap.containsKey(name))
		{
			fScopeMap.remove(name);
		}	
		fScopeMap.put(name, scope);		
	}
	
	public JsNode getNode(String image)
	{
		JsNode node = null;
		for(Scope n : fScopeMap.values()) {
			node = n.getNode(image);
			if(node != null)
				break;
		}
		return node;		
	}
}
