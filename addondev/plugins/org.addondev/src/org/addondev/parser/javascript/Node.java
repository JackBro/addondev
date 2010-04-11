package org.addondev.parser.javascript;

import java.util.HashMap;
import java.util.Map;

public abstract class Node {
	private Node parent;
	private EnumNode fNodeType;
	private boolean isParam;
	private String fSymbol;
	private int fStartOffset;
	private int fEndOffset;	
	
	protected JsDocParser fJsDocParser;
	private String fJsDoc;	
	private String fReturnType;
	
	protected abstract Map<String, Node> getSymTable();
	protected abstract void setSymTable(Map<String, Node> symtable);
	
	public abstract Node clone(Node parent);
	public abstract void clearSymTable();
	public abstract void addChildNode(Node node);
	public abstract Node getChild(String symbol);

	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public EnumNode getNodeType() {
		return fNodeType;
	}
	public void setNodeType(EnumNode NodeType) {
		this.fNodeType = NodeType;
	}
	public boolean isParam() {
		return isParam;
	}
	public void setParam(boolean isParam) {
		this.isParam = isParam;
	}
	public String getSymbol() {
		return fSymbol;
	}
	public void setSymbol(String Symbol) {
		this.fSymbol = Symbol;
	}
	public int getStartOffset() {
		return fStartOffset;
	}
	public void setStartOffset(int StartOffset) {
		this.fStartOffset = StartOffset;
	}
	public int getEndOffset() {
		return fEndOffset;
	}
	public void setEndOffset(int EndOffset) {
		this.fEndOffset = EndOffset;
	}
	public String getJsDoc() {
		return fJsDoc;
	}
	public void setJsDoc(String JsDoc) {
		this.fJsDoc = JsDoc;
		fJsDocParser = null;
		fReturnType = null;
	}
	
	public Node(Node parent, EnumNode nodetype, String symbol, int offset)
	{
		this.parent = parent;
		this.fSymbol = symbol;
		this.fStartOffset = offset;	
		this.fNodeType = nodetype;
		this.fReturnType = null;
		this.isParam = false;
	}
	
	public Node(Node parent, EnumNode nodetype, String symbol, String returntype, int offset)
	{
		this.parent = parent;
		this.fSymbol = symbol;
		this.fStartOffset = offset;
		this.fNodeType = nodetype;
		this.fReturnType = returntype;
		this.isParam = false;
	}	
	
	public boolean hasChildNode(){
		return getSymTable().size()>0?true:false;
	}
	
	public boolean hasChildNode(String name)
	{
		return getSymTable().containsKey(name);
	}
	public void assignChildNode(Node distNode) {
		//distNode.setSymbalTable(srcNode.getSymbalTable());
		distNode.setSymTable(getSymTable());
	}

	public void cloneChildNode(Node distNode) {
//		JsNode[] srcChildNodes = srcNode.getChildNodes();
//		distNode.getSymbalTable().clear();
//		for (JsNode node : srcChildNodes) {
//			distNode.addChildNode(node.getClone(distNode));
//		}
		Node[] srcChildNodes = getChildNodes();
		distNode.clearSymTable();
		for (Node node : srcChildNodes) {
			distNode.addChildNode(node.clone(distNode));
		}
	}
	
	public Node[] getChildNodes() {
		return getSymTable().values().toArray(new Node[getSymTable().size()]);
	}	
	
	public String getReturnType()
	{
		if(fReturnType != null) {
			if(fJsDocParser == null)
			{
				fJsDocParser = new JsDocParser();
				fJsDocParser.parse(fJsDoc);
				//fReturnType = fJsDocParser.getType();
			}
			return fReturnType;
		}
		
		if(fJsDocParser == null)
		{
			fJsDocParser = new JsDocParser();
			fJsDocParser.parse(fJsDoc);
			fReturnType = fJsDocParser.getType();
		}
		
		return fReturnType;		
	}
	
	public void setReturnType(String ReturnType) {
		this.fReturnType = ReturnType;
	}
	public String toString(String prefix) { return prefix + fNodeType; }
	
	public void dump(String prefix) {
		System.out.println(toString(prefix) + " : " + fSymbol + " returntype=" + getReturnType());//" s:e= " + s + ":" + e);
		for (Node node : getSymTable().values()) {
			node.dump(prefix + " ");
		}
	}
	
}
