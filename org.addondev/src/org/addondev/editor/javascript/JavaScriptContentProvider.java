package org.addondev.editor.javascript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import org.addondev.parser.javascript.JsNode;
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
		
		JsNode n = (JsNode)parentElement;
		//return n.getChildNode();
		return n.getChildNode().toArray(new JsNode[n.getChildNode().size()]);
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		//return null;
		//return ((File)element).getParentFile();
		return ((JsNode)element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		//return false;
		//return n.jjtGetNumChildren() == 0 ? false : true; 
		//JavaScriptNode n = (JavaScriptNode)element;
		//return n.getChildrenNum() == 0 ? false:true;
		
		JsNode n = (JsNode)element;
		return n.getChildrenNum() == 0 ? false:true;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		//return null;
		
		//JavaScriptNode node = (JavaScriptNode)inputElement;
		//return node.getChildren();
		
		JsNode node = (JsNode)inputElement;
		//return node.getChildren();
		return node.getChildNode().toArray(new JsNode[node.getChildNode().size()]);
		
		
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
