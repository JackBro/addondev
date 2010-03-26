package org.addondev.parser.javascript;

import java.util.List;

public class InterfaceFunction implements IXPcomFunction {

	@Override
	public JsNode Run(List<JsNode> args) /* throws IllegalArgumentException */ {
		// TODO Auto-generated method stub
		if(args.size() != 1) return null;
		
		String name = args.get(0).getName();
		JsNode node = ScopeManager.instance().getGlobalNode(name); // global other
		return node;
	}

}
