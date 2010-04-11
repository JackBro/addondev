package org.addondev.parser.javascript;

import java.util.HashMap;
import java.util.Map;

public class ArrayNode extends Node {

	private Map<String, Node> fArraySymbalTable; 
	private Map<String, Node> fArryValSymbalTable; 
	
	private boolean isArray = true;
	
	public boolean isArray() {
		return isArray;
	}

	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

	public ArrayNode(Node parent, EnumNode nodetype, String symbol, int offset) {
		super(parent, nodetype, symbol, offset);
		fArraySymbalTable = new HashMap<String, Node>();
		fArryValSymbalTable = new HashMap<String, Node>();
	}

	public ArrayNode(Node parent, EnumNode nodetype, String symbol,
			String returntype, int offset) {
		super(parent, nodetype, symbol, returntype, offset);
		fArraySymbalTable = new HashMap<String, Node>();
		fArryValSymbalTable = new HashMap<String, Node>();
	}

	@Override
	public void addChildNode(Node node) {
		if(!getSymTable().containsKey(node))
			getSymTable().put(node.getSymbol(), node);
	}

	@Override
	public void clearSymTable() {
		getSymTable().clear();
	}

	@Override
	public Node clone(Node parent) {
		Node node = new ValNode(parent, getNodeType(), getSymbol(), getStartOffset());
		node.setJsDoc(getJsDoc());
		return node;
	}

	@Override
	public String getReturnType() {
		// TODO Auto-generated method stub
		String ret = super.getReturnType();
		if(ret!= null && ret.length()>0)
		{
			int index = ret.indexOf("[");
			if(index > 0)
			{
				ret = ret.substring(0,ret.indexOf("["));
			}
			else{
				ret = null;
			}
		}
		else
		{
			ret = null;
		}
		return isArray()?"Array":ret;
	}

	@Override
	protected Map<String, Node> getSymTable() {
		return isArray()?fArraySymbalTable:fArryValSymbalTable;
	}

	@Override
	protected void setSymTable(Map<String, Node> symtable) {
		//Map<String, Node> map = getSymTable();
		//map = symtable;
		if(isArray()){
			fArraySymbalTable = symtable;
		}else{
			fArryValSymbalTable = symtable;
		}
	}

	@Override
	public Node getChild(String symbol) {
		// TODO Auto-generated method stub
		return getSymTable().get(symbol);
	}

}
