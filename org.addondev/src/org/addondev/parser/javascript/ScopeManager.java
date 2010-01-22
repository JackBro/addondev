package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.Stack;

public class ScopeManager {
	private ArrayList<Scope> fScopeStack;
	private int stackindex;
	
	public Scope getScope()
	{
		return fScopeStack.get(stackindex);
	}
	
	public ScopeManager()
	{
		fScopeStack = new ArrayList<Scope>();
		stackindex = 0;
	}
	
	public void pushScope(Scope scope)
	{
		fScopeStack.add(scope);
		stackindex++;
	}
	
	public void popScope()
	{
		fScopeStack.remove(stackindex);
		stackindex--;
	}
}
