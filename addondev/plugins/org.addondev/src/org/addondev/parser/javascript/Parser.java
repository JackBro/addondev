package org.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.Map;

import org.addondev.parser.javascript.util.JavaScriptParserManager;

public class Parser {
	private Lexer lex;
	private int token;
	private String fName;
	private String fJsDoc;

	public JsNode root;
	private static JsNode fTrueNode = new JsNode(null, EnumNode.VALUE, "true", -1);
	private static JsNode fFalseNode = new JsNode(null, EnumNode.VALUE, "false", -1);

	private ScopeStack fScopeStack = new ScopeStack();
	private ScopeManager fScopeManager;

	private void setJsDoc(JsNode node, String jsDoc) {
		if (fJsDoc != null && fJsDoc.length() > 0) {
			node.setfJsDoc(jsDoc);
		}
		fJsDoc = "";
	}

	private JsNode findNode(String name) {
		JsNode node = null;
		node = fScopeStack.getCurrntScope().getNode(name); // current
		// if (node == null) {
		// node = fScopeStack.getScope(0).getNode(name); // global this
		// }

		if (node == null) {
			// node = fScopeStack.getScope(name).getNode(name); // all this
			Scope scope = fScopeStack.getUpScope(fScopeStack.getCurrntScope(),
					name);
			node = scope.getNode(name);
		}

		if (node == null) {
			// node = ScopeManager.instance().getGlobalNode(name); // global
			// other
			node = fScopeManager.getGlobalNode(name); // global other
		}
		return node;
	}

	private JsNode findChildNode(JsNode node, String image) {
		if (node == null)
			return null;

		return node.getChild(image);
	}

	private JsNode getNodeByType(String type) {
		JsNode node = null;
		JsNode gnode = findNode(type);
		
		JsNode chNode = findChildNode(gnode, "prototype");
		if (chNode != null) {
			node = new JsNode(null, EnumNode.VALUE_PROP, lex.value(), 0);
			cloneChildNode(chNode, node);
		} else {
			node = gnode;
		}

		return node;
	}
	
	private void setNodeByType(String type, JsNode node) {
		JsNode gnode = findNode(type);
		if(gnode == null) return;
		
		JsNode chNode = findChildNode(gnode, "prototype");
		if (chNode != null) {
			cloneChildNode(chNode, node);
		} else {
			//node = gnode;
			assignChildNode(gnode, node);
		}
	}

	private void cloneChildNode(JsNode srcNode, JsNode distNode) {
		JsNode[] srcChildNodes = srcNode.getChildNodes();
		distNode.getSymbalTable().clear();
		for (JsNode node : srcChildNodes) {
			distNode.addChildNode(node.getClone(distNode));
		}
	}

	private void assignChildNode(JsNode srcNode, JsNode distNode) {
		distNode.setSymbalTable(srcNode.getSymbalTable());
	}

	private JsNode createNode(JsNode parent, EnumNode nodetype, String symbol, int offset)
	{
		JsNode node = new JsNode(parent, EnumNode.VALUE, symbol, lex.offset());
		return node;
	}
	
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

