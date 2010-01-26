package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	private Lexer lex;
	private int token;
	private String fName;
	private String fJsDoc;
	
	public JsNode root;
	
	private Frame frame = new Frame();
	//private Stack<JsNode> thisNodeStack = new Stack<JsNode>();
	//private Stack<Scope> ScopeStack = new Stack<Scope>();
	private ScopeStack fScopeStack = new ScopeStack();
	
	private JsNode getNode(JsNode parent, String sym, int offset)
	{	
		JsNode node = parent;
		
		List<String> symlist = new ArrayList<String>();
		if(sym.contains("."))
		{
			String[] sp = sym.split("\\.");
			for (int i = 0; i < sp.length; i++) {
				if(!parent.getImage().equals(sp[i]))
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
			ArrayList<JsNode> child = node.getChildNode();
			if(child != null)
			{
				for (JsNode jsNode : child) {
					if(jsNode.getImage().equals(s))
					{
						newnode = jsNode;
						break;
					}					
				}
//			for (int j = 0; j < childs.length; j++) {
//				if(childs[j].getImage().equals(s))
//				{
//					newnode = childs[j];
//					break;
//				}
//			}
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
	
	private JsNode findNode(String image)
	{
		JsNode node = null;
//		if("this".equals(image))
//		{
//			node = fScopeStack.getCurrntScope().getNode(image);
//		}
//		else
//		{
			node = fScopeStack.getCurrntScope().getNode(image); //current
			if(node == null)
			{
				node = fScopeStack.getScope(0).getNode(image); //global this src
			}
			if(node == null)
			{
				node = ScopeManager.instance().getNode(image); //global thoer src
			}
//		}
		return node;
	}
	
	private JsNode getNodeByType(String type)
	{
		JsNode node = null;
		JsNode gnode = findNode(type);
		JsNode chNode = JsNodeHelper.findChildNode(gnode, "prototype");
		if(chNode != null)
		{
			node = new JsNode(null, "var", lex.value(), 0);
			JsNodeHelper.assignCloneNode(node.getChildNode(), chNode.getChildNode()); 
		}
		else
		{
			node = gnode;
		}	
		
		return node;
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
		if (lex.advance()) {
			token = lex.token();
		} else {
			token = TokenType.EOS; // 次のトークンが存在しないときにはEOSを設定しておく。
		}
	}
	
	public Parser(String name)
	{
		fName = name;
	}
	
	private JsNode program() throws EOSException {
		root = new JsNode(null, "root", "root", 0);
		//thisNodeStack.push(root); //global
		fScopeStack.pushScope(new Scope(0, root));
		
		frame.push();
		while (token != TokenType.EOS) {
			stmt(root);
			getToken(); 
		}
		frame.pop();
		
		//thisNodeStack.pop();

		Scope scope = fScopeStack.popScope();
		scope.setEnd(lex.offset());
		
		ScopeManager.instance().setScope(fName, fScopeStack.getScope(0));
		
		return root;
	}

	private void stmt(JsNode parent) throws EOSException {	
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
					//getToken(); //skep =
					String sym = lex.value();
					JsNode node = null;
					if("this".equals(sym) && parent.getId().equals("propfunction"))
					{
						node = parent.getParent();
						
					}
					else
					{
						node = findNode(sym);
					}
					
					//JsNode node = findNode(sym);
					if(node == null)
					{
						node = new JsNode(root, "var", sym, lex.offset());
						root.addChild(node);
					}
					
					getToken(); // skip '='
					JsNode res = factor(node);
					if(res == null)
					{
						//node.setBindNode();
						//parent.setBindNode(node);
					}
					else
					{
						//node.setBindNode(res.getBindNode()==null?res:res.getBindNode());
						JsNodeHelper.assignNode(node.getChildNode(), res.getChildNode());
					}
				}
				else if(token == '.')
				{
					String sym = lex.value();
					JsNode node = null;
					if("this".equals(sym) && parent.getId().equals("propfunction"))
					{
						node = parent.getParent();
						
					}
					else
					{
						node = findNode(sym);
					}
					//JsNode node = findNode(sym);
					
					if(node == null)
					{
						node = new JsNode(root, "var", sym, lex.offset());
						root.addChild(node);
					}
					
					//getToken(); // skip .
					while(token == '.')
					{
						getToken();	//.	
//						if(token != TokenType.SYMBOL)
//						{
//							break;
//						}
						String val = lex.value();
						
						JsNode m = node.getChild(val);
						if(m == null)
						{
							JsNode cnode = new JsNode(node, "var", val, lex.offset());
							node.addChild(cnode);
							node = cnode;
						}
						else
						{
							node = m;
						}	
						getToken();	//.	
					}

					if(token == '='){
						getToken();  // skip '='
						factor(node);
						//getToken();
					}		
					
//					JsNode fromnode;
//					String sym = lex.value();
//					if(sym.equals("this"))
//					{
//						fromnode = thisNodeStack.lastElement();
//					}
//					else
//					{
//						fromnode = frame.findNodeGlobalFrame(sym);
//						if(fromnode == null)
//						{
//							fromnode = frame.findNodeCurrentFrame(sym);
//						}
//						
//					}
//
//					JsNode fnode = findNode(sym);
//					//JsNode chNode = fnode.getChild(val);
//					if(fnode == null)
//					{
//						fnode = new JsNode(parent, "var", sym, lex.offset());
//						parent.addChild(fnode);
//					}
//					
//					String objsym2 = lex.value();
////					if(fromnode.getId().equals("function"))
////					{
////						objsym2 = ".prototype";
////					}
//					while(token == '.')
//					{
//						getToken();	//.	
//						if(token != TokenType.SYMBOL)
//						{
//							break;
//						}
//						String val = lex.value();
//						
//						JsNode m = fnode.getChild(val);
//						if(m == null)
//						{
//							JsNode cnode = new JsNode(fnode, "var", val, lex.offset());
//							fnode.addChild(cnode);
//							fnode = cnode;
//						}
//						else
//						{
//							fnode = m;
//						}
//						
//						objsym2 = objsym2 +"."+ lex.value();
//						getToken();	//symbol
//					}
//					
//					if(objsym2.length() > 0)
//					{
//						//objsym2 = objsym2.substring(1);
//					
//						if(token != TokenType.EOS && token != TokenType.VAR && token != TokenType.SYMBOL) //tmp
//						{
//							if(fromnode == null)
//							{
//								fromnode = root;
//							}
//							JsNode mnode = getNode(fromnode, objsym2, 0);
//							setJsDoc(fJsDoc, mnode);
//							//if(token == '=' && !objsym2.contains("prototype.")){
//							if(token == '='){
//								getToken();  // skip '='
//								factor(mnode);
////								JsNode res = factor(mnode);
////								if(res != null)
////								{
////									JsNodeHelper.assignNode(fromnode.getChildrenList(), res.getChildrenList()); 
////								}
//								//if(res != null)
//								//	mnode.setValueNode(new ValueNode(res));
//							}
//							else if(token == '(')
//							{
//								getToken();
//								//factor(new JsNode(null, "", "", 0));
//								//factor(mnode);
//								
//								JsNode code = new JsNode(mnode, "function", "anonymous", lex.offset());
//								mnode.addChild(code);
//									
//						    	frame.push();
//								getToken();
//								advanceToken(')');
//								
//								getToken();
//								block(code);
//								
//								frame.pop(); 
//								
//							}
//						}
//					}
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
		JsNode code = null;
		//List<JsNode> arglist = new ArrayList<JsNode>();
		
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
			getToken(); // skip ),
			if(token == '{')
			{
				//getToken(); // skip {
				//node.setOffset(lex.offset());
				//getToken();
				fScopeStack.pushScope(new Scope(lex.offset()+1, node));
				block(node);
				//fun(node);
				//objectExpr(node);
				//block(node);
				Scope scope = fScopeStack.popScope();
				scope.setEnd(lex.offset());
			}			
		}
		else
		{
			getToken(); // skip ),
			if(token == '{')
			{
				//getToken(); // skip {
				//node.setOffset(lex.offset());
				//getToken();
				fScopeStack.pushScope(new Scope(lex.offset()+1, node));
				block(node);
				//fun(node);
				//objectExpr(node);
				//block(node);
				Scope scope = fScopeStack.popScope();
				scope.setEnd(lex.offset());
			}			
		}
	}	
	
	private void fun(JsNode parent) throws EOSException {
		// TODO Auto-generated method stub
		JsNode code = null;
		getToken(); //skip function 
		//char ch = (char)token;
	    if(token == TokenType.SYMBOL){
		    String sym = lex.value();
		    int offset = lex.offset();
		    code = new JsNode(parent, "function", sym, offset);
		    //code.setType(sym);
			parent.addChild(code);
			//ScopeStack.push(new Scope(lex.offset(), code));
			fScopeStack.pushScope(new Scope(offset, code));
			
			frame.setNode(code);
			//thisNodeStack.push(code);
			frame.push();				
			getToken();
			advanceToken('(');
			functionExpr(code);
			
			//block(code);
			//thisNodeStack.pop();
			Scope scope = fScopeStack.popScope();
			int endoffset = lex.offset();
			scope.setEnd(endoffset);
			
			frame.pop(); 
	    }
	    else if(token == '(') //anonymous
	    {
	    	int offset = lex.offset();	 
	    	code = new JsNode(parent, "function", "anonymous", offset);
	    	parent.addChild(code);
   	
	    	fScopeStack.pushScope(new Scope(offset, code));
				
	    	frame.push();
			getToken();
			advanceToken(')');
			
			getToken();
			block(code);

			Scope scope = fScopeStack.popScope();
			int endoffset = lex.offset();
			scope.setEnd(endoffset);			
			
			frame.pop(); 
	    }
	}

	private void block(JsNode parent) throws EOSException {
		// TODO Auto-generated method stub	
		getToken();	
		while (token != '}' && token != TokenType.EOS) {
			stmt(parent);
			getToken(); 
		}
		//getToken(); //skip }
	}
	
	private void def(JsNode parent) throws EOSException {
		getToken(); // skip 'var'
		if (token != TokenType.SYMBOL) {
			// throw new Exception("文法エラーです。");
		}
		String sym = lex.value();
		JsNode node = new JsNode(parent, "var", sym, lex.offset());
		parent.addChild(node);
		
		getToken(); //skip symbol
		if(token == '='){
			getToken(); //skip =
			JsNode res =factor(node);
			if(res == null)
			{
				//node.setBindNode();
				//parent.setBindNode(node);
			}
			else
			{
				//node.setBindNode(res.getBindNode()==null?res:res.getBindNode());
				JsNodeHelper.assignNode(node.getChildNode(), res.getChildNode());
			}			
		}
		//stmt(node);
	}
	
	private JsNode factor(JsNode parent) throws EOSException {
		// TODO Auto-generated method stub
		JsNode node = null;
		String sym = lex.value();
		switch(token){
		case '{':
			parent.setOffset(lex.offset());
			//thisNodeStack.push(parent);
			frame.push(); //test
			objectExpr(parent);
			frame.pop();  //test
			//thisNodeStack.pop();	
			//node = parent;
			break;
		case TokenType.FUNCTION:
			//thisNodeStack.push(parent);
			advanceToken('(');
			frame.push();
			//parent.setId("function");
			functionExpr(parent);			
			//advanceToken('{');			
			//block(parent);
			String ll = lex.value();
			frame.pop();
			//thisNodeStack.pop();			
			break;
		case TokenType.NEW:
			getToken();
			String obj = lex.value(); 			
			node = findNode(obj);
			JsNode pnode = node==null?null:node.getChild("prototype");
			if(pnode != null)
			{
				JsNodeHelper.assignCloneNode(parent.getChildNode(), pnode.getChildNode());
			}
			break;
		case TokenType.ARRAY:
			//node = new JsNode(null, "Array", lex.value(), 0);
			//parent.setValueNode(new ValueNode(jsdoc));
			// getToken();
			node = getNodeByType("Array");
			advanceToken(']');
			getToken(); //skip ']'
			break;
		case TokenType.STRING:
//			JsNode gnode = NodeManager.getInstance().getGlobalNode("String");
//			if(gnode != null)
//			{
//				JsNode chNode = JsNodeHelper.findChildNode(gnode, "prototype");
//				if(chNode != null)
//				{
//					node = new JsNode(null, "string", lex.value(), 0); 
//					JsNodeHelper.assignCloneNode(node.getChildNode(), chNode.getChildNode()); 
//				}
//			}
			node = getNodeByType("String");
			getToken();
			break;
		case TokenType.INT:
//			JsNode gnode1 = NodeManager.getInstance().getGlobalNode("Number");
//			if(gnode1 != null)
//			{
//				JsNode chNode1 = JsNodeHelper.findChildNode(gnode1, "prototype");
//				if(chNode1 != null)
//				{
//					node = new JsNode(null, "Number", lex.value(), 0);
//					JsNodeHelper.assignCloneNode(node.getChildNode(), chNode1.getChildNode()); 
//				}
//			}
			node = getNodeByType("Number");
			getToken();
			break;
		case TokenType.SYMBOL:
			getToken();
			if(token == '('){
				functionExpr(parent);
		    }
			else
			{
				node = findNode(sym);
			}
			break;
		 default:	 
			 break;
		}
		
		//String 
		while(token == '.'){
			getToken();  // skip '.'
			sym = lex.value();
				
			getToken();  // skip symbol
			if(token == '(')
			{
				if(node != null)
				{
					String type = node.getType();
					if(type != null)
					{
						if("interfaces".equals(type))
						{
							String param = null;
						    if(token != ')'){
						    	getToken();
						    	//param = lex.value();
						    	while(token != ')'){
						    		getToken();  // skip ','
						    	}
						    	param = lex.value();
						    }
						    if(param != null)
						    {
						    	node = NodeManager.getInstance().getGlobalNode(param);
						    }
						}
						else
						{
//							JsNode gnode = NodeManager.getInstance().getGlobalNode(type);
//							if(gnode == null) gnode = findNode(type);
//							JsNode chNode = JsNodeHelper.findChildNode(gnode, "prototype");
//							if(chNode != null)
//							{
//								node = new JsNode(null, "var", lex.value(), 0);
//								JsNodeHelper.assignCloneNode(node.getChildNode(), chNode.getChildNode()); 
//							}
//							else
//							{
//								node = gnode;
//							}	
							
							node = getNodeByType(type);
						}
					}
					else
					{
						JsNode anode = node.getChild(sym);
						if(anode != null)
						{
							type = anode.getType();
							if("interfaces".equals(type))
							{
								String param = null;
							    if(token != ')'){
							    	getToken();
							    	while(token != ')'){
							    		getToken();  // skip ','
							    	}
							    	param = lex.value();
							    }
							    if(param != null)
							    {
							    	node = NodeManager.getInstance().getGlobalNode(param);
							    	if(node == null) node = findNode(param);
							    }
							}
							else
							{
								
							}
						}
					}
				}
				else
				{
					//functionExpr(node);
					functionExpr(node);
					//getToken();
				}
			}
			else
			{
				if(token == '[')
				{
					advanceToken(']');
					getToken(); //skip ']'
					
				}
				
				if(node != null)
				{
					JsNode anode = node.getChild(sym);
					if(anode == null)
					{
						String type = node.getType();
						if(type != null)
						{
//							JsNode gnode = NodeManager.getInstance().getGlobalNode(type);
//							if(gnode == null) gnode = findNode(type);
//							JsNode chNode = JsNodeHelper.findChildNode(gnode, "prototype");
//							if(chNode != null)
//							{
//								node = new JsNode(null, "var", lex.value(), 0);
//								JsNodeHelper.assignCloneNode(node.getChildNode(), chNode.getChildNode()); 
//							}
//							else
//							{
//								node = gnode;
//							}
							node = getNodeByType(type);
						}
					}
					else
					{
						String type = anode.getType();
						if(type != null)
						{
//							JsNode gnode = NodeManager.getInstance().getGlobalNode(type);
//							if(gnode == null) gnode = findNode(type);
//							JsNode chNode = JsNodeHelper.findChildNode(gnode, "prototype");
//							if(chNode != null)
//							{
//								node = new JsNode(null, "var", lex.value(), 0);
//								JsNodeHelper.assignCloneNode(node.getChildNode(), chNode.getChildNode()); 
//							}
//							else
//							{
//								node = gnode;
//							}
							node = getNodeByType(type);
						}
						else
						{
							node =anode;
						}
					}
				}				
			}
		}

		return node;
	}


	
	private void objectExpr(JsNode parent) throws EOSException 
	{
		//fScopeStack.pushScope(new Scope(lex.offset(), parent));
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
					JsNode node = new JsNode(parent, "propfunction", sym, lex.offset());
					parent.addChild(node);
					//fScopeStack.pushScope(new Scope(lex.offset(), node, "prop"));
					
					advanceToken('(');
					
					frame.push();

					functionExpr(node);
//					getToken();	
//					while (token != '}' && token != TokenType.EOS) {
//						stmt(node);
//						//parent.setEndoffset(lex.offset());
//						//node.setEndoffset(lex.offset());
//						getToken(); 
//						node.setEndoffset(lex.offset()-1);
//					}

					frame.pop();
					
					//Scope scope = fScopeStack.popScope();
					//scope.setEnd(lex.offset());
					//advanceToken('}');
					//int e = lex.offset();
					//node.setEndoffset(lex.offset());
					//parent.setEndoffset(lex.offset());
				}
				else if(token == '{')
				{
					JsNode node = new JsNode(parent, "var", sym, lex.offset());
					parent.addChild(node);
					objectExpr(node);
					//int e = lex.offset();
					//parent.setEndoffset(lex.offset());
				}
			}
		}
		if(token != '}' && token != TokenType.EOS)
		{
			getToken();
			//parent.setEndoffset(lex.offset()-1);
			//getToken();
		}
		parent.setEndoffset(lex.offset()-1);
		//Scope scope = fScopeStack.popScope();
		//scope.setEnd(lex.offset());
	}
}
