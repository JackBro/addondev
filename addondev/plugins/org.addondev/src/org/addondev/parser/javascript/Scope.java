package org.addondev.parser.javascript;

public class Scope {
	private int startOffset;
	private int endOffset;
	private String fType;
	private JsNode node;
	//private HashMap<String, JsNode> map;
	
	public String getType() {
		return fType;
	}
	public void setType(String Type) {
		this.fType = Type;
	}
	public int getStart() {
		return startOffset;
	}
	public void setStart(int start) {
		this.startOffset = start;
	}
	public int getEnd() {
		return endOffset;
	}
	public void setEnd(int end) {
		this.endOffset = end;
	}
	public JsNode getNode() {
		return node;
	}
	public JsNode getNode(String name) {
		//return map.get(image);
//		ArrayList<JsNode> nodes = node.getChildNode();
//		if(nodes == null) return null;
//		for (JsNode jsnode : nodes) {
//			String key = jsnode.getName();
//			if(key.equals(image))
//				return jsnode;
//		}
//		return null;
		
		return node.getChild(name);
	}
//	public void setNode(JsNode node) {
//		this.node = node;
//		JsNode[] nodes = node.getChildren();
//		for (JsNode jsnode : nodes) {
//			String key = jsnode.getImage();
//			map.put(key, jsnode);
//		}
//	}
	public Scope(int startOffset, int endOffset, JsNode node) {
		super();
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.node = node;
		
		//map = new HashMap<String, JsNode>();
	}
	public Scope(int startOffset, JsNode node) {
		super();
		this.startOffset = startOffset;
		this.node = node;
		
		//map = new HashMap<String, JsNode>();
	}
	
	public Scope(int startOffset, int endOffset, JsNode node, String type) {
		super();
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.node = node;
		this.fType = type;
		
		//map = new HashMap<String, JsNode>();
	}
	public Scope(int startOffset, JsNode node, String type) {
		super();
		this.startOffset = startOffset;
		this.node = node;
		this.fType = type;
		
		//map = new HashMap<String, JsNode>();
	}
}
