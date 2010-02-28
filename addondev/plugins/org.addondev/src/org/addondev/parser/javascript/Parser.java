package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	private Lexer lex;
	private int token;
	private String fName;
	private String fJsDoc;
	
	public JsNode root;
	
	//private Frame frame = new Frame();
	//private Stack<JsNode> thisNodeStack = new Stack<JsNode>();
	//private Stack<Scope> ScopeStack = new Stack<Scope>();
	private ScopeStack fScopeStack = new ScopeStack();
	
	private void setJsDoc(JsNode node, String jsDoc)
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
				node = fScopeStack.getScope(0).getNode(image); //global this
			}
			if(node == null)
			{
				node = ScopeManager.instance().getGlobalNode(image); //global other
			}
//		}
		return node;
	}
	
	private JsNode findChildNode(JsNode node, String image)
	{
		if(node == null) return null;
		ArrayList<JsNode> childs = node.getChildNode();
		if(childs == null) return null;
		
		for (JsNode child : childs) {
			if(child.getImage().equals(image))
			{
				return child;
			}			
		}		
		return null;		
	}
	
	private JsNode getNodeByType(String type)
	{
		JsNode node = null;
		JsNode gnode = findNode(type);
		//JsNode chNode = JsNodeHelper.findChildNode(gnode, "prototype");
		JsNode chNode = findChildNode(gnode, "prototype");
		if(chNode != null)
		{
			node = new JsNode(null, "var", lex.value(), 0);
			//JsNodeHelper.assignCloneNode(node.getChildNode(), chNode.getChildNode()); 
			cloneChildNode(chNode, node);
		}
		else
		{
			node = gnode;
		}	
		
		return node;
	}
	
	private void cloneChildNode(JsNode srcNode, JsNode distNode)
	{
		List<JsNode> srcChildNodes = srcNode.getChildNode();
		List<JsNode> distChildNodes = distNode.getChildNode();
		
		for (JsNode node : srcChildNodes) 
		{
			distChildNodes.add(node.getClone(distNode));
		}
	}
	
	private void assignChildNode(JsNode srcNode, JsNode distNode)
	{
		List<JsNode> srcChildNodes = srcNode.getChildNode();
		List<JsNode> distChildNodes = distNode.getChildNode();
		
//		for (JsNode node : srcChildNodes) 
//		{
//			if(!distChildNodes.contains(node))
//			{
//				distChildNodes.add(node);
//			}
//		}
		for (int i = 0; i < srcChildNodes.size(); i++) {
			if(!hasNode(distChildNodes, srcChildNodes.get(i)))
			{
				distChildNodes.add(srcChildNodes.get(i));
			}
		}
	}
	
	private boolean hasNode(List<JsNode> nodelist, JsNode node)
	{
		for (JsNode jsNode : nodelist) {
			if(jsNode.getImage().equals(node.getImage()))
				return true;
		}	
		
		return false;
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
	int endoff;
	public JsNode parse(String src) {
		endoff = src.length();
		JsNode code = null;
		try {
			lex = new Lexer(src);
			code = program();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}
	
	public JsNode parse(String src, Scope scope) {
		root = new JsNode(null, "root", "root", 0);
		try {
			lex = new Lexer(src);
			fScopeStack.pushScope(scope);
			while (token != TokenType.EOS) {
				stmt(root);
				getToken(); 
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}
	
	private JsNode program() throws EOSException {
		root = new JsNode(null, "root", "root", 0);
		//thisNodeStack.push(root); //global
		fScopeStack.pushScope(new Scope(0, endoff, root));
		int offset = lex.offset();
		//frame.push();
		while (token != TokenType.EOS) {
			stmt(root);
			getToken(); 
			
			if(offset == lex.offset() && token != TokenType.EOS) 
				throw new EOSException();
			
			offset = lex.offset();
		}
		//frame.pop();
		
		//thisNodeStack.pop();

		//Scope scope = fScopeStack.popScope();
		//scope.setEnd(lex.offset());
		
		ScopeManager.instance().setScopeStack(fName, fScopeStack);
		
		return root;
	}

	private void stmt(JsNode parent) throws EOSException {	
			switch (token) {	
			case '/':
				int i=0;
				lex.jsRex();
				//getToken(); 
				break;
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
					if(res != null)
					{
						//JsNodeHelper.assignNode(node.getChildNode(), res.getChildNode());
						assignChildNode(res, node);
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
						if(token == TokenType.EOS)
						{
							break;
						}
						getToken();	//.	
						
						if(token == TokenType.EOS)
						{
							break;
						}
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
						JsNode res = factor(node);
						if(res != null)
						{
							//JsNodeHelper.assignNode(node.getChildNode(), res.getChildNode());
							assignChildNode(res, node);
						}
						//getToken();
					}	
					else if(token == '(')
					{
						//factor(node);
						functionCall(node);
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
	
	private void functionDef(JsNode node) throws EOSException {
		getToken(); //skip (
		
		ArrayList<String> args = new ArrayList<String>();
		
		if (token != ')') {

			//factor(node);
			args.add(lex.value());
			getToken();
			
			while (token != ')') 
			{	
				if(token == TokenType.EOS)
				{
					break;
				}
				getToken();  // skip ,
				
				//factor(node);
				args.add(lex.value());
				getToken();
				
				int i = 0;
			}

			getToken(); // skip ),
			if(token == '{')
			{
				//getToken(); // skip {
				//node.setOffset(lex.offset());
				//getToken();
				fScopeStack.pushScope(new Scope(lex.offset()+1, node));
				for (String arg : args) {
					JsNode argnode = new JsNode(node, "var", arg, 0);
					node.addChild(argnode);
				}
				block(node);
				//fun(node);
				//objectExpr(node);
				//block(node);
				Scope scope = fScopeStack.popScope();
				scope.setEnd(lex.offset());
			}			
		}
	}
	
	private void functionCall(JsNode node) throws EOSException {
		JsNode code = null;
		
		getToken(); //skip (
		if (token != ')') {

			factor(node);

			while (token != ')') 
			{	
				if(token == TokenType.EOS)
				{
					break;
				}
				getToken();  // skip ,
				factor(node);
				
				int i = 0;
			}

			getToken(); // skip ),
			if(token == '{')
			{
				//getToken(); // skip {
				//node.setOffset(lex.offset());
				//getToken();
				int of = lex.offset();
				//fScopeStack.pushScope(new Scope(lex.offset()+1, node));
				fScopeStack.pushScope(new Scope(lex.offset(), node));
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
			//fScopeStack.pushScope(new Scope(offset, code));
			
			//frame.setNode(code);
			//thisNodeStack.push(code);
			//frame.push();				
			getToken();
			advanceToken('(');
			functionCall(code);
			
			//block(code);
			//thisNodeStack.pop();
			//Scope scope = fScopeStack.popScope();
			//int endoffset = lex.offset();
			//scope.setEnd(endoffset);
			
			//frame.pop(); 
	    }
	    else if(token == '(') //anonymous
	    {
//	    	int offset = lex.offset();	 
//	    	code = new JsNode(parent, "function", "anonymous", offset);
//	    	parent.addChild(code);
//   	
//	    	fScopeStack.pushScope(new Scope(offset, code));
//				
//	    	//frame.push();
//			getToken();
//			advanceToken(')');
//			
//			getToken();
//			block(code);
//
//			Scope scope = fScopeStack.popScope();
//			int endoffset = lex.offset();
//			scope.setEnd(endoffset);			
			
			//frame.pop(); 
	    	functionCall(parent);
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
			if(res != null)
			{
				//JsNodeHelper.assignNode(node.getChildNode(), res.getChildNode());
				assignChildNode(res, node);
			}	
			//getToken(); //test 100226
		}
	}
	
	private JsNode factor(JsNode parent) throws EOSException {
		JsNode node = null;
		String sym = lex.value();
		switch(token){
		case '/':
			int i=0;
			lex.jsRex();
			//getToken(); 
			break;
		case '{':
			parent.setOffset(lex.offset());
			//thisNodeStack.push(parent);
			//frame.push(); //test
			objectExpr(parent);
			//frame.pop();  //test
			//thisNodeStack.pop();	
			//node = parent;
			break;
		case TokenType.FUNCTION:
			//thisNodeStack.push(parent);
			advanceToken('(');
			//frame.push();
			//parent.setId("function");
			setJsDoc(parent, fJsDoc);
			functionCall(parent);			
			//advanceToken('{');			
			//block(parent);
			//String ll = lex.value();
			//frame.pop();
			//thisNodeStack.pop();			
			break;
		case TokenType.NEW:
			getToken();
			String obj = lex.value(); 			
			JsNode tnode = findNode(obj);
			JsNode prototypenode = tnode==null?null:tnode.getChild("prototype");
			if(prototypenode != null)
			{
				node = new JsNode(null, "var", sym, 0);
				//JsNodeHelper.assignCloneNode(parent.getChildNode(), prototypenode.getChildNode());
				cloneChildNode(prototypenode, node);
			}
			break;
		case TokenType.ARRAY:
			node = getNodeByType("Array");
			advanceToken(']');
			getToken(); //skip ']'
			break;
		case TokenType.STRING:
			node = getNodeByType("String");
			getToken();
			break;
		case TokenType.INT:
			node = getNodeByType("Number");
			getToken();
			break;
		case TokenType.SYMBOL:
			getToken();
			if(token == '('){
				functionCall(parent);
		    }
			else
			{
				node = findNode(sym);
				if(node == null)
				{
					JsNode valnode = new JsNode(parent, "var", sym, lex.offset());
					parent.addChild(valnode);
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
						    	//node = NodeManager.getInstance().getGlobalNode(param);
						    	node = findNode(param);
						    }
						}
						else
						{							
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
							    	//node = NodeManager.getInstance().getGlobalNode(param);
							    	//if(node == null) node = findNode(param);
							    	node = findNode(param);
							    }
							}
							else
							{
								
							}
						}
					}
				}
//				else
//				{
//					//functionExpr(node);
//					functionExpr(node);
//					//getToken();
//				}
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
							node = getNodeByType(type);
						}
						else
						{
							JsNode valnode = new JsNode(node, "var", sym, lex.offset());
							node.addChild(valnode);
							node =valnode;							
						}
					}
					else
					{
						String type = anode.getType();
						if(type != null)
						{
							node = getNodeByType(type);
						}
						else
						{
							node =anode;
							//JsNode valnode = new JsNode(parent, "var", sym, lex.offset());
							//node =anode;
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
					
					//frame.push();

					functionCall(node);
					//functionDef(node);
					
//					getToken();	
//					while (token != '}' && token != TokenType.EOS) {
//						stmt(node);
//						//parent.setEndoffset(lex.offset());
//						//node.setEndoffset(lex.offset());
//						getToken(); 
//						node.setEndoffset(lex.offset()-1);
//					}

					//frame.pop();
					
					//Scope scope = fScopeStack.popScope();
					//scope.setEnd(lex.offset());
					advanceToken('}');
					getToken();	
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
