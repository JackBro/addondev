package jp.addondev.parser.javascript;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Frame {
	private Stack<Map<String, JsNode>> frame = new Stack<Map<String, JsNode>>();
	
	public boolean hasNodeAllFrame(String sym)
	{
		for(int i= frame.size()-1; i>=0; i--)
		{
			if(frame.elementAt(i).containsKey(sym))
				return true;
		}
		
		return false;
	}
	
	public JsNode findNodeAllFrame(String sym)
	{
		for(int i= frame.size()-1; i>=0; i--)
		{
			if(frame.elementAt(i).containsKey(sym))
			{
				return frame.elementAt(i).get(sym);
			}
		}
		
		return null;
	}	
		
	public boolean hasNodeCurrentFrame(String sym)
	{
		return frame.lastElement().containsKey(sym);
	}
	
	public JsNode findNodeCurrentFrame(String sym)
	{
		if(frame.lastElement().containsKey(sym))
		{
			return frame.lastElement().get(sym);
		}
		return null;
	}
	
	public boolean hasNodeGlobalFrame(String sym)
	{
		return frame.firstElement().containsKey(sym);
	}
	
	public JsNode findNodeGlobalFrame(String sym)
	{
		if(frame.firstElement().containsKey(sym))
		{
			return frame.firstElement().get(sym);
		}
		return null;
	}

	public boolean hasNodeFrame(String sym)
	{
		boolean res = hasNodeCurrentFrame(sym);
		if(!res)
		{
			res = hasNodeGlobalFrame(sym);
		}
		return res;
	}	
	
	public JsNode findNodeFrame(String sym)
	{
		JsNode node = findNodeCurrentFrame(sym);
		if(node == null)
		{
			node = findNodeGlobalFrame(sym);
		}
		return node;
	}	
	
	public void push()
	{
		frame.add(new HashMap<String, JsNode>());
	}
	
	public void pop()
	{
		frame.pop();
	}
	
	public void setNode(String sym)
	{
		if(!frame.lastElement().containsKey(sym))
		{
			JsNode node = new JsNode(null, "param", sym, 0);
			frame.lastElement().put(node.getImage(), node);
		}		
	}
	
	public void setNode(JsNode node)
	{
		if(!frame.lastElement().containsKey(node.getImage()))
		{
			frame.lastElement().put(node.getImage(), node);
		}
	}
	
	public void setRootNode(JsNode node)
	{
		if(!frame.firstElement().containsKey(node.getImage()))
		{
			frame.firstElement().put(node.getImage(), node);
		}
	}
	
	public void removeNode(JsNode node)
	{
		for (int i = 0; i < frame.size(); i++) {
			if(frame.elementAt(i).containsValue(node))
			{
				//frame.elementAt(i).
				//frame.elementAt(i).remove(key)
				break;
			}
		}
	}
}
