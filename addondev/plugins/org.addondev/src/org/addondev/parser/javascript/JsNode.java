package org.addondev.parser.javascript;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JsNode {
	//private static Pattern fJsDocTypePattern = Pattern.compile("@type\\s+(\\w+)");
	//private static Pattern fJsDocReturnPattern = Pattern.compile("@returns\\s+\\{\\s*(.*)\\s*\\}");
	
	private JsNode parent;
	private HashMap<String, JsNode> fSymbalTable; 

	private EnumNode fNodeType;
	
	private String fName;
	private int fOffset;
	private int fEndOffset;

	private JsDocParser fJsDocParser;
	private String fJsDoc;	
	private String fReturnType;	
	
	
	public int getEndoffset() {
		return fEndOffset;
	}

	public void setEndoffset(int endoffset) {
		this.fEndOffset = endoffset;
	}
	
	public String getfJsDoc() {
		return fJsDoc;
	}

	public void setfJsDoc(String fJsDoc) {
		this.fJsDoc = fJsDoc;
	}

	public JsNode getClone(JsNode p)
	{
		JsNode node = new JsNode(p, fNodeType, fName, fOffset);
		node.setfJsDoc(fJsDoc);
		return node;
	}

	public JsNode(JsNode parent, EnumNode nodetype, String name, int offset)
	{
		this.parent = parent;
		this.fName = name;
		this.fOffset = offset;	
		this.fNodeType = nodetype;
		this.fReturnType = null;
		
		fSymbalTable = new HashMap<String, JsNode>();
	}
	
	public JsNode(JsNode parent, EnumNode nodetype, String name, String returntype, int offset)
	{
		this.parent = parent;
		this.fName = name;
		this.fOffset = offset;
		this.fNodeType = nodetype;
		this.fReturnType = returntype;
		
		fSymbalTable = new HashMap<String, JsNode>();
	}
	
	public String getName()
	{
		return fName;
	}
	
	public EnumNode getNodeType() {
		return fNodeType;
	}

	public void setNodeType(EnumNode nodetype) {
		this.fNodeType = nodetype;
	}
	
	public JsNode getChild(String name) {
		return fSymbalTable.get(name);
	}
	
	public int getOffset()
	{
		return fOffset;
	}
	public void setOffset(int offset)
	{
		this.fOffset = offset;
	}
	
	public void addChildNode(JsNode node)
	{
		fSymbalTable.put(node.getName(), node);
	}
	
	public JsNode getParent()
	{
		return parent;
	}
	
	public String toString(String prefix) { return prefix + fNodeType; }
	
	public void dump(String prefix) {
		System.out.println(toString(prefix) + " : " + fName + " returntype=" + getReturnType());//" s:e= " + s + ":" + e);
		for (JsNode node : fSymbalTable.values()) {
			node.dump(prefix + " ");
		}
	}
	
	public boolean hasChildNode()
	{
		return fSymbalTable.size()>0?true:false;
	}
	
	public boolean hasChildNode(String name)
	{
		return fSymbalTable.containsKey(name);
	}
	
	public JsNode[] getChildNodes() {
		return fSymbalTable.values().toArray(new JsNode[fSymbalTable.size()]);
	}
	
	public HashMap<String, JsNode> getSymbalTable()
	{
		return fSymbalTable;
	}
	
	public void setSymbalTable(HashMap<String, JsNode> table)
	{
		fSymbalTable.clear();
		fSymbalTable = table;
	}
	
	public void setReturnType(String type)
	{
		fReturnType = type;
	}
	public String getReturnType()
	{
		//String key = null;
		if(fReturnType != null) return fReturnType;
		
		if(fJsDocParser == null)
		{
			fJsDocParser = new JsDocParser();
			fJsDocParser.parse(fJsDoc);
			fReturnType = fJsDocParser.getType();
		}
		
		return fReturnType;
//		if(fJsDoc != null && fReturnType == null)
//		{
//			JsDocParser p = new JsDocParser();
//			fReturnType = p.getType(fJsDoc);		
////			Matcher m = fJsDocTypePattern.matcher(fJsDoc);
////			 if (m.find()) {
////				 key = m.group(1);
////			 }
////			 else
////			 {
////				 m = fJsDocReturnPattern.matcher(fJsDoc);
////				 if (m.find()) {
////					 key = m.group(1);
////				 }
////			 }
////			 fReturnType = key;
//		}
//		
//		return fReturnType;
	}
	
	public String getParamType(String paramname)
	{
		if(fJsDocParser == null)
		{
			fJsDocParser = new JsDocParser();
			fJsDocParser.parse(fJsDoc);
		}
		
		return fJsDocParser.getParamType(paramname);		
	}
}
