package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	private Lexer lex;
	private int token;
	private String fName;
	private String fJsDoc;

	public JsNode root;

	private ScopeStack fScopeStack = new ScopeStack();

	private void setJsDoc(JsNode node, String jsDoc) {
		if (fJsDoc != null && fJsDoc.length() > 0) {
			node.setfJsDoc(jsDoc);

		}
		fJsDoc = "";
	}

	private JsNode findNode(String image) {
		JsNode node = null;
		// if("this".equals(image))
		// {
		// node = fScopeStack.getCurrntScope().getNode(image);
		// }
		// else
		// {
		node = fScopeStack.getCurrntScope().getNode(image); // current
		if (node == null) {
			node = fScopeStack.getScope(0).getNode(image); // global this
		}
		if (node == null) {
			node = ScopeManager.instance().getGlobalNode(image); // global other
		}
		// }
		return node;
	}

	private JsNode findChildNode(JsNode node, String image) {
		if (node == null)
			return null;
		
		return node.getChild(image);
		
//		ArrayList<JsNode> childs = node.getChildNode();
//		if (childs == null)
//			return null;
//
//		for (JsNode child : childs) {
//			if (child.getName().equals(image)) {
//				return child;
//			}
//		}
//		return null;
	}

	private JsNode getNodeByType(String type) {
		JsNode node = null;
		JsNode gnode = findNode(type);
		// JsNode chNode = JsNodeHelper.findChildNode(gnode, "prototype");
		JsNode chNode = findChildNode(gnode, "prototype");
		if (chNode != null) {
			node = new JsNode(null, EnumNode.VALUE_PROP, lex.value(), 0);
			// JsNodeHelper.assignCloneNode(node.getChildNode(),
			// chNode.getChildNode());
			cloneChildNode(chNode, node);
		} else {
			node = gnode;
		}

		return node;
	}

	private void cloneChildNode(JsNode srcNode, JsNode distNode) {
//		List<JsNode> srcChildNodes = srcNode.getChildNode();
//		List<JsNode> distChildNodes = distNode.getChildNode();
//
//		for (JsNode node : srcChildNodes) {
//			distChildNodes.add(node.getClone(distNode));
//		}
		
		JsNode[] srcChildNodes = srcNode.getChildNodes();
		for (JsNode node : srcChildNodes) {
			distNode.addChildNode(node.getClone(distNode));
		}
	}

	private void assignChildNode(JsNode srcNode, JsNode distNode) {
//		List<JsNode> srcChildNodes = srcNode.getChildNode();
//		List<JsNode> distChildNodes = distNode.getChildNode();
//
//		for (int i = 0; i < srcChildNodes.size(); i++) {
//			if (!hasNode(distChildNodes, srcChildNodes.get(i))) {
//				distChildNodes.add(srcChildNodes.get(i));
//			}
//		}
		
		JsNode[] srcChildNodes = srcNode.getChildNodes();
		//JsNode[] distChildNodes = distNode.getChildNodes();

		for (JsNode node : srcChildNodes) {
//			if (!hasNode(distChildNodes, srcChildNodes.get(i))) {
//				distChildNodes.add(srcChildNodes.get(i));
//			}
			if(!distNode.hasChildNode(node.getName()))
			{
				distNode.addChildNode(node);
			}
		}
	}

