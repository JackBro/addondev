package org.addondev.parser.javascript;

import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.BoldAction;

public class JsNodeHelper {
	public static JsNode findChildNode(JsNode node, String image)
	{
		if(node == null) return null;
		if(node.getChildrenNum() == 0) return null;
		for (int i = 0; i < node.getChildrenNum(); i++) {
			if(node.getChild(i).getImage().equals(image))
			{
				return node.getChild(i);
			}
		}		
		return null;
	}
	
	public static Boolean hasChildNode(JsNode node, String image)
	{
		for (int i = 0; i < node.getChildrenNum(); i++) {
			if(node.getChild(i).getImage().equals(image))
			{
				return true;
			}
		}		
		return false;
	}
	
	public static void assignNode(ArrayList<JsNode> toNode, ArrayList<JsNode> fromNode)
	{
		for (JsNode jsNode : fromNode) {
			if(!hasNode(toNode, jsNode))
				toNode.add(jsNode);
		}
	}
	
	public static void assignCloneNode(ArrayList<JsNode> toNode, ArrayList<JsNode> fromNode)
	{
		//if(node1 == null) node1 = new ArrayList<JsNode>();
		//node1.clear();
		
		for (JsNode jsNode : fromNode) {
			toNode.add(jsNode.getClone(null));
		}
	}
	
	private static boolean hasNode(ArrayList<JsNode> nodelist, JsNode node)
	{
		for (JsNode jsNode : nodelist) {
			if(jsNode.getImage().equals(node.getImage()))
				return true;
		}	
		
		return false;
	}
}
