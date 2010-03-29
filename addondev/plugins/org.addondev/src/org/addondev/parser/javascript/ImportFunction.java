package org.addondev.parser.javascript;

import java.util.List;

public class ImportFunction implements IFunction {
	
/**
 * @param args[0] module path
 * @param args[1] node
 * 
 */
	@Override
	public JsNode Run(ScopeManager scopemanager, List<JsNode> args) {
		// TODO Auto-generated method stub
		if(args.size() == 2)
		{
			JsNode currntnode = args.get(0);
			String path = args.get(1).getName();
		}
		else if(args.size() == 3)
		{
			JsNode currntnode = args.get(0);
			String path = args.get(1).getName();
			JsNode node = args.get(2);
		}
		
		
		return null;
	}

}
