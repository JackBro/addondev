package org.addondev.parser.javascript;

import java.util.ArrayList;

public class GlobalObjectFrame {
	private static GlobalObjectFrame instance;
	private ArrayList<JsNode> nodeList;
	
	private GlobalObjectFrame(){
		nodeList = new ArrayList<JsNode>();
	}
	
	public static GlobalObjectFrame getInstance()
	{
		if(instance == null)
			instance = new GlobalObjectFrame();
		
		return instance;
	}
}
