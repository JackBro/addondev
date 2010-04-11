package org.addondev.parser.javascript;

import java.util.List;

public class InterfaceFunction implements IFunction {

	@Override
	public Node Run(ScopeManager scopemanager, String name, List<Node> args) /* throws IllegalArgumentException */ {
		// TODO Auto-generated method stub
		if(args.size() != 1) return null;
		
		String symbole = args.get(0).getSymbol();
		Node node = scopemanager.getGlobalNode(symbole); // global other
		return node;
	}

}
