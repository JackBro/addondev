package org.addondev.parser.javascript;

import java.util.List;

public class InterfaceFunction implements IFunction {

	@Override
	public JsNode Run(ScopeManager scopemanager, String name, List<JsNode> args) /* throws IllegalArgumentException */ {
		// TODO Auto-generated method stub
		if(args.size() != 1) return null;
		
		String symbole = args.get(0).getName();
		JsNode node = scopemanager.getGlobalNode(symbole); // global other
		return node;
	}

}
