package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JsNode {
	private static Pattern fJsDocTypePattern = Pattern.compile("@type\\s+(\\w+)");
	private static Pattern fJsDocReturnPattern = Pattern.compile("@returns\\s+\\{\\s*(.*)\\s*\\}");
	
	private JsNode parent;
	//private ArrayList<JsNode> children;
	private HashMap<String, JsNode> fSymbalTable; 

	//private String id;
	private EnumNode fNodeType;
	
	private String fName;
	private int fOffset;
	private int fEndOffset;

	private String fJsDoc;	
	private String fType;	
	
	
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
	
	/**
	 * 
	 * @param parent
	 * @param id
	 * @param image
	 * @param offset
	 */
	public JsNode(JsNode parent, EnumNode nodetype, String name, int offset)
	{
		this.parent = parent;
		//this.id = id;
		this.fName = name;
		this.fOffset = offset;
		offset = -1;
		fSymbalTable = new HashMap<String, JsNode>();
		this.fNodeType = nodetype;
	}

//	public String getId()
//	{
//		return id;
//	}
//	
//	public void setId(String id)
//	{
//		this.id = id;
//	}
	
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

//	public int getChildrenNum() {
//		return children == null?0:children.size();
//	}
	
//	public JsNode getChild(int i) {
//		return children.get(i);
//	}
	
	public JsNode getChild(String name) {
//		if(children == null) return null;
//		
//		for (int i = 0; i < children.size(); i++) {
//			if(children.get(i).fName.equals(sym))
//			{
//				return children.get(i);
//			}
//		}
//		return null;
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
	
//	public void addChild(JsNode child)
//	{
//		if(children == null)
//			children = new ArrayList<JsNode>();
//		
//		children.add(child);
//	}
	
	public void addChildNode(JsNode node)
	{
		fSymbalTable.put(node.getName(), node);
	}
	
	public JsNode getParent()
	{
		return parent;
	}
	
//	public void removeAllChild()
//	{
//		children.clear();
//	}
	
//	public void removeChild(JsNode node)
//	{
//		children.remove(node);
//	}
	
	public String toString(String prefix) { return prefix + fNodeType; }
	
	public void dump(String prefix) {
		int s = fOffset;
		int e = fEndOffset;
		String val = "";
		System.out.println(toString(prefix) + " : " + fName + " = " + val + " type=" + getType());//" s:e= " + s + ":" + e);
//		if (children != null) {
//			for (int i = 0; i < children.size(); ++i) {
//				JsNode n = children.get(i);
//				if (n != null) {
//					n.dump(prefix + " ");
//				}
//			}
//		}
		for (JsNode node : fSymbalTable.values()) {
			node.dump(prefix + " ");
		}
	}
	
//	public List<JsNode> getChildNode() {
//		if(this.children == null)
//		{
//			this.children = new ArrayList<JsNode>();
//
//		}
//		return this.children;
//	}
	
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
	
	public void setType(String type)
	{
		fType = type;
	}
	public String getType()
	{
		String key = null;
		if(fJsDoc != null && fType == null)
		{
			
			Matcher m = fJsDocTypePattern.matcher(fJsDoc);
			 if (m.find()) {
				 key = m.group(1);
			 }
			 else
			 {
				 m = fJsDocReturnPattern.matcher(fJsDoc);
				 if (m.find()) {
					 key = m.group(1);
				 }
			 }
			 fType = key;
		}
		return fType;
	}
}
