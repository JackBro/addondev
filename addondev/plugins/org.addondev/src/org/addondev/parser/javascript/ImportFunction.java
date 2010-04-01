package org.addondev.parser.javascript;

import java.util.List;

public class ImportFunction implements IFunction {
	
/**
 * @param args[0] module path
 * @param args[1] node
 * 
 */
	@Override
	public JsNode Run(ScopeManager scopemanager, String name, List<JsNode> args) {
		// TODO Auto-generated method stub
		if(args.size() == 1)
		{
			Scope currnetscope = scopemanager.getCurrentScope(name);
			//JsNode currntnode = args.get(0);
			String resourcefile = args.get(0).getName();
			
		}
		else if(args.size() == 2)
		{
			String resourcefile = args.get(0).getName();	
			JsNode node = args.get(1);
		}
		else
		{
			return null;
		}
		
		
		return null;
	}

}
