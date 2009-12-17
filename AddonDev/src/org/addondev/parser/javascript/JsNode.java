package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import jp.addondev.parser.javascript.SimpleNode;

public class JsNode {
	private static Pattern fJsDocTypePattern = Pattern.compile("@type\\s+(\\w+)");
	private static Pattern fJsDocReturnPattern = Pattern.compile("@returns\\s+\\{\\s*(.*)\\s*\\}");
	
	private JsNode parent;
	private ArrayList<JsNode> children;
	private String id;
	private String image;
	private int fOffset;
	private int endoffset;
	
	private String fJsDoc;	
	private String fType;	
	
	
	public int getEndoffset() {
		return endoffset;
	}

	public void setEndoffset(int endoffset) {
		this.endoffset = endoffset;
	}
	
	public String getfJsDoc() {
		return fJsDoc;
	}

	public void setfJsDoc(String fJsDoc) {
		this.fJsDoc = fJsDoc;
	}

//	private boolean isvisible;
//	private ValueNode valueNode;
//	
//	public ValueNode getValueNode() {
//		return valueNode;
//	}
//
//	public void setValueNode(ValueNode valueNode) {
//		this.valueNode = valueNode;
//	}

	
	public JsNode getClone(JsNode p)
	{
		JsNode node = new JsNode(p, id, image, fOffset);
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
	public JsNode(JsNode parent, String id, String image, int offset)
	{
		this.parent = parent;
		this.id = id;
		this.image = image;
		this.fOffset = offset;
		//this.isvisible = true;
		offset = -1;
	}
	
	/**
	 * 
	 * @param parent
	 * @param id
	 * @param image
	 * @param offset
	 * @param isvisible
	 */
	public JsNode(JsNode parent, String id, String image, int offset, boolean isvisible)
	{
		this.parent = parent;
		this.id = id;
		this.image = image;
		this.fOffset = offset;
		//this.isvisible = isvisible;
		offset = -1;
	}

	public String getId()
	{
		return id;
	}
	
	public String getImage()
	{
		return image;
	}
	
	public JsNode[] getChildren()
	{
		return children == null?null:children.toArray(new JsNode[children.size()]);
	}
	
	public int getChildrenNum() {
		// TODO Auto-generated method stub
		return children == null?0:children.size();
	}
	
	public JsNode getChild(int i) {
		// TODO Auto-generated method stub
		return children.get(i);
	}
	
	public JsNode getChild(String sym) {
		// TODO Auto-generated method stub
//		if(children == null || (children != null && children.size() == 0))
//		{
//			 Matcher m = pattern.matcher(fJsDoc);
//			 if (m.find()) {
//			     String key = m.group(1); // key = "Name";
//			     //String value = m.group(2); // value = "Regular Expressions";
//			     //System.out.println("key   = " + key);
//			     //System.out.println("value = ");
//				 
//			 }			
//		}

		for (int i = 0; i < children.size(); i++) {
			if(children.get(i).image.equals(sym))
			{
				return children.get(i);
			}
		}
		return null;
	}
	
	public JsNode getChild(Frame localframe, String sym) {
		// TODO Auto-generated method stub
//		if(children == null || (children != null && children.size() == 0))
//		{
//			 Matcher m = pattern.matcher(fJsDoc);
//			 if (m.find()) {
//			     String key = m.group(1); // key = "Name";
//			     //String value = m.group(2); // value = "Regular Expressions";
//			     //System.out.println("key   = " + key);
//			     //System.out.println("value = ");
//				 
//			 }			
//		}

		for (int i = 0; i < children.size(); i++) {
			if(children.get(i).image.equals(sym))
			{
				return children.get(i);
			}
		}
		
		String type = getType();
		
		return null;
	}
	
//	public int getLine()
//	{
//		return line;
//	}
	
	public int getOffset()
	{
		return fOffset;
	}
	public void setOffset(int offset)
	{
		this.fOffset = offset;
	}
	
	public void addChild(JsNode child)
	{
		if(children == null)
			children = new ArrayList<JsNode>();
		
		children.add(child);
	}
	
	public JsNode getParent()
	{
		return parent;
	}
	
	public void removeAllChild()
	{
		children.clear();
	}
	
	public void removeChild(JsNode node)
	{
		children.remove(node);
	}
	
	public String toString(String prefix) { return prefix + id; }
	public void dump(String prefix) {
		int s = fOffset;
		int e = endoffset;
		String val = "";
//		if(getChildrenNum() >0)
//		{
//			val = getChild(0).getId();
//		}
		//if(valueNode != null && valueNode.getNode() != null)
		//	val = valueNode.getNode().id;
		//String val = valueNode == null?"":ValueNode.id;
		System.out.println(toString(prefix) + " : " + image + " = " + val + " s:e= " + s + ":" + e);
		if (children != null) {
			for (int i = 0; i < children.size(); ++i) {
				JsNode n = children.get(i);
				if (n != null) {
					n.dump(prefix + " ");
				}
			}
		}
	}
	
	private static class ttmp
	{
		public static JsNode node;
	}
	
	private  JsNode fOffsetNode;
	private boolean fff;
	public JsNode getNodeFromOffset(int offset)
	{
		fOffsetNode = null;
		fff = false;
		_getNodeFromOffset(offset);
		return ttmp.node;
	}
	public void _getNodeFromOffset(int offset)
	{
		//if(fff) return fOffsetNode;
		if (children != null) {
			for (int i = 0; i < children.size(); ++i) {
				JsNode n = children.get(i);
				if (n != null) {
					if((offset > n.getOffset() && offset < n.getEndoffset()) && fOffsetNode == null)
					{
						fOffsetNode = n;
						ttmp.node = n;
						System.out.println("fOffsetNode = " + fOffsetNode.getId() + " : " + fOffsetNode.getImage());
					}
					else if(fOffsetNode != null)
					{
						fff = true;
						System.out.println("!fOffsetNode = " + fOffsetNode.getId() + " : " + fOffsetNode.getImage());
						//return fOffsetNode;
						break;
					}
					n._getNodeFromOffset(offset);
					//if(fff) break;
				}
			}
		}
		//return fOffsetNode;
	}

	public void setChildren(ArrayList<JsNode> children) {
		this.children = children;
	}
	public ArrayList<JsNode> getChildrenList() {
		if(this.children == null)
		{
			this.children = new ArrayList<JsNode>();

		}
		return this.children;
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
		return key;
	}
}
