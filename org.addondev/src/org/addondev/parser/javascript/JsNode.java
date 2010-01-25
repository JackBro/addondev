package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JsNode {
	private static Pattern fJsDocTypePattern = Pattern.compile("@type\\s+(\\w+)");
	private static Pattern fJsDocReturnPattern = Pattern.compile("@returns\\s+\\{\\s*(.*)\\s*\\}");
	
	private JsNode parent;
	private ArrayList<JsNode> children;
	//private JsNode fBindNode = null;
	
	private static  ArrayList<JsNode> non = new ArrayList<JsNode>();
	
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
		offset = -1;
	}

	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getImage()
	{
		return image;
	}
	
//	private ArrayList<JsNode> getChildNodes()
//	{
//		if(children != null)
//		{
//			return children;
//		}
//		else if(fBindNode != null)
//		{
//			return fBindNode.getChildNodes();
//		}
//		else
//			return non;
//	}
	
//	public ArrayList<JsNode> getChildren()
//	{
//		//return children == null?null:children.toArray(new JsNode[children.size()]);
//		return children;
//	}
	
	public int getChildrenNum() {
		return children == null?0:children.size();
	}
	
	public JsNode getChild(int i) {
		return children.get(i);
	}
	
	public JsNode getChild(String sym) {
		if(children == null) return null;
		
		for (int i = 0; i < children.size(); i++) {
			if(children.get(i).image.equals(sym))
			{
				return children.get(i);
			}
		}
		return null;
	}
	
//	public void setBindNode(JsNode node)
//	{
//		fBindNode = node;
//		children = null;
//	}
//	
//	public JsNode getBindNode()
//	{
//		return fBindNode;
//	}
	
//	public JsNode getChild(Frame localframe, String sym) {
//
//		for (int i = 0; i < children.size(); i++) {
//			if(children.get(i).image.equals(sym))
//			{
//				return children.get(i);
//			}
//		}
//		
//		String type = getType();
//		
//		return null;
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
//		if(fBindNode != null)
//			fBindNode = null;
		
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
		System.out.println(toString(prefix) + " : " + image + " = " + val + " s:e= " + s + ":" + e);
		if (children != null) {
			for (int i = 0; i < children.size(); ++i) {
				JsNode n = children.get(i);
				if (n != null) {
					n.dump(prefix + " ");
				}
			}
		}
//		else if(fBindNode != null)
//		{
//			JsNode[] bnodes = fBindNode.getChildren();
//			for (int i = 0; i < bnodes.length; ++i) {
//				JsNode n = bnodes[i];
//				if (n != null) {
//					n.dump(prefix + " ");
//				}
//			}			
//		}
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

	public void setChildNode(ArrayList<JsNode> children) {
		this.children = children;
	}
	public ArrayList<JsNode> getChildNode() {
		if(this.children == null)
		{
			this.children = new ArrayList<JsNode>();

		}
		return this.children;
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
		return key;
	}
}
