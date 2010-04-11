package org.addondev.parser.javascript.serialize;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.addondev.parser.javascript.EnumNode;
//import org.addondev.parser.javascript.JsNode;
import org.addondev.parser.javascript.FunctionNode;
import org.addondev.parser.javascript.Node;
import org.addondev.parser.javascript.Scope;
import org.addondev.parser.javascript.ScopeManager;
import org.addondev.parser.javascript.ScopeStack;
import org.addondev.parser.javascript.ValNode;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


public class NodeSerializer {

	
	public static Node read(String filepath)
	{
		Node root = new ValNode(null, EnumNode.ROOT, "root", 0);
		
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
			Node classNode = new ValNode(root, EnumNode.OBJECT, classname, 0);
			root.addChildNode(classNode);
			
			ArrayList<JsElement> elements = jsClass.getElements();
			for (JsElement element : elements) {
				EnumNode nodetype = getNodeType(element);
				Node chNode=null;
				if(nodetype == EnumNode.VALUE_PROP || nodetype == EnumNode.VALUE
						|| nodetype == EnumNode.OBJECT )
				{
					chNode = new ValNode(classNode, nodetype, element.getName(), element.getReturntype(), 0);
				}else if(nodetype == EnumNode.FUNCTION_PROP || nodetype == EnumNode.FUNCTION){
					chNode = new FunctionNode(classNode, nodetype, element.getName(), 0);
					chNode.setReturnType(element.getReturntype());
				}
				
				classNode.addChildNode(chNode);
				if(element.getParams() != null)
				{
					ArrayList<JsElement> params = element.getParams();
					for (JsElement param : params) {
						//JsNode paramNode = new JsNode(chNode, EnumNode.VALUE, param.getName(), param.getReturntype(), 0);
						EnumNode paramtype = getNodeType(param);
						Node paramNode = null;
						if(paramtype == EnumNode.VALUE_PROP || paramtype == EnumNode.VALUE
								|| paramtype == EnumNode.OBJECT )
						{
							paramNode = new ValNode(chNode, paramtype, param.getName(), param.getReturntype(), 0);
						}else if(paramtype == EnumNode.FUNCTION_PROP || paramtype == EnumNode.FUNCTION){
							paramNode = new FunctionNode(chNode, paramtype, param.getName(), 0);
							paramNode.setReturnType(param.getReturntype());
						}
						
						paramNode.setParam(true);
						chNode.addChildNode(paramNode);
					}
				}
			}
		}
		
//		String name = "xpcom";
//		ScopeStack fScopeStack = new ScopeStack();
//		fScopeStack.pushScope(new Scope(0, 1, root));
//		ScopeManager.instance().setScopeStack(name, fScopeStack);
		
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
//		else if(element.getNodeType().equals(EnumNode.PARAM.name()))
//		{
//			nodetype = EnumNode.PARAM;
//		}
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
