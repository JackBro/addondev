package org.addondev.parser.javascript;

import java.util.ArrayList;

public class ScopeStack {
	private ArrayList<Scope> fScopeStack;
	private int stackindex;
	
	public ArrayList<Scope> getScopeList()
	{
		return fScopeStack;
	}
	
	public ScopeStack()
	{
		fScopeStack = new ArrayList<Scope>();
		stackindex = -1;
	}
	
	public Scope getScope(int index)
	{
		return fScopeStack.get(index);
	}
	
	public Scope getCurrntScope()
	{
		return fScopeStack.get(stackindex);
	}
	
	public void pushScope(Scope scope)
	{
		fScopeStack.add(scope);
		//stackindex = fScopeStack.size()-1;
		stackindex++;
	}
	
	public Scope popScope()
	{
		//fScopeStack.remove(stackindex);
		Scope scope = getCurrntScope();
		stackindex--;
		return scope;
	}
}