	public Parser(String name, ScopeManager scopemanager) {
		fName = name;
		fScopeManager = scopemanager;
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
		root = new JsNode(null, EnumNode.ROOT, "root", src.length());
		try {
			lex = new Lexer(src);
			fScopeStack.pushScope(scope);
			while (token != TokenType.EOS) {
				stmt(root);
				// getToken();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ScopeManager.instance().setScopeStack(fName, fScopeStack);
		fScopeManager.setScopeStack(fName, fScopeStack);
		return root;
	}

	private JsNode program() throws EOSException {
		root = new JsNode(null, EnumNode.ROOT, "root", 0);
		//fScopeStack.pushScope(new Scope(0, endoff, root));
		fScopeStack.pushScope(new Scope(0, root));
		int offset = lex.offset();
		while (token != TokenType.EOS) {
			stmt(root);
			// getToken();

			if (offset == lex.offset() && token != TokenType.EOS)
			//	break;
			 throw new EOSException();

			offset = lex.offset();
		}
		fScopeStack.getScope(0).setEnd(endoff);
		fScopeManager.setScopeStack(fName, fScopeStack);

		return root;
	}

	private void stmt(JsNode parent) throws EOSException {
		switch (token) {
		case '/':
			// int i = 0;
			lex.jsRex();
			// getToken();
			break;
		case TokenType.VAR:
		case TokenType.CONST:
		case TokenType.LET:
			def(parent);
			break;
		case TokenType.JSDOC:
			jsDoc_stmt();
			break;
		case TokenType.FUNCTION:
			fun(parent);
			break;
		case TokenType.TRY:
		case TokenType.FINALLY:
			advanceToken('{'); // skip try, finally
			block(parent);
			break;
		case TokenType.CATCH:
			advanceToken('{');
			block(parent);
			break;
		case TokenType.IF:
			if_stmt(parent);
			break;
		case TokenType.ELSE:
			elseif_stmt(parent);
			break;
		case TokenType.SYMBOL:
			getToken();
			if (token == '=') {
				String sym = lex.value();
				JsNode node = null;
				if ("this".equals(sym)
						&& parent.getNodeType() == EnumNode.FUNCTION_PROP) {
					node = parent.getParent();

				} else {
					node = findNode(sym);
				}

				if (node == null) {
					node = new JsNode(root, EnumNode.VALUE, sym, lex.offset());
					root.addChildNode(node);
				}

				getToken(); // skip '='
				JsNode res = factor(node);
				if (res != null) {
					assignChildNode(res, node);
				}
			} else if (token == '.') {
				String sym = lex.value();
				JsNode node = null;
				if ("this".equals(sym)
						&& parent.getNodeType() == EnumNode.FUNCTION_PROP) {
					node = parent.getParent();

				} else {
					node = findNode(sym);
				}

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
					if (token != TokenType.SYMBOL) {
						// lex.un(token);
						break;
					}
					String val = lex.value();
					JsNode m = node.getChild(val);
					if (m == null) {
						JsNode cnode = new JsNode(node, EnumNode.VALUE, val,
								lex.offset());
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
						assignChildNode(res, node);
					}
				} else if (token == '(') {
					// factor(node);
					node.setNodeType(EnumNode.FUNCTION);
					functionCall(node);
					// getToken(); // skip (
					// if (token != ')') {
				}
			}
			break;
		case '{':
			block(parent);
			break;
		default:
			getToken();
		}
	}

	private void elseif_stmt(JsNode parent) throws EOSException {
		getToken(); // skip 'else'

		if (token == TokenType.IF) {
			getToken(); // skip 'if'

			if (token == '{') {
				block(parent);
			} else {
				stmt(parent);
			}
		}

		if (token == TokenType.ELSE) {
			getToken(); // skip 'else'
			if (token == '{') {
				block(parent);
			} else {
				stmt(parent);
			}
		}
	}

	private void if_stmt(JsNode node) throws EOSException {
		getToken(); // skip 'if'

		getToken(); // skip '('
		factor(node);
		getToken(); // skip ')'

		if (token == '{') {
			block(node);
			// getToken();
		} else {
			stmt(node);
		}

		if (token == TokenType.ELSE) {
			getToken(); // skip 'else'
			if (token == '{') {
				block(node);
			} else {
				stmt(node);
			}
		}
	}

	private void functionCall(JsNode node) throws EOSException {
		//JsNode code = null;
		
		getToken(); // skip (
		if (token != ')') {

			//factor(node);
			factor(node, true);

			while (token != ')') {
				if (token == TokenType.EOS) {
					break;
				}
				getToken(); // skip ,
				//factor(node);
				factor(node, true);
			}

			getToken(); // skip ),
			if (token == '{') {
				fScopeStack.pushScope(new Scope(lex.offset(), node));
				block(node);
				Scope scope = fScopeStack.popScope();
				scope.setEnd(lex.offset() - 1);
			}
		} else {
			getToken(); // skip ),
			if (token == '{') {
				fScopeStack.pushScope(new Scope(lex.offset(), node));
				block(node);
				Scope scope = fScopeStack.popScope();
				scope.setEnd(lex.offset() - 1);
			}
		}
	}

	private void fun(JsNode parent) throws EOSException {
		JsNode code = null;
		getToken(); // skip function
		// char ch = (char)token;
		if (token == TokenType.SYMBOL) {
			String sym = lex.value();
			int offset = lex.offset();
			code = new JsNode(parent, EnumNode.FUNCTION, sym, offset);
			setJsDoc(code, fJsDoc);
			parent.addChildNode(code);
			getToken();
			advanceToken('(');
			functionCall(code);
		} else if (token == '(') // anonymous
		{
			int offset = lex.offset();
			code = new JsNode(parent, EnumNode.ANONYMOUS_FUNCTION, "anonymous"
					+ offset, offset);
			parent.addChildNode(code);

			// getToken();
			// advanceToken('(');
			functionCall(code);
		}
	}

	private void block(JsNode parent) throws EOSException {
		// TODO Auto-generated method stub
		getToken(); // skip {
		while (token != '}' && token != TokenType.EOS) {
			stmt(parent);
			// if(token == '}')
			// {
			// //getToken(); //skip }
			// //return;
			// break;
			// }
		}
		getToken(); // skip }
	}

	private void def(JsNode parent) throws EOSException {
		getToken(); // skip 'var'
		if (token != TokenType.SYMBOL) {
			// throw new Exception("文法エラーです。");
			// return;
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
		}
	}

	private JsNode factor(JsNode parent) throws EOSException {
		return factor(parent, false);
	}
	
	private JsNode factor(JsNode parent, Boolean isparam) throws EOSException {
		JsNode node = null;
		String sym = lex.value();
		switch (token) {
//		case TokenType.JSDOC:
//			jsDoc_stmt();
//			break;
		case '/':
			lex.jsRex();
			// getToken();
			break;
		case '{':
			parent.setOffset(lex.offset());
			parent.setNodeType(EnumNode.OBJECT);
			objectExpr(parent);
			break;
		case TokenType.FUNCTION:
			if(isparam)
			{
				advanceToken('(');
				int offset = lex.offset();
				JsNode code = new JsNode(parent, EnumNode.ANONYMOUS_FUNCTION, "anonymous"
						+ offset, offset);	
				parent.addChildNode(code);
				
				setJsDoc(parent, fJsDoc);
				functionCall(code);
			}
			else
			{
				advanceToken('(');
				setJsDoc(parent, fJsDoc);
				functionCall(parent);
			}
			break;
		case TokenType.NEW:
			getToken();
			String obj = lex.value();
			JsNode tnode = findNode(obj);
			JsNode prototypenode = tnode == null ? null : tnode.getChild("prototype");
			if (prototypenode != null) {
				// if(prototypenode.getChild(sym)!=null)
				// {
				// node = new JsNode(null,
				// prototypenode.getChild(sym).getNodeType(), sym, 0);
				// cloneChildNode(prototypenode, node);
				// }
				// node = new JsNode(null, EnumNode.OBJECT, "", 0);
				cloneChildNode(prototypenode, parent);
			}else{
				cloneChildNode(tnode, parent);
			}
			advanceToken(')');
			getToken(); // skip ')'
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
//				if(nodetype == EnumNode.PARAM)
//				{
//					//factor(parent);
//				}
//				else
//				{
					functionCall(parent);
//				}
			} else if (token == '[') {
				advanceToken(']');
				getToken(); // skip ']'

				node = findNode(sym);
				if (node == null) {
					node = new JsNode(parent, EnumNode.VALUE, sym, lex.offset());
					parent.addChildNode(node);
				}
				

			} else {
				node = findNode(sym);
				if (node == null) {
					// JsNode valnode = new JsNode(parent, EnumNode.VALUE, sym,
					// lex.offset());
					// parent.addChildNode(valnode);

					node = new JsNode(parent, EnumNode.VALUE, sym, lex.offset());
					parent.addChildNode(node);
					if(isparam)
					{
						String paramtype = parent.getParamType(sym);
						node.setReturnType(paramtype);
						if(paramtype != null)setNodeByType(paramtype, node);
					}
				}
				node.setParam(isparam);
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
					String type = tnode.getReturnType();
					if (type != null) {

						// if ("interfaces".equals(type)) {
						// String param = null;
						// if (token != ')') {
						// getToken();
						// while (token != ')') {
						// getToken(); // skip ','
						// }
						// param = lex.value();
						// node = findNode(param);
						// }
						Map<String, IFunction> funcmap = JavaScriptParserManager
								.instance().getFunctions();
						if (funcmap.containsKey(type)) {
							ArrayList<JsNode> args = new ArrayList<JsNode>();

							if (token != ')') {
								JsNode argnode = factor(null);
								if (argnode != null)
									args.add(argnode);
								while (token != ')') {
									getToken(); // skip ','
									argnode = factor(null);
									if (argnode != null)
										args.add(argnode);
								}
							}
							IFunction func = funcmap.get(type);
							node = func.Run(fScopeManager, fName, args);
							getToken(); // skip ')'
							
						} else {
							
							node = getNodeByType(type);
							advanceToken(')');
							getToken(); // skip ')'
						}
					} else {
						JsNode ttnode = new JsNode(tnode, EnumNode.VALUE, sym,
								lex.offset());
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

		if (node != null && node.getReturnType() != null) {
			String type = node.getReturnType();
			Map<String, IFunction> funcmap = JavaScriptParserManager.instance()
					.getFunctions();
			if (funcmap.containsKey(type)) {
				ArrayList<JsNode> args = new ArrayList<JsNode>();

				if (token != ')') {
					args.add(factor(null));
					while (token != ')') {
						getToken(); // skip ','
						args.add(factor(null));
					}
				}

				IFunction func = funcmap.get(type);
				node = func.Run(fScopeManager, fName, args);
			}
			else
			{
				//node = getNodeByType(type);		
			}
			// if ("interfaces".equals(type)) {
			// String param = null;
			// if (token != ')') {
			// while (token != ')') {
			// getToken(); // skip ','
			// }
			// param = lex.value();
			// }
			// if (param != null) {
			// node = findNode(param);
			// }
			// }
		}

		return node;
	}

	private void objectExpr(JsNode parent) throws EOSException {

		getToken(); // skip {
		while (token != '}') {
			if (token == TokenType.EOS) {
				break;
			}

			String sym = lex.value();
			// getToken();
			switch (token) {
			case TokenType.JSDOC:
				jsDoc_stmt();
				break;
			case TokenType.SYMBOL:
				// case ':':
				getToken();
				if (token == ':')
					getToken();
				
				if (token == TokenType.SYMBOL || token == TokenType.STRING || token == TokenType.INT) {
					JsNode node = new JsNode(parent, EnumNode.VALUE_PROP, sym,
							lex.offset());
					setJsDoc(node, fJsDoc);
					parent.addChildNode(node);
				} else if (token == TokenType.FUNCTION) {
					JsNode node = new JsNode(parent, EnumNode.FUNCTION_PROP,
							sym, lex.offset());
					setJsDoc(node, fJsDoc);
					parent.addChildNode(node);
					advanceToken('(');
					functionCall(node);
					// advanceToken('}');
					// getToken();
				} else if (token == '{') {
					JsNode node = new JsNode(parent, EnumNode.OBJECT, sym, lex
							.offset());
					parent.addChildNode(node);
					objectExpr(node);
				}

				break;

			default:
				getToken();
				break;
			}
		}
		// if (token != '}' && token != TokenType.EOS) {
		// getToken();
		// // parent.setEndoffset(lex.offset()-1);
		// // getToken();
		// }
		getToken(); // skip }
		parent.setEndoffset(lex.offset() - 1);
	}

	private void jsDoc_stmt() {
		fJsDoc = lex.value();
		getToken();
	}
	
//	private boolean is()
//	{
//		
//	}
}
