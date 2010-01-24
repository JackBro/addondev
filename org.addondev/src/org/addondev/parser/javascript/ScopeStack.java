package org.addondev.parser.javascript;

import java.util.ArrayList;

public class ScopeStack {
	private ArrayList<Scope> fScopeStack;
	private int stackindex;
	
	public ScopeStack()
	{
		fScopeStack = new ArrayList<Scope>();
		stackindex = 0;
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
		stackindex = fScopeStack.size()-1;
	}
	
	public Scope popScope()
	{
		//fScopeStack.remove(stackindex);
		Scope scope = getCurrntScope();
		stackindex--;
		return scope;
	}
}
