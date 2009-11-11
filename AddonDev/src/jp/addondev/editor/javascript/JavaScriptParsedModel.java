package jp.addondev.editor.javascript;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
//import jp.addondev.parser.javascript.EcmaScript;
//import jp.addondev.parser.javascript.ParseException;
//import jp.addondev.parser.javascript.SimpleNode;

public class JavaScriptParsedModel {
	
	private IDocument document;
	//private EcmaScript parser;
	//private SimpleNode root;
	//private JavaScriptNode jsrootnode;
	
 	//private Map<String, JavaScriptNode> regnodeMap;
	
//	public JavaScriptParsedModel(JavaScriptEditor editor)
//	{
//
//	}
//	public JavaScriptNode getRoot(EcmaScript parser)
//	{
//		jsrootnode = new JavaScriptNode(null, "root", "root", 0);
//        SimpleNode srootnode = null;
//		try {
//			srootnode = parser.Program();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//		
//		for (int i = 0; i < srootnode.jjtGetNumChildren(); i++) {
//			makeJavaScriptNode(jsrootnode, srootnode.getChild(i));
//		}
//		
//		return jsrootnode;
//	}
//	//find node
//	public boolean isExistNode(JavaScriptNode node, String image)
//	{
//		boolean res = false;
//		if(image.contains("."))
//		{
//			String[] sp = image.split("\\.");
//			int n =0;
//			JavaScriptNode chnode = node;
//			boolean chflg = false;
//			while(n<sp.length)
//			{
//				chflg = false;
//				for (int i = 0; i < chnode.getChildrenNum(); i++) {
//					if(chnode.getChild(i).getImage().equals(sp[n]))
//					{
//						n++;
//						chnode = chnode.getChild(i);
//						chflg = true;
//						break;
//					}
//				}
//				if(!chflg)
//				{
//					res = false;
//					break;
//				}
//			}
//			if(n == sp.length)
//				res = true;
//		}
//		else
//		{
//			for (int i = 0; i < node.getChildrenNum(); i++) {
//				if(node.getChild(i).getImage().equals(image))
//				{
//					res = true;
//					break;
//				}
//			}			
//		}	
//		return res;
//	}
//	public JavaScriptNode getExpressionStatementNodeFromIdentifier(JavaScriptNode node, String image)
//	{
//		//boolean res = false;
//		if(image.contains("."))
//		{
//			String[] sp = image.split("\\.");
//			int n =0;
//			JavaScriptNode chnode = node;
//			boolean chflg = false;
//			while(n<sp.length)
//			{
//				chflg = false;
//				for (int i = 0; i < chnode.getChildrenNum(); i++) {
//					if(chnode.getChild(i).getImage().equals(sp[n]))
//					{
//						n++;
//						chnode = chnode.getChild(i);
//						chflg = true;
//						break;
//					}
//				}
//				if(!chflg)
//				{
//					//res = false;
//					break;
//				}
//			}
//			if(n == sp.length)
//			{
//				return chnode;
//			}
//			else
//			{
//				String nodename= null;
//				for (int i = n; i < sp.length; i++) {
//					
//					if(i==0) 
//						nodename = "ExpressionStatement";
//					else
//						nodename = "Property";
//					JavaScriptNode lnode = new JavaScriptNode(chnode, nodename, sp[i], 0);
//					chnode.addChild(lnode);	
//					chnode = lnode;
//				}
//				
//				return chnode;
//			}
//		}
////		else
////		{
////			JavaScriptNode lnode = new JavaScriptNode(node, "ExpressionStatement", image, 0);
////			node.addChild(lnode);
////			return lnode;
////			//return node;
////		}
//	
//		return null;
//	}
//	public JavaScriptNode findChildNodeFromIdent(JavaScriptNode node, String ident)
//	{
//		for (int i = 0; i < node.getChildrenNum(); i++) {
//			if(node.getChild(i).getImage().equals(ident))
//			{
//				return node.getChild(i);
//			}
//		}
//		
//		return null;
//	}
//	
//	private JavaScriptNode gettt(JavaScriptNode jsparent, SimpleNode sparent, String ident)
//	{
//		//String ident = SimpleNodeHelper.getIdentifierFromExpressionStatement(sparent);
//		JavaScriptNode tmpnode = null;
//		
//		if(ident.contains("."))
//		{
//			
//			String firstident = SimpleNodeHelper.getFirstIdentifierFromExpressionStatement(sparent);
//			tmpnode = getExpressionStatementNodeFromIdentifier(jsparent, firstident);
//			if(tmpnode != null)
//			{
//				tmpnode = getExpressionStatementNodeFromIdentifier(jsparent, ident);
//			}
//			else
//			{		
//				tmpnode = getExpressionStatementNodeFromIdentifier(jsrootnode, ident);
//				if(tmpnode != null)
//				{
//					tmpnode = getExpressionStatementNodeFromIdentifier(jsrootnode, ident);
//				}
//			}
//
//		}
////		else
////		{
////			JavaScriptNode lnode = new JavaScriptNode(jsparent, "ExpressionStatement", ident, 0);
////			jsparent.addChild(lnode);
////			return lnode;
////		}
//		
////		JavaScriptNode jsexpnode = null;
////		if(tmpnode !=null)
////		{
////			jsexpnode = tmpnode;
////		}
////		else
////		{
////			jsexpnode = new JavaScriptNode(jsparent, "ExpressionStatement", ident, sparent.getLine());
////			jsparent.addChild(jsexpnode);	
////		}
//		
//		return tmpnode;
//	}
//	
//	public JavaScriptNode makeJavaScriptNode(JavaScriptNode jsparent, SimpleNode sparent)
//	{
//		//with
//		if(sparent.getNodeName().equals("WithStatement"))
//		{
//			SimpleNode blocknode = SimpleNodeHelper.findChildNode(sparent, "Block");
//			if(blocknode != null && blocknode.jjtGetNumChildren() >0)
//			{
//				for (int i = 0; i < blocknode.jjtGetNumChildren(); i++) {
//					if(blocknode.getChild(i).getNodeName().equals("StatementList"))
//					{
//						SimpleNode StatementListnode = blocknode.getChild(i);
//						for (int j = 0; j < StatementListnode.jjtGetNumChildren(); j++) {
//								makeJavaScriptNode(jsparent, StatementListnode.getChild(j));						
//						}
//					}
//					else
//					{
//						makeJavaScriptNode(jsparent, blocknode.getChild(i));
//					}
//				}
//			 }
//		}
//		else if(sparent.getNodeName().equals("VariableStatement"))
//		{
//			SimpleNode[] varDecList = SimpleNodeHelper.getVariableDeclarationList(sparent);
//			for (int i = 0; i < varDecList.length; i++) {
//				
//				SimpleNode varindet = SimpleNodeHelper.getVariableIdentifier((SimpleNode)varDecList[i].jjtGetParent());	
//				JavaScriptNode jsvarnode = new JavaScriptNode(jsparent, "VariableStatement", varindet.getImage(), varindet.getLine());
//				jsparent.addChild(jsvarnode);
//				
//				SimpleNode objliteral = SimpleNodeHelper.getVariableObjectLiteral((SimpleNode)varDecList[i].jjtGetParent());
//				//SimpleNode tmpnode = vardec.getChild(1);
//				if(objliteral != null)
//				{
//					for (int j = 0; j < objliteral.jjtGetNumChildren(); j++) {
//						if(objliteral.getChild(j).getNodeName().equals("LiteralField"))
//						{
//							SimpleNode literaidentnode = SimpleNodeHelper.getLiteralFieldIdentifier(objliteral.getChild(j));
//							JavaScriptNode jsliteranode = new JavaScriptNode(jsvarnode, "VariableStatement", literaidentnode.getImage(), literaidentnode.getLine());
//							jsvarnode.addChild(jsliteranode);
//							
//							SimpleNode blocknode = SimpleNodeHelper.getLiteralFieldFunctionBlock(objliteral.getChild(j));
//							if(blocknode != null && blocknode.jjtGetNumChildren() >0)
//							{
//								for (int n = 0; n < blocknode.jjtGetNumChildren(); n++) {
//									makeJavaScriptNode(jsvarnode, blocknode.getChild(n));						
//								}
//							}
//						}
//					}
//				}
//				else
//				{
//					SimpleNode CompositeReferencenode = SimpleNodeHelper.getVariableCompositeReference((SimpleNode)varDecList[i].jjtGetParent());
//					if(CompositeReferencenode != null)
//					{
//						SimpleNode[] objliterals = SimpleNodeHelper.getObjectLiteralFromCompositeReference(CompositeReferencenode);
//						if(objliterals != null)
//						{
//							for (int n = 0; n < objliterals.length; n++) {
//								
//							for (int j = 0; j < objliterals[n].jjtGetNumChildren(); j++) {
//								SimpleNode litenode = objliterals[n].getChild(j);
//								if(litenode.getNodeName().equals("LiteralField"))
//								{
//									SimpleNode literaidentnode = SimpleNodeHelper.getLiteralFieldIdentifier(litenode);
//									JavaScriptNode jsliteranode = new JavaScriptNode(jsvarnode, "VariableStatement", literaidentnode.getImage(), literaidentnode.getLine());
//									jsvarnode.addChild(jsliteranode);
//									
//									SimpleNode blocknode = SimpleNodeHelper.getLiteralFieldFunctionBlock(litenode);
//									if(blocknode != null && blocknode.jjtGetNumChildren() >0)
//									{
//										for (int n1 = 0; n1 < blocknode.jjtGetNumChildren(); n1++) {
//											makeJavaScriptNode(jsvarnode, blocknode.getChild(n1));						
//										}
//									}
//								}
//							}
//							
//							}
//						}
//					}
//				}
//			}
//		}
//		else if(sparent.getNodeName().equals("FunctionDeclaration"))
//		{
//			SimpleNode identnode = SimpleNodeHelper.getFunctionDeclarationIdentifier(sparent);
//			JavaScriptNode jsfunctionnode = new JavaScriptNode(jsparent, "FunctionDeclaration", identnode.getImage(), identnode.getLine());
//			jsparent.addChild(jsfunctionnode);
//			
//			SimpleNode blocknode = SimpleNodeHelper.getFunctionDeclarationBlock(sparent);
//			if(blocknode != null && blocknode.jjtGetNumChildren() >0)
//			{
//				for (int j = 0; j < blocknode.jjtGetNumChildren(); j++) {
//					makeJavaScriptNode(jsfunctionnode, blocknode.getChild(j));						
//				}
//			}
//		}
//		else if(sparent.getNodeName().equals("ExpressionStatement"))
//		{
//			if(sparent.getImage().equals("("))
//			{
//				SimpleNode blocknode = SimpleNodeHelper.getAnonymousBlockFromExpressionStatement(sparent);
//				if(blocknode != null && blocknode.jjtGetNumChildren() >0)
//				{
//					JavaScriptNode anonymousnode = new JavaScriptNode(jsparent, "AnonymousFunction", "anonymous", sparent.getLine());
//					jsparent.addChild(anonymousnode);
//					
//					for (int n = 0; n < blocknode.jjtGetNumChildren(); n++) {
//						makeJavaScriptNode(anonymousnode, blocknode.getChild(n));						
//					}
//				}				
//				
//			}
//			else
//			{
//				SimpleNode assnode = SimpleNodeHelper.getAssignmentExpressionFromExpressionStatement(sparent);
//				if(assnode != null)
//				{
//					boolean localflag = false;
//					boolean globalflag = false;
//					boolean addglobalflag = false;
//					{
//						//String ident = SimpleNodeHelper.getIdentifierFromExpressionStatement(sparent);
//						String ident = SimpleNodeHelper.getFirstIdentifierFromExpressionStatement(sparent);
//						localflag = isExistNode(jsparent, ident);
//						globalflag = isExistNode(jsrootnode, ident);
//						if(!localflag && !globalflag)
//						{
//							//add global
//							addglobalflag = true;				
//						}
//					}
//					
//					SimpleNode objliteral = SimpleNodeHelper.findChildNode(assnode, "ObjectLiteral");		
//					if(objliteral != null)
//					{
//						String ident = SimpleNodeHelper.getIdentifierFromExpressionStatement(sparent);
//						
//						JavaScriptNode tmpnode = null;
//						String firstident = SimpleNodeHelper.getFirstIdentifierFromExpressionStatement(sparent);
//						tmpnode = getExpressionStatementNodeFromIdentifier(jsparent, firstident);
//						if(tmpnode != null)
//						{
//							tmpnode = getExpressionStatementNodeFromIdentifier(jsparent, ident);
//						}
//						else
//						{		
//							tmpnode = getExpressionStatementNodeFromIdentifier(jsrootnode, ident);
//							if(tmpnode != null)
//							{
//								tmpnode = getExpressionStatementNodeFromIdentifier(jsrootnode, ident);
//							}
//						}
//						
//						JavaScriptNode jsexpnode = null;
//						if(tmpnode !=null)
//						{
//							jsexpnode = tmpnode;
//						}
//						else
//						{
//							jsexpnode = new JavaScriptNode(jsparent, "ExpressionStatement", ident, sparent.getLine());
//							jsparent.addChild(jsexpnode);	
//						}		
//							
//						for (int i = 0; i < objliteral.jjtGetNumChildren(); i++) {
//							if(objliteral.getChild(i).getNodeName().equals("LiteralField"))
//							{
//								SimpleNode literaidentnode = SimpleNodeHelper.getLiteralFieldIdentifier(objliteral.getChild(i));
//								JavaScriptNode jsliteranode = new JavaScriptNode(jsexpnode, "VariableStatement", literaidentnode.getImage(), literaidentnode.getLine());
//								jsexpnode.addChild(jsliteranode);
//								
//								SimpleNode blocknode = SimpleNodeHelper.getLiteralFieldFunctionBlock(objliteral.getChild(i));
//								if(blocknode != null && blocknode.jjtGetNumChildren() >0)
//								{
//									for (int n = 0; n < blocknode.jjtGetNumChildren(); n++) {
//										//makeJavaScriptNode(jsexpnode, blocknode.getChild(n));	
//										makeJavaScriptNode(jsliteranode, blocknode.getChild(n));	
//									}
//								}
//							}
//						}
//					}
//					else
//					{
//						SimpleNode funcdecnode = SimpleNodeHelper.findChildNode(assnode, "FunctionDeclaration");
//						String ident = SimpleNodeHelper.getIdentifierFromExpressionStatement(sparent);
//						if(funcdecnode != null)
//						{
//							//JavaScriptNode jfunctionnode = new JavaScriptNode(jsparent, "Function", ident, sparent.getLine());
//							//jsparent.addChild(jfunctionnode);
//							JavaScriptNode jfunctionnode = null;
//							JavaScriptNode tmpnode = gettt(jsparent, sparent, ident);
//							if(tmpnode !=null)
//							{
//								jfunctionnode = tmpnode;
//							}
//							else
//							{
//								jfunctionnode = new JavaScriptNode(jsparent, "ExpressionStatement", ident, sparent.getLine());
//								jsparent.addChild(jfunctionnode);	
//							}	
//							
//							SimpleNode blocknode = SimpleNodeHelper.getFunctionDeclarationBlock(funcdecnode);
//							if(blocknode != null && blocknode.jjtGetNumChildren() >0)
//							{
//								for (int n = 0; n < blocknode.jjtGetNumChildren(); n++) {
//									makeJavaScriptNode(jfunctionnode, blocknode.getChild(n));						
//								}
//							}
//						}
//						else
//						{
//
//							if(addglobalflag)
//							{
//								//JavaScriptNode jexpnode = new JavaScriptNode(jsparent, "ExpressionStatement", ident, sparent.getLine());
//								JavaScriptNode jexpnode = null;
//								JavaScriptNode tmpnode = gettt(jsrootnode, sparent, ident);
//								//jsrootnode.addChild(jexpnode);	
//								if(tmpnode !=null)
//								{
//									jexpnode = tmpnode;
//								}
//								else
//								{
//									jexpnode = new JavaScriptNode(jsrootnode, "ExpressionStatement", ident, sparent.getLine());
//									jsrootnode.addChild(jexpnode);	
//								}	
//								SimpleNode[] objliterals = SimpleNodeHelper.getObjectLiteralFromCompositeReference(SimpleNodeHelper.getCompositeReferenceFromExpressionStatement(assnode));
//								if(objliterals != null)
//								{
//									JavaScriptNode anonymousnode = new JavaScriptNode(jexpnode, "AnonymousFunction", "anonymous", sparent.getLine());
//									jexpnode.addChild(anonymousnode);
//										for (int n = 0; n < objliterals.length; n++) {
//											
//										for (int j = 0; j < objliterals[n].jjtGetNumChildren(); j++) {
//											SimpleNode litenode = objliterals[n].getChild(j);
//											if(litenode.getNodeName().equals("LiteralField"))
//											{
//												SimpleNode literaidentnode = SimpleNodeHelper.getLiteralFieldIdentifier(litenode);
//												JavaScriptNode jsliteranode = new JavaScriptNode(anonymousnode, "VariableStatement", literaidentnode.getImage(), literaidentnode.getLine());
//												anonymousnode.addChild(jsliteranode);
//												
//												SimpleNode blocknode = SimpleNodeHelper.getLiteralFieldFunctionBlock(litenode);
//												if(blocknode != null && blocknode.jjtGetNumChildren() >0)
//												{
//													for (int n1 = 0; n1 < blocknode.jjtGetNumChildren(); n1++) {
//														makeJavaScriptNode(anonymousnode, blocknode.getChild(n1));						
//													}
//												}
//											}
//										}
//										
//										}								
//								}
//									
//							}
//							else
//							{
//								JavaScriptNode n = gettt(jsparent, sparent, ident);
//								
//							}
//						}
//					}
//				}
//				else
//				{
//					//SimpleNode CompositeReferencenode = SimpleNodeHelper.getVariableCompositeReference(sparent); 
//					if(sparent.jjtGetNumChildren() > 0)
//					{
//					SimpleNode CompositeReferencenode = sparent.getChild(0);
//					if(CompositeReferencenode.getNodeName().equals("CompositeReference"))
//					{
//						SimpleNode[] objliterals = SimpleNodeHelper.getObjectLiteralFromCompositeReference(CompositeReferencenode);
//						String ident = SimpleNodeHelper.getIdentifierFromExpressionStatement(sparent);
//						if(objliterals != null)
//						{
//							
//							JavaScriptNode tmpnode = gettt(jsparent, sparent, ident);
//							if(tmpnode !=null)
//							{
//								JavaScriptNode anoynode = new JavaScriptNode(jsparent, "AnonymousFunction", "Anonymous", sparent.getLine());
//								tmpnode.addChild(anoynode);
//								
//								for (int n = 0; n < objliterals.length; n++) {
//									
//									for (int j = 0; j < objliterals[n].jjtGetNumChildren(); j++) {
//										SimpleNode litenode = objliterals[n].getChild(j);
//										if(litenode.getNodeName().equals("LiteralField"))
//										{
//											SimpleNode literaidentnode = SimpleNodeHelper.getLiteralFieldIdentifier(litenode);
//											JavaScriptNode jsliteranode = new JavaScriptNode(anoynode, "VariableStatement", literaidentnode.getImage(), literaidentnode.getLine());
//											anoynode.addChild(jsliteranode);
//											
//											SimpleNode blocknode = SimpleNodeHelper.getLiteralFieldFunctionBlock(litenode);
//											if(blocknode != null && blocknode.jjtGetNumChildren() >0)
//											{
//												for (int n1 = 0; n1 < blocknode.jjtGetNumChildren(); n1++) {
//													makeJavaScriptNode(anoynode, blocknode.getChild(n1));						
//												}
//											}
//										}
//									}
//									
//									}
//							}
//						}
//						else
//						{
//							JavaScriptNode jfunctionnode = null;
//							JavaScriptNode tmpnode = gettt(jsparent, sparent, ident);
//							if(tmpnode !=null)
//							{
//								jfunctionnode = tmpnode;
//							}
//							else
//							{
//								jfunctionnode = new JavaScriptNode(jsparent, "ExpressionStatement", ident, sparent.getLine());
//								jsparent.addChild(jfunctionnode);	
//							}	
//							SimpleNode eee = SimpleNodeHelper.findChildNode(CompositeReferencenode, "FunctionCallParameters");
//							 if(eee != null && eee.jjtGetNumChildren() >0)
//							 {
//								SimpleNode blocknode = SimpleNodeHelper.getFunctionDeclarationBlock(eee.getChild(0));
//								if(blocknode != null && blocknode.jjtGetNumChildren() >0)
//								{
//									for (int j = 0; j < blocknode.jjtGetNumChildren(); j++) {
//										makeJavaScriptNode(jfunctionnode, blocknode.getChild(j));						
//									}
//								}
//							 }
//						}
//					}
//					}
////					String ident = SimpleNodeHelper.getIdentifierFromExpressionStatement(sparent);
////					if(!isExistNode(jsparent, ident))
////					{
////						JavaScriptNode jfunctionnode = new JavaScriptNode(jsparent, sparent.getNodeName(), ident, sparent.getLine());
////						jsrootnode.addChild(jfunctionnode);					
////					}
//				}
//			}
//		}
//		return jsparent;
//	}
	
	
	private class StringInputStream extends InputStream {
	    StringReader in;
	    private StringInputStream() {}

	    /** build input stream from given string.
	     * @param source input stream source
	     */
	    public StringInputStream(String source) {
	    	in = new StringReader(source);
	    }

	    public int read() throws IOException { return in.read(); }
	    
	    public void close() throws IOException { in.close(); }

	    public synchronized void mark(int readlimit) {
			try {
			    in.mark(readlimit);
			} catch(IOException e) {
			    throw new RuntimeException("IOException : StringInputStream["+
						       toString()+"]");
			}
	    }
	 
	    public synchronized void reset() throws IOException { in.reset(); }
	    public boolean markSupported() { return true; }
	}
}

