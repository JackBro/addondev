package org.addondev.parser.javascript;

import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.BoldAction;

public class JsNodeHelper {
	public static JsNode findChildNode(JsNode node, String image)
	{
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
	
	public static void assignNode(ArrayList<JsNode> node1, ArrayList<JsNode> node2)
	{
		for (JsNode jsNode : node2) {
			if(!hasNode(node1, jsNode))
				node1.add(jsNode);
		}
	}
	
	public static void assignCloneNode(ArrayList<JsNode> node1, ArrayList<JsNode> node2)
	{
		//if(node1 == null) node1 = new ArrayList<JsNode>();
		//node1.clear();
		
		for (JsNode jsNode : node2) {
			node1.add(jsNode.getClone(null));
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
