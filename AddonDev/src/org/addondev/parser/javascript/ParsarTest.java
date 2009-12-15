package org.addondev.parser.javascript;
import java.util.HashMap;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import  org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.ScriptOrFnNode;
import org.mozilla.javascript.Token;

public class ParsarTest {

	public void test(String sourceString)
	{
		Context cx = Context.enter();
		
        CompilerEnvirons compilerEnv = new CompilerEnvirons();
        compilerEnv.initFromContext(cx);

        ErrorReporter compilationErrorReporter =
        	compilerEnv.getErrorReporter();
        Parser p = new Parser(compilerEnv, compilationErrorReporter);
        ScriptOrFnNode root = p.parse(sourceString, "<cmd>", 1);
        
        //System.out.print(root.toStringTree(root));
        Node node = root.getFirstChild();
        do
        {
        	//System.out.print(node.getType() + node.getString() +  node.getLineno());
        	System.out.println(node.getType() + " " +  node.getLineno());
        	//node.getScope().getSymbolTable()
        	
        	if(node.getType() == Token.VAR)
        	{
        		//HashMap<String, Symbol> map = node.getScope().getSymbolTable();
        		
        		node.getScope().getDouble()
        		System.out.println( node.getFirstChild().getString() );
        	}
        	//ScriptOrFnNode snode = (ScriptOrFnNode)node;
        	//
        	node = node.getNext();
        }while(node != null);

        
        Context.exit();
	}
}
