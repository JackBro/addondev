package org.addondev.parser.javascript;

import java.util.List;

public interface IFunction {
	public Node Run(ScopeManager scopemanager, String name, List<Node> args);
}
