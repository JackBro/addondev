package org.addondev.parser.javascript;

import java.util.List;

public class InterfaceFunction implements IFunction {

	@Override
	public JsNode Run(ScopeManager scopemanager, List<JsNode> args) /* throws IllegalArgumentException */ {
		// TODO Auto-generated method stub
		if(args.size() != 1) return null;
		
		String name = args.get(0).getName();
		JsNode node = scopemanager.getGlobalNode(name); // global other
		return node;
	}

}
