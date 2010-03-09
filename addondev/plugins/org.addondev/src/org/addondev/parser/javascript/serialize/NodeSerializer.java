package org.addondev.parser.javascript.serialize;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.addondev.parser.javascript.EnumNode;
import org.addondev.parser.javascript.JsNode;
import org.addondev.parser.javascript.Scope;
import org.addondev.parser.javascript.ScopeManager;
import org.addondev.parser.javascript.ScopeStack;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


public class NodeSerializer {

	
	public static JsNode read(String filepath)
	{
		JsNode root = new JsNode(null, EnumNode.ROOT, "root", 0);
		
		Serializer serializer = new Persister();
		JsData data = null;
		try {
			data = serializer.read(JsData.class, new File(filepath));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<JsClass> classes = data.getClasses();
		for (JsClass jsClass : classes) {
			String classname = jsClass.getName();
			JsNode classNode = new JsNode(root, EnumNode.OBJECT, classname, 0);
			root.addChildNode(classNode);
			
			ArrayList<JsElement> elements = jsClass.getElements();
			for (JsElement element : elements) {
				EnumNode nodetype = getNodeType(element);
				JsNode chNode = new JsNode(classNode, nodetype, element.getName(), 0);
				classNode.addChildNode(chNode);
				if(element.getParams().size() > 0)
				{
					ArrayList<String> params = element.getParams();
					for (String param : params) {
						JsNode paramNode = new JsNode(chNode, EnumNode.PARAM, param, 0);
						chNode.addChildNode(paramNode);
					}
				}
			}
		}
		
		String name = "xpcom";
		ScopeStack fScopeStack = new ScopeStack();
		fScopeStack.pushScope(new Scope(0, 1, root));
		ScopeManager.instance().setScopeStack(name, fScopeStack);
		
		return root;
	}
	
	private static EnumNode getNodeType(JsElement element)
	{
		EnumNode nodetype = EnumNode.VALUE;
		if(element.getNodeType().equals(EnumNode.FUNCTION.name()))
		{
			nodetype = EnumNode.FUNCTION;
		}
		else if(element.getNodeType().equals(EnumNode.FUNCTION_PROP.name()))
		{
			nodetype = EnumNode.FUNCTION_PROP;
		}
		else if(element.getNodeType().equals(EnumNode.OBJECT.name()))
		{
			nodetype = EnumNode.OBJECT;
		}
		else if(element.getNodeType().equals(EnumNode.PARAM.name()))
		{
			nodetype = EnumNode.PARAM;
		}
		else if(element.getNodeType().equals(EnumNode.VALUE.name()))
		{
			nodetype = EnumNode.VALUE;
		}
		else if(element.getNodeType().equals(EnumNode.VALUE_PROP.name()))
		{
			nodetype = EnumNode.VALUE_PROP;
		}
		
		return nodetype;
	}
}
