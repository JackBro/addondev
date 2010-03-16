package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.Stack;

public class ScopeStack {
	private ArrayList<Scope> fScopeStack;
	private Stack<Scope> fScapeStack;
	//private int stackindex;
	
	public ArrayList<Scope> getScopeList()
	{
		return fScopeStack;
	}
	
	public ScopeStack()
	{
		fScopeStack = new ArrayList<Scope>();
		fScapeStack = new Stack<Scope>();
		//stackindex = -1;
	}
	
	public Scope getScope(int index)
	{
		
		//return fScopeStack.get(index);
		return fScapeStack.get(index);
	}
	
	public Scope getCurrntScope()
	{
		//return fScopeStack.get(stackindex);
		return fScapeStack.peek();
	}
	
	public void pushScope(Scope scope)
	{
		fScopeStack.add(scope);
		//stackindex = fScopeStack.size()-1;
		//stackindex++;
		
		fScapeStack.push(scope);
	}
	
	public Scope popScope()
	{
		//Scope scope = getCurrntScope();
		//stackindex--;
		//return scope;
		
		return fScapeStack.pop();
	}
}
