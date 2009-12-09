package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
	private Lexer lex;
	private int token;
	
	public JsNode root;
	
	private Frame frame = new Frame();
	private Stack<JsNode> thisNodeStack = new Stack<JsNode>();
	
	private JsNode getNode(JsNode parent, String sym, int offset)
	{
		JsNode node = parent;
		
		List<String> symlist = new ArrayList<String>();
		if(sym.contains("."))
		{
			String[] sp = sym.split("\\.");
			for (int i = 0; i < sp.length; i++) {
				symlist.add(sp[i]);
			}
		}
		else
		{
			symlist.add(sym);
		}
		for (int i = 0; i < symlist.size(); i++) {
			String s = symlist.get(i);
			JsNode newnode = null;
			JsNode[] childs = node.getChildren();
			if(childs != null)
			{
			for (int j = 0; j < childs.length; j++) {
				if(childs[j].getImage().equals(s))
				{
					newnode = childs[j];
					break;
				}
			}
			}
			if(newnode == null)
			{
				newnode = new JsNode(node, "var", s, offset);
				node.addChild(newnode);
			}
			node = newnode;
		}			
		
		return node;
	}
	
	private void setJsDoc(String jsDoc, JsNode node)
	{
		node.setfJsDoc(jsDoc);
		fJsDoc = "";
	}
	
	private void advanceToken(char c) throws EOSException
	{
		while (token != c) 
		{
			if(token == TokenType.EOS)
			{
				throw new EOSException();
			}	
			getToken();
		}
	}
	
	public JsNode parse(Lexer lexer) {
		JsNode code = null;
		lex = lexer;
		try {
			code = program();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	private void getToken() {
		// TODO Auto-generated method stub
		if (lex.advance()) {
			token = lex.token();
		} else {
			token = TokenType.EOS; // 次のトークンが存在しないときにはEOSを設定しておく。
		}
	}

//	private void assign(JsNode assignedNode, )
//	{
//		
//	}
	
	private JsNode program() throws EOSException {
		root = new JsNode(null, "root", "root", 0);
		thisNodeStack.push(root); //global
		
		// TODO Auto-generated method stub
		//nodestakc.push(new NodePass(root, "root"));
		frame.push();
		while (token != TokenType.EOS) {
			stmt(root);
			getToken(); 
		}
		frame.pop();
		
		thisNodeStack.pop();
		//nodestakc.pop();
		return root;
	}
String fJsDoc;
	private void stmt(JsNode parent) throws EOSException {	
		// TODO Auto-generated method stub
			switch (token) {	
			case TokenType.VAR: // 変数宣言
			case TokenType.CONST: 	
				def(parent);		
				break;
			case TokenType.JSDOC:
				fJsDoc = lex.value();
				//getToken();
				break;
			case TokenType.FUNCTION:
				//frame.push();			
				fun(parent); 
				//frame.pop(); 
				break;
			case TokenType.SYMBOL:	
				getToken();
				if (token == '=') {
					String sym = lex.value();
					JsNode node = null;
					if(!frame.hasNodeAllFrame(sym))
					{
						node = new JsNode(root, "var", sym, lex.offset());
						root.addChild(node);
						frame.setRootNode(node);
					}
					else	
					{
						node = frame.findNodeAllFrame(sym);
					}
					
					getToken(); // skip '='
					JsNode res = factor(node);
					//if(res != null)
					//	node.setValueNode(new ValueNode(res));
				}
				else if(token == '.')
				{
					JsNode fromnode;
					String sym = lex.value();
					if(sym.equals("this"))
					{
						fromnode = thisNodeStack.lastElement();
					}
					else
					{
						fromnode = frame.findNodeGlobalFrame(sym);
						
					}
					//getToken();
					String objsym2 = "";	
//					if(fromnode.getId().equals("function"))
//					{
//						objsym2 = ".prototype";
//					}
					while(token == '.')
					{
						getToken();	//.	
						if(token != TokenType.SYMBOL)
						{
							break;
						}
						String val = lex.value();
//						if(!val.equals("prototype"))
//						{
//							objsym2 = objsym2 +"."+ lex.value();
//						}
						objsym2 = objsym2 +"."+ lex.value();
						getToken();	//symbol
					}
					
					if(objsym2.length() > 0)
					{
						objsym2 = objsym2.substring(1);
					
						if(token != TokenType.EOS && token != TokenType.VAR && token != TokenType.SYMBOL) //tmp
						{
							if(fromnode == null)
							{
								fromnode = root;
							}
							JsNode mnode = getNode(fromnode, objsym2, 0);
							setJsDoc(fJsDoc, mnode);
							//if(token == '=' && !objsym2.contains("prototype.")){
							if(token == '='){
								getToken();  // skip '='
								JsNode res = factor(mnode);
								if(res != null)
								{
									JsNodeHelper.assignNode(fromnode.getChildrenList(), res.getChildrenList()); 
								}
								//if(res != null)
								//	mnode.setValueNode(new ValueNode(res));
							}
//							else if(token == '=')
//							{
//								getToken();
//								factor(new JsNode(null, "", "", 0));
//							}
						}
					}
				}
				
				break;
			case '{':
				block(parent);
				break;
			default:
				//getToken(); 
			}
	}

	private void methodCall(JsNode node) throws EOSException {
		if (token != ')') {
			while (token != ')') {
				
			}
		}
	}
	
	private void functionExpr(JsNode node) throws EOSException {
		// TODO Auto-generated method stub
		JsNode code = null;
		List<JsNode> arglist = new ArrayList<JsNode>();
		
		getToken(); //skip (
		if (token != ')') {
			if(token == '{')
			{
				getToken(); //{
				int e = lex.offset();
				node.setOffset(lex.offset());
				
				objectExpr(node);
				frame.setNode(node);
			}
			else if(token == TokenType.FUNCTION)
			{
				while (token != '(') {
					getToken();
				}
				frame.setNode(node);
				
				functionExpr(node);
				block(node);
			}
			else //symbol, number, strigg
			{
				String arg = lex.value();
				frame.setNode(arg);
				//factor(node);
				getToken();		
			}
			
			while (token != ')') {
				if(token == TokenType.EOS)
				{
					throw new EOSException();
				}
				if (token != ',') {
					//throw new Exception("文法エラーです。");
				}
				while (token != ',') {
					if(token == TokenType.EOS)
					{
						return;
						//throw new EOSException();
					}
					getToken(); // skip ','
					if(token == ')' || token == TokenType.VAR) //tmp
					{
						return;
					}
				}
				
				getToken(); // skip ',
				if(token == '{')
				{
					getToken();
					if(code == null)
					{	
						objectExpr(node);
						frame.setNode(node);
					}
					else
					{
						objectExpr(node);
					}
				}
				else
				{
					String arg = lex.value();
					frame.setNode(arg);
					//factor(node);
					getToken();
				}
			}
			//
		}
		else
		{
			getToken(); // skip ),
			if(token == '{')
			{
				//getToken();
				node.setOffset(lex.offset());
				//getToken();
				
				//objectExpr(node);
				//block(node);
			}			
		}
		//getToken();
	}	
	
	private void fun(JsNode parent) throws EOSException {
		// TODO Auto-generated method stub
		JsNode code = null;
		getToken(); //skip function 
		//char ch = (char)token;
	    if(token == TokenType.SYMBOL){
		    String sym = lex.value();
		    code = new JsNode(parent, "function", sym, lex.offset());
			parent.addChild(code);
			
			frame.setNode(code);
			thisNodeStack.push(code);
			frame.push();				
			getToken();
			advanceToken('(');
			functionExpr(code);
			
			block(code);
			thisNodeStack.pop();
			
			frame.pop(); 
	    }
	    else if(token == '(') //anonymous
	    {
	    	code = new JsNode(parent, "function", "anonymous", lex.offset());
	    	parent.addChild(code);
				
	    	frame.push();
			getToken();
			advanceToken(')');
			
			getToken();
			block(code);
			
			frame.pop(); 
	    }
	}

	private void block(JsNode parent) throws EOSException {
		// TODO Auto-generated method stub	
		getToken();	
		while (token != '}' && token != TokenType.EOS) {
			stmt(parent);
			//parent.setEndoffset(lex.offset());
			getToken(); 
		}
		//parent.setEndoffset(lex.offset());
		//getToken();
	}
	
	private void def(JsNode parent) throws EOSException {
		// TODO Auto-generated method stub
		getToken(); // skip 'var'
		if (token != TokenType.SYMBOL) {
			// throw new Exception("文法エラーです。");
		}
		String sym = lex.value();
		//JsNode node = new JsNode(parent, "var", sym, lex.offset());
		JsNode node =null;
		JsNode pnode = parent;
		if(sym.contains("."))
		{
			String[] sp = sym.split("\\.");
			//String objsym = sp[0];			
			for (int i = 0; i < sp.length; i++) {
				node = new JsNode(pnode, "var", sp[i], lex.offset()+sp[i].length());
				pnode.addChild(node);
				if(i==0)
				{
					frame.setNode(node);					
				}
				pnode = node;
			}			
		}
		else
		{
			node = new JsNode(parent, "var", sym, lex.offset());
			parent.addChild(node);
			frame.setNode(node);
		}
		getToken(); //skip symbol
		if(token == '='){
			//expr(node);
			getToken(); // skip '='
			JsNode res = factor(node);
			//node.setValueNode(new ValueNode(res));
			if(res != null)
			{
				if(res.getChildrenList().size() == 0)	
				{
					String type = res.getType();
					if(type != null)
					{
						JsNode gnode = NodeManager.getInstance().getGlobalNode(type);
						JsNode chNode = JsNodeHelper.findChildNode(gnode, "prototype");
						if(chNode != null)
						{
							JsNodeHelper.assignCloneNode(node.getChildrenList(), chNode.getChildrenList());  
						}	
						else
						{
							JsNodeHelper.assignNode(node.getChildrenList(), gnode.getChildrenList()); 
						}
					}
				}
				else
				{
					JsNodeHelper.assignNode(node.getChildrenList(), res.getChildrenList());
				}
			}			
		}

	}
	
	private JsNode factor(JsNode parent) throws EOSException {
		// TODO Auto-generated method stub
		JsNode node = null;
		String sym = lex.value();
		switch(token){
		case '{':
			parent.setOffset(lex.offset());
			thisNodeStack.push(parent);
			frame.push(); //test
			objectExpr(parent);
			frame.pop();  //test
			thisNodeStack.pop();			
			break;
		case TokenType.FUNCTION:
			//parent.setValueNode(valueNode)
			//parent.setValueNode(new ValueNode(fJsDoc));
			thisNodeStack.push(parent);
			advanceToken('(');
			frame.push();	
			functionExpr(parent);			
			advanceToken('{');			
			block(parent);
			frame.pop();
			thisNodeStack.pop();			
			break;
		case TokenType.NEW:
			getToken();
			String obj = lex.value(); 
			node = frame.findNodeGlobalFrame(obj);
			
			JsNode chNode2 = JsNodeHelper.findChildNode(node, "prototype");
			if(chNode2 != null)
			{
				//parent.getChildrenList().clear();
				JsNodeHelper.assignCloneNode(parent.getChildrenList(), chNode2.getChildrenList());
			}			
			break;
		case TokenType.ARRAY:
			node = new JsNode(null, "array", lex.value(), 0);
			//parent.setValueNode(new ValueNode(jsdoc));
			 getToken();
			break;
		case TokenType.STRING:
			JsNode gnode = NodeManager.getInstance().getGlobalNode("String");
			JsNode chNode = JsNodeHelper.findChildNode(gnode, "prototype");
			if(chNode != null)
			{
				node = new JsNode(null, "string", lex.value(), 0);
				JsNodeHelper.assignCloneNode(node.getChildrenList(), chNode.getChildrenList()); 
			}
			getToken();
			break;
		case TokenType.INT:
			//node = new JsNode(null, "int", lex.value(), 0);

//			JsNode gnode1 = NodeManager.getInstance().getGlobalNode("Number");
//			JsNode chNode1 = JsNodeHelper.findChildNode(gnode1, "prototype");
//			if(chNode1 != null)
//			{
//				node = new JsNode(null, "Number", lex.value(), 0);
//				JsNodeHelper.assignCloneNode(node.getChildrenList(), chNode1.getChildrenList()); 
//			}			
			 getToken();
			break;
		case TokenType.SYMBOL:
			//String sym = lex.value();	
			getToken();
			if(token == '('){
				functionExpr(parent);
		    }
			else
			{
				if(frame.hasNodeFrame(sym))
				{
					node = frame.findNodeFrame(sym);
					//parent.getChildrenList().clear();
					//JsNodeHelper.assignNode(parent.getChildrenList(), node.getChildrenList());
				}				
			}

			break;
		 default:	 
			 break;
		}
		
		//String 
		while(token == '.'){
			getToken();  // skip '.'
			sym = lex.value();
			
			if(node != null)
			{
				JsNode anode = node.getChild(sym);
				if(anode == null)
				{
					String type = node.getType();
					if(type != null)
					{
						JsNode gnode = NodeManager.getInstance().getGlobalNode(type);
						JsNode chNode = JsNodeHelper.findChildNode(gnode, "prototype");
						if(chNode != null)
						{
							node = new JsNode(null, type, lex.value(), 0);
							JsNodeHelper.assignCloneNode(node.getChildrenList(), chNode.getChildrenList()); 
							//JsNodeHelper.assignCloneNode(parent.getChildrenList(), chNode.getChildrenList()); 
						}					
					}
				}
				else
				{
					node =anode;
				}
				//JsNode chnode = node.getChild(sym);
			}
			
			getToken();  // skip symbol
			
			if(token == '(')
			{
				//if(node != null)
				//	node = node.getChild(sym);
				
				if(node != null)
				{
					if(sym.equals("createInstance"))
					{
					    ArrayList<JsNode> list = null;
					    if(token != ')'){
					    	getToken();
					      list = new ArrayList<JsNode>();
					      JsNode param = factor(node);
					      //String param = lex.value();
					      list.add(param);
					      while(token != ')'){
					        if(token != ','){
					          //throw new Exception("文法エラーです。");
							      int hh=0;
							      hh++;
					        }
					        getToken();  // skip ','
					        param = factor(node);
					        list.add(param);
					      }
					      JsNode inode = frame.findNodeGlobalFrame(param.getImage());
					      int hh=0;
					      hh++;
					    }
						
					}
					else
					{
						String type = node.getType();
						if(type != null)
						{
							JsNode gnode = NodeManager.getInstance().getGlobalNode(type);
							if(gnode != null)
							{
								node = new JsNode(null, type, "", 0);
								if(JsNodeHelper.hasChildNode(gnode, "prototype"))
								{
									JsNode chNode = JsNodeHelper.findChildNode(gnode, "prototype");
									JsNodeHelper.assignCloneNode(node.getChildrenList(), chNode.getChildrenList()); 
								}
								else
								{
									JsNodeHelper.assignNode(node.getChildrenList(), gnode.getChildrenList()); 
								}
								
								//JsNodeHelper.assignCloneNode(parent.getChildrenList(), chNode.getChildrenList()); 
							}							
						}
					}
//					else if(node.getValueNode().getReturns() != null)
//					{
//						node = frame.findNodeGlobalFrame(node.getValueNode().getReturns());
//					}
				}
				else
				{
					//functionExpr(node);
					functionExpr(node);
					//getToken();
				}
			}
//			else //if(token == TokenType.SYMBOL)
//			{
//				if(node != null)
//					node = node.getChild(sym);
//				
//				//getToken();
//			}
			
			if(token == '[')
			{
				advanceToken(']');
				getToken(); //skip ']'
			}
		}
		
//		if(node != null && node.getValueNode() != null)
//		{
//			node = node.getValueNode().getNode();
//		}
		return node;
			//return node.getValueNode()!=null?node.getValueNode():node;
	}

	private void objectExpr(JsNode parent) throws EOSException {
		// TODO Auto-generated method stub
		//getToken();
		while (token != '}'){
			if(token == TokenType.EOS)
			{
				//throw new EOSException();
				break;
			}
			
			String sym = lex.value();
			getToken();
			if (token == ':') {
				getToken();
				if (token == TokenType.SYMBOL || token == TokenType.STRING) {
					JsNode node = new JsNode(parent, "var", sym, lex.offset());
					parent.addChild(node);
				} else if (token == TokenType.FUNCTION) {
					JsNode node = new JsNode(parent, "function", sym, lex.offset());
					parent.addChild(node);
					advanceToken('(');
					
					frame.push();
					//methodCall(sym, node);
					functionExpr(node);
					getToken();	
					while (token != '}' && token != TokenType.EOS) {
						stmt(node);
						//parent.setEndoffset(lex.offset());
						//node.setEndoffset(lex.offset());
						getToken(); 
						node.setEndoffset(lex.offset()-1);
					}
					//block(node);
					frame.pop();
					
					//advanceToken('}');
					int e = lex.offset();
					//node.setEndoffset(lex.offset());
					//parent.setEndoffset(lex.offset());
				}
				else if(token == '{')
				{
					JsNode node = new JsNode(parent, "var", sym, lex.offset());
					parent.addChild(node);
					objectExpr(node);
					int e = lex.offset();
					//parent.setEndoffset(lex.offset());
				}
			}
		}
		if(token != TokenType.EOS)
		{
			getToken();
			parent.setEndoffset(lex.offset()-1);
			//getToken();
		}
	}
	
	private void newExpr()
	{
		
	}
}