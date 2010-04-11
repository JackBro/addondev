package org.addondev.parser.javascript;

import java.util.HashMap;
import java.util.Map;

public class ValNode extends Node {
	
	private Map<String, Node> fSymbalTable; 
	
	public ValNode(Node parent, EnumNode nodetype, String symbol, int offset) {
		super(parent, nodetype, symbol, offset);
		fSymbalTable = new HashMap<String, Node>();
	}

	public ValNode(Node parent, EnumNode nodetype, String symbol,
			String returntype, int offset) {
		super(parent, nodetype, symbol, returntype, offset);
		fSymbalTable = new HashMap<String, Node>();
	}

	@Override
	public void addChildNode(Node node) {
		// TODO Auto-generated method stub
		if(!fSymbalTable.containsKey(node))
			fSymbalTable.put(node.getSymbol(), node);		
	}

	@Override
	public void clearSymTable() {
		// TODO Auto-generated method stub
		fSymbalTable.clear();
	}

	@Override
	public Node clone(Node parent) {
		Node node = new ValNode(parent, getNodeType(), getSymbol(), getStartOffset());
		node.setJsDoc(getJsDoc());
		return node;
	}

//	@Override
//	public String getReturnType() {
//		// TODO Auto-generated method stub
//		return getJsDoc();
//	}

	@Override
	protected Map<String, Node> getSymTable() {
		// TODO Auto-generated method stub
		return fSymbalTable;
	}

	@Override
	protected void setSymTable(Map<String, Node> symtable) {
		// TODO Auto-generated method stub
		fSymbalTable = symtable;
	}

	@Override
	public Node getChild(String symbol) {
		// TODO Auto-generated method stub
		return getSymTable().get(symbol);
	}

}
