package org.addondev.parser.javascript;

import java.util.HashMap;
import java.util.Map;

public class FunctionNode extends Node {
	
	private Map<String, Node> fSymbalTable; 
	
	public FunctionNode(Node parent, EnumNode nodetype, String symbol,
			int offset) {
		super(parent, nodetype, symbol, offset);
		// TODO Auto-generated constructor stub
		fSymbalTable = new HashMap<String, Node>();
	}
	
	public String getParamType(String paramname){
		if(fJsDocParser == null){
			fJsDocParser = new JsDocParser();
			fJsDocParser.parse(getJsDoc());
		}
		return fJsDocParser.getParamType(paramname);		
	}
	
	@Override
	public void addChildNode(Node node) {
		// TODO Auto-generated method stub
		if(!getSymTable().containsKey(node))
			getSymTable().put(node.getSymbol(), node);	
	}

	@Override
	public void clearSymTable() {
		// TODO Auto-generated method stub
		getSymTable().clear();
	}

	@Override
	public Node clone(Node parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturnType() {
		// TODO Auto-generated method stub
		return super.getReturnType();
	}

	@Override
	protected Map<String, Node> getSymTable() {
		// TODO Auto-generated method stub
		return fSymbalTable;
	}

	@Override
	protected void setSymTable(Map<String, Node> symtable) {
		// TODO Auto-generated method stub

	}

	@Override
	public Node getChild(String symbol) {
		// TODO Auto-generated method stub
		return getSymTable().get(symbol);
	}

}
