package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.Stack;

public class ScopeStack {
	private ArrayList<Scope> fScopeList;
	private Stack<Scope> fScopeStack;
	private static Scope fEmptyScope = new Scope(-1, new JsNode(null, EnumNode.VALUE, "", -1));
	
	public ArrayList<Scope> getScopeList()
	{
		return fScopeList;
	}
	
	public ScopeStack()
	{
		fScopeList = new ArrayList<Scope>();
		fScopeStack = new Stack<Scope>();
	}
	
	public Scope getScope(int index)
	{
		return fScopeStack.get(index);
	}
	
	public Scope getCurrntScope()
	{
		return fScopeStack.peek();
	}
	
	public void pushScope(Scope scope)
	{
		fScopeList.add(scope);	
		fScopeStack.push(scope);
	}
	
	public Scope popScope()
	{	
		return fScopeStack.pop();
	}
	
//	public Scope getScope(String name)
//	{
//		for (Scope scope : fScopeList) {
//			if(scope.getNode(name) != null)
//			{
//				return scope;
//			}
//		}
//		
//		return fScopeList.get(0);
//	}
	
	public Scope getUpScope(Scope currentscope, String name)
	{
		for (Scope scope : fScopeList) {
			if((scope.getStart() < currentscope.getStart()) 
					&& scope.getEnd() >= currentscope.getEnd())
			{
				if(scope.getNode(name) != null)
				{
					return scope;
				}
			}
		}
	
		//return fScopeList.get(0);
		return fEmptyScope;
	}
	
}
