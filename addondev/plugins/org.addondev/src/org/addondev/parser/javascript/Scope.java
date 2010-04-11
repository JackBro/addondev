package org.addondev.parser.javascript;

public class Scope {
	private int startOffset;
	private int endOffset;
	private String fType;
	//private JsNode node;
	private Node node;
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
//	public JsNode getNode() {
//		return node;
//	}
	public Node getNode() {
		return node;
	}
	public Node getNode(String symbol) {
		return node.getChild(symbol);
	}
	public Scope(int startOffset, int endOffset, Node node) {
		super();
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.node = node;
	}
	public Scope(int startOffset, Node node) {
		super();
		this.startOffset = startOffset;
		this.node = node;
		
		this.endOffset = Integer.MAX_VALUE;
	}
	
	public Scope(int startOffset, int endOffset, Node node, String type) {
		super();
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.node = node;
		this.fType = type;
	}
	public Scope(int startOffset, Node node, String type) {
		super();
		this.startOffset = startOffset;
		this.node = node;
		this.fType = type;
	}
}
