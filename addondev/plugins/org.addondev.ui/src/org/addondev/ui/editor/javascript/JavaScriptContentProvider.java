package org.addondev.ui.editor.javascript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import org.addondev.parser.javascript.Node;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class JavaScriptContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {
		// TODO Auto-generated method stub
		//return null;
		//File [] children = ((File)parentElement).listFiles( );
		//return children == null ? new Object[0] : children;
		
		//JavaScriptNode n = (JavaScriptNode)parentElement;
		//return n.getChildren();
		
		Node n = (Node)parentElement;
		//return n.getChildNode();
		//return n.getChildNode().toArray(new JsNode[n.getChildNode().size()]);
		return n.getChildNodes();
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		//return null;
		//return ((File)element).getParentFile();
		return ((Node)element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		//return false;
		//return n.jjtGetNumChildren() == 0 ? false : true; 
		//JavaScriptNode n = (JavaScriptNode)element;
		//return n.getChildrenNum() == 0 ? false:true;
		
		Node n = (Node)element;
		//return n.getChildrenNum() == 0 ? false:true;
		return n.hasChildNode();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		//return null;
		
		//JavaScriptNode node = (JavaScriptNode)inputElement;
		//return node.getChildren();
		
		Node node = (Node)inputElement;
		//return node.getChildren();
		//return node.getChildNode().toArray(new JsNode[node.getChildNode().size()]);
		return node.getChildNodes();
		
//		ArrayList<String> checklist = new  ArrayList<String>();
//		ArrayList<Object> nodelist = new ArrayList<Object>();
//		SimpleNode n = (SimpleNode)inputElement;
//		int cnt = n.jjtGetNumChildren();
//		for (int i = 0; i < cnt; i++) {
//			if(n.getNodeName().equals("FunctionDeclaration"))
//			{
//				nodelist.add(n.jjtGetChild(i));		
//			}
//			else if(n.getNodeName().equals("VariableStatement"))
//			{
//				if(!checklist.contains(n.getImage()))
//				{
//					checklist.add(n.getImage());
//					nodelist.add(n.jjtGetChild(i));			
//				}
//			}
//			else if(n.getNodeName().equals("ExpressionStatement"))
//			{
//				if(SimpleNodeHelper.hasObject(n))
//				{
//					nodelist.add(n.jjtGetChild(i));
//				}
//			}
//		}
//		return nodelist.toArray();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

}
