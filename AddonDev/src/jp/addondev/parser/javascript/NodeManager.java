package jp.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NodeManager {
	private static NodeManager instance;
	private ArrayList<JsNode> nodeList;
	private HashMap<String, JsNode> nodeMap;
	
	private NodeManager(){
		nodeList = new ArrayList<JsNode>();
		nodeMap = new HashMap<String, JsNode>();
	}
	
	public static NodeManager getInstance()
	{
		if(instance == null)
			instance = new NodeManager();
		
		return instance;
	}
	
	public void SetNode(String key, JsNode node)
	{
		if(nodeMap.containsKey(key))
		{
			nodeMap.remove(key);
		}	
		nodeMap.put(key, node);
	}
	
	public JsNode getGlobalNode(String sym)
	{
		JsNode node = null;
		for(Map.Entry<String, JsNode> n : nodeMap.entrySet()) {
		    //System.out.println(e.getKey() + " : " + e.getValue());
			node = JsNodeHelper.findChildNode(n.getValue(), sym);
			if(node != null)
				break;
		}
		return node;
	}
}