//	private boolean hasNode(List<JsNode> nodelist, JsNode node) {
//		for (JsNode jsNode : nodelist) {
//			if (jsNode.getName().equals(node.getName()))
//				return true;
//		}
//
//		return false;
//	}

	private void advanceToken(char c) throws EOSException {
		while (token != c) {
			if (token == TokenType.EOS) {
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

	public Parser(String name) {
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
		root = new JsNode(null, EnumNode.ROOT, "root", 0);
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
		root = new JsNode(null, EnumNode.ROOT, "root", 0);
		// thisNodeStack.push(root); //global
		fScopeStack.pushScope(new Scope(0, endoff, root));
		int offset = lex.offset();
		// frame.push();
		while (token != TokenType.EOS) {
			stmt(root);
			getToken();

			if (offset == lex.offset() && token != TokenType.EOS)
				throw new EOSException();

			offset = lex.offset();
		}
		ScopeManager.instance().setScopeStack(fName, fScopeStack);

		return root;
	}

	private void stmt(JsNode parent) throws EOSException {
		switch (token) {
		case '/':
			int i = 0;
			lex.jsRex();
			// getToken();
			break;
		case TokenType.VAR: // 変数宣言
		case TokenType.CONST:
			def(parent);
			break;
		case TokenType.JSDOC:
			fJsDoc = lex.value();
			// getToken();
			break;
		case TokenType.FUNCTION:
			// frame.push();
			fun(parent);
			// frame.pop();
			break;
		case TokenType.SYMBOL:
			getToken();
			if (token == '=') {
				// getToken(); //skep =
				String sym = lex.value();
				JsNode node = null;
				//if ("this".equals(sym) && parent.getId().equals("propfunction")) {
				if ("this".equals(sym) && parent.getNodeType() == EnumNode.FUNCTION_PROP) {
					node = parent.getParent();

				} else {
					node = findNode(sym);
				}

				// JsNode node = findNode(sym);
				if (node == null) {
					node = new JsNode(root, EnumNode.VALUE, sym, lex.offset());
					root.addChildNode(node);
				}

				getToken(); // skip '='
				JsNode res = factor(node);
				if (res != null) {
					// JsNodeHelper.assignNode(node.getChildNode(),
					// res.getChildNode());
					assignChildNode(res, node);
				}
			} else if (token == '.') {
				String sym = lex.value();
				JsNode node = null;
				//if ("this".equals(sym) && parent.getId().equals("propfunction")) {
				if ("this".equals(sym) && parent.getNodeType() == EnumNode.FUNCTION_PROP) {
					node = parent.getParent();

				} else {
					node = findNode(sym);
				}
				// JsNode node = findNode(sym);

				if (node == null) {
					node = new JsNode(root, EnumNode.VALUE, sym, lex.offset());
					root.addChildNode(node);
				}

				// getToken(); // skip .
				while (token == '.') {
					if (token == TokenType.EOS) {
						break;
					}
					getToken(); // .

					if (token == TokenType.EOS) {
						break;
					}
					// if(token != TokenType.SYMBOL)
					// {
					// break;
					// }
					String val = lex.value();
					JsNode m = node.getChild(val);
					if (m == null) {
						JsNode cnode = new JsNode(node, EnumNode.VALUE, val, lex.offset());
						node.addChildNode(cnode);
						node = cnode;
					} else {
						node = m;
					}
					getToken(); // .
				}

				if (token == '=') {
					getToken(); // skip '='
					JsNode res = factor(node);
					if (res != null) {
						// JsNodeHelper.assignNode(node.getChildNode(),
						// res.getChildNode());
						assignChildNode(res, node);
					}
					// getToken();
				} else if (token == '(') {
					// factor(node);
					functionCall(node);
				}
			}
			break;
		case '{':
			block(parent);
			break;
		default:
			// getToken();
		}
	}

	private void functionCall(JsNode node) throws EOSException {
		JsNode code = null;

		getToken(); // skip (
		if (token != ')') {

			factor(node);

			while (token != ')') {
				if (token == TokenType.EOS) {
					break;
				}
				getToken(); // skip ,
				factor(node);
			}
			
//			JsNode[] paramnodes = node.getChildNodes();
//			for (JsNode pnode : paramnodes) {
//				pnode.setNodeType(EnumNode.PARAM);
//			}

			getToken(); // skip ),
			if (token == '{') {
				// getToken(); // skip {
				// node.setOffset(lex.offset());
				// getToken();
				int of = lex.offset();
				// fScopeStack.pushScope(new Scope(lex.offset()+1, node));
				fScopeStack.pushScope(new Scope(lex.offset(), node));
				block(node);
				// fun(node);
				// objectExpr(node);
				// block(node);
				Scope scope = fScopeStack.popScope();
				scope.setEnd(lex.offset());
			}
		} else {
			getToken(); // skip ),
			if (token == '{') {
				// getToken(); // skip {
				// node.setOffset(lex.offset());
				// getToken();
				fScopeStack.pushScope(new Scope(lex.offset() + 1, node));
				block(node);
				// fun(node);
				// objectExpr(node);
				// block(node);
				Scope scope = fScopeStack.popScope();
				scope.setEnd(lex.offset());
			}
		}
	}

	private void fun(JsNode parent) throws EOSException {
		// TODO Auto-generated method stub
		JsNode code = null;
		getToken(); // skip function
		// char ch = (char)token;
		if (token == TokenType.SYMBOL) {
			String sym = lex.value();
			int offset = lex.offset();
			code = new JsNode(parent, EnumNode.FUNCTION, sym, offset);
			parent.addChildNode(code);
			getToken();
			advanceToken('(');
			functionCall(code);
		} else if (token == '(') // anonymous
		{
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
		// getToken(); //skip }
	}

	private void def(JsNode parent) throws EOSException {
		getToken(); // skip 'var'
		if (token != TokenType.SYMBOL) {
			// throw new Exception("文法エラーです。");
		}
		String sym = lex.value();
		JsNode node = new JsNode(parent, EnumNode.VALUE, sym, lex.offset());
		parent.addChildNode(node);

		getToken(); // skip symbol
		if (token == '=') {
			getToken(); // skip =
			JsNode res = factor(node);
			if (res != null) {
				// JsNodeHelper.assignNode(node.getChildNode(),
				// res.getChildNode());
				assignChildNode(res, node);
			}
			// getToken(); //test 100226
		}
	}

	private JsNode factor(JsNode parent) throws EOSException {
		JsNode node = null;
		String sym = lex.value();
		switch (token) {
		case '/':
			int i = 0;
			lex.jsRex();
			// getToken();
			break;
		case '{':
			parent.setOffset(lex.offset());
			// thisNodeStack.push(parent);
			// frame.push(); //test
			parent.setNodeType(EnumNode.OBJECT);
			objectExpr(parent);
			// frame.pop(); //test
			// thisNodeStack.pop();
			// node = parent;
			break;
		case TokenType.FUNCTION:
			// thisNodeStack.push(parent);
			advanceToken('(');
			// frame.push();
			// parent.setId("function");
			setJsDoc(parent, fJsDoc);
			functionCall(parent);
			// advanceToken('{');
			// block(parent);
			// String ll = lex.value();
			// frame.pop();
			// thisNodeStack.pop();
			// TODO

			break;
		case TokenType.NEW:
			getToken();
			String obj = lex.value();
			JsNode tnode = findNode(obj);
			JsNode prototypenode = tnode == null ? null : tnode.getChild("prototype");
			if (prototypenode != null) {
				if(prototypenode.getChild(sym)!=null)
				{
					node = new JsNode(null, prototypenode.getChild(sym).getNodeType(), sym, 0);
					cloneChildNode(prototypenode, node);
				}
			}
			break;
		case TokenType.ARRAY:
			node = getNodeByType("Array");
			advanceToken(']');
			getToken(); // skip ']'
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
			if (token == '(') {
				String symm = lex.value();
				functionCall(parent);
				// node = new JsNode(parent, "var", sym, lex.offset());
				// functionCall(node);
				// parent.addChild(node);
			} else {
				node = findNode(sym);
				if (node == null) {
					JsNode valnode = new JsNode(parent, EnumNode.VALUE, sym, lex.offset());
					parent.addChildNode(valnode);
				}
			}
			break;
		default:
			break;
		}

		while (token == '.') {
			getToken(); // skip '.'
			sym = lex.value();
			JsNode tnode = node.getChild(sym);

			if (tnode == null) {
				tnode = new JsNode(node, EnumNode.VALUE, sym, lex.offset());
				node.addChildNode(tnode);
				node = tnode;
				getToken(); // skip symbol

				if (token == '(') {
					// while(token != ')'){
					// getToken(); // skip ','
					// }
					functionCall(node);
				} else if (token == '[') {
					advanceToken(']');
					getToken(); // skip ']'
				}
			} else {
				getToken(); // skip symbol
				if (token == '(') {
					String type = tnode.getType();
					if (type != null) {
						if ("interfaces".equals(type)) {
							String param = null;
							if (token != ')') {
								getToken();
								while (token != ')') {
									getToken(); // skip ','
								}
								param = lex.value();
								node = findNode(param);
							}
						} else {
							node = getNodeByType(type);
						}
					} else {
						JsNode ttnode = new JsNode(tnode, EnumNode.VALUE, sym, lex.offset());
						node.addChildNode(ttnode);
						node = ttnode;
						getToken(); // skip symbol

						if (token == '(') {
							functionCall(node);
						} else if (token == '[') {
							advanceToken(']');
							getToken(); // skip ']'
						}
					}
				} else {
					if (token == '[') {
						advanceToken(']');
						getToken(); // skip ']'
					}
					node = tnode;
				}
			}
		}

		if (node != null && node.getType() != null) {
			String type = node.getType();
			if ("interfaces".equals(type)) {
				String param = null;
				if (token != ')') {
					while (token != ')') {
						getToken(); // skip ','
					}
					param = lex.value();
				}
				if (param != null) {
					node = findNode(param);
				}
			}
		}

		return node;
	}

	private void objectExpr(JsNode parent) throws EOSException {
		// fScopeStack.pushScope(new Scope(lex.offset(), parent));
		// getToken();
		while (token != '}') {
			if (token == TokenType.EOS) {
				break;
			}

			String sym = lex.value();
			getToken();
			switch (token) {
			case TokenType.JSDOC:
				fJsDoc = lex.value();
				break;

			case ':':
				getToken();
				if (token == TokenType.SYMBOL || token == TokenType.STRING) {
					JsNode node = new JsNode(parent, EnumNode.VALUE_PROP, sym, lex.offset());
					setJsDoc(node, fJsDoc);
					parent.addChildNode(node);
				} else if (token == TokenType.FUNCTION) {
					JsNode node = new JsNode(parent, EnumNode.FUNCTION_PROP, sym, lex.offset());
					setJsDoc(node, fJsDoc);
					parent.addChildNode(node);
					advanceToken('(');
					functionCall(node);
					advanceToken('}');
					getToken();
				} else if (token == '{') {
					JsNode node = new JsNode(parent, EnumNode.OBJECT, sym, lex.offset());
					parent.addChildNode(node);
					objectExpr(node);
				}
				break;
			}
		}
		if (token != '}' && token != TokenType.EOS) {
			getToken();
			// parent.setEndoffset(lex.offset()-1);
			// getToken();
		}
		parent.setEndoffset(lex.offset() - 1);
		// Scope scope = fScopeStack.popScope();
		// scope.setEnd(lex.offset());
	}
}
