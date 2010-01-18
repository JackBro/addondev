package org.addondev.unittest.rhino;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptOrFnNode;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.ShellContextFactory;

import com.sun.xml.internal.ws.org.objectweb.asm.Type;

public class JavascriptParser {
  
	
	public static ShellContextFactory shellContextFactory = new ShellContextFactory();
	static protected JavaScriptParserErrorReporter errorReporter;
	public void test(String sourceString) throws FileNotFoundException
	{
		

//		Context cx = Context.enter();
//		//cx.setOptimizationLevel(-1);
//		
//        CompilerEnvirons compilerEnv = new CompilerEnvirons();
//        compilerEnv.initFromContext(cx);
//        
//        ErrorReporter compilationErrorReporter =
//        	compilerEnv.getErrorReporter();
//        
//        String[] secs = sourceString.split("\n");
//        int l = 0;
//        Parser p = new Parser(compilerEnv, compilationErrorReporter);
//        String e = null;
//        int line = 0;
//        while(true)
//        {
//        	e = null;
//			try {
//				ScriptOrFnNode root = p.parse(sourceString, "<cmd>", line+1);
//			} catch (EvaluatorException ee) {
//				// TODO: handle exception
//				System.out.println(ee.lineNumber() + " : " + ee.columnNumber() + " : " + ee.details());
//				String sss ="";
//				line = ee.lineNumber() + 1;
//				for (int i=line; i<secs.length; i++) {
//					sss += secs[i] + "\n";
//				}
//				sourceString = sss;
//				e = "er";
//			}
//			if(e == null) break;
//        }
		
		//ContextFactory cxfactory = new ContextFactory();
		//cxfactory.
		//Context cx =  cxfactory.enterContext();
	     //errorReporter = new ToolErrorReporter(false, global.getErr());
		errorReporter = new JavaScriptParserErrorReporter();
		
		shellContextFactory.setStrictMode(true);
        //shellContextFactory.setWarningAsError(true);
	        shellContextFactory.setErrorReporter(errorReporter);
	        

	        
	        Context cx =  shellContextFactory.enterContext();
	        
	        //cx.setLocale(Locale.getDefault());
	        //cx.setOptimizationLevel(-1);
	        
	        CompilerEnvirons compilerEnv = new CompilerEnvirons();
	        compilerEnv.initFromContext(cx);	
	        compilerEnv.setLanguageVersion(170);
	        //compilerEnv.setErrorReporter(errorReporter);
	        //cx.setOptimizationLevel(-1);
	        Parser p = null;
	        ScriptOrFnNode root = null;
	        Script script = null;
        //for (int i=line; i<secs.length; i++)
        //{
	        long time1 = System.currentTimeMillis();
			try {
				p = new Parser(compilerEnv, errorReporter);
				root = p.parse(sourceString, "<cmd>", 1);
				//Token.printTrees = true;
				//System.out.println(root.getFirstChild().toStringTree( root));
				//System.out.println(root.toString());
				//System.out.println(root.toStringTree(root));
				//dump(root);
			
				 System.out.println("Token.printTrees = " + Token.printTrees);
		        if (Token.printTrees) {
		            System.out.println(root.toStringTree(root));
		        }
				//root.getFirstChild().
				
				//cx.stringIsCompilableUnit(sourceString);
				//script = cx.compileString(sourceString, "test", 1, null);
				//script.exec(cx, new Global());
			} catch (EvaluatorException ee) {
				// TODO: handle exception
				System.out.println(ee.lineNumber() + " : " + ee.columnNumber() + " : " + ee.details());
			} catch (RhinoException rex) {
				System.out.println("RhinoException " + rex.details());
			} catch (VirtualMachineError ex) {
				System.out.println("VirtualMachineError " + ex.getMessage());
			}
				
			 long time2 = System.currentTimeMillis();
			 System.out.println("time = " + (time2-time1));
			int i=0;
			i++;
        //}
        
        
        
//        String source = "";
//        while (true) {
//            String newline;
//            try {
//                newline = in.readLine();
//            }
//            catch (IOException ioe) {
//                ps.println(ioe.toString());
//                break;
//            }
//            if (newline == null) {
//                hitEOF = true;
//                break;
//            }
//            source = source + newline + "\n";
//            lineno++;
//            if (cx.stringIsCompilableUnit(source))
//                break;
//            //ps.print(prompts[1]);
//        }
       
//        try {
//        	 //cx.stringIsCompilableUnit(sourceString);
//        	//InputStream in = rhinoTest.class.getResourceAsStream("test.js");
//        	//InputStreamReader inf = new InputStreamReader(in);
//        	//FileReader in = new FileReader("D:\\data\\src\\PDE\\work\\rhinoTest\\src\\org\\rhino\\test.js");
//        	Script script = cx.compileString(sourceString, "test", 1, null);
//        	//script.exec(cx, this);
//	    }
//	    catch (EvaluatorException ee) {
//	        System.out.println("js: " + ee.getMessage());
//	    }
//	    catch (JavaScriptException jse) {
//	        System.out.println("js: " + jse.getMessage());
//	    }        
//        //System.out.print(root.toStringTree(root));
//        Node node = root.getFirstChild();
//        do
//        {
//        	//System.out.print(node.getType() + node.getString() +  node.getLineno());
//        	System.out.println(node.getType() + " " +  node.getLineno());
//        	//node.getScope().getSymbolTable()
//        	
//        	if(node.getType() == Token.VAR)
//        	{
//        		//HashMap<String, Symbol> map = node.getScope().getSymbolTable();
//        		
//        		node.getScope().getDouble()
//        		System.out.println( node.getFirstChild().getString() );
//        	}
//        	//ScriptOrFnNode snode = (ScriptOrFnNode)node;
//        	//
//        	node = node.getNext();
//        }while(node != null);

        
//        Context.exit();
		
//        Context cx = Context.enter();
//        try {
//            Scriptable scope = cx.initStandardObjects();
//
//            // Add a global variable "out" that is a JavaScript reflection
//            // of System.out
//            Object jsOut = Context.javaToJS(System.out, scope);
//            ScriptableObject.putProperty(scope, "out", jsOut);
//
//           
//            Object result = cx.evaluateString(scope, sourceString, "<cmd>", 1, null);
//            System.err.println(Context.toString(result));
//        } finally {
//            Context.exit();
//        }
	}
	
	public void dump(Node node)
	{
		System.out.println(Type.getType(node.getClass()) );
		Node chnode = node.getFirstChild();
		
		System.out.println(chnode.getLineno() + " : " + chnode.getString());
		while(chnode != null)
		{
			chnode = chnode.getNext();
			if(chnode != null)
			{
				dump(chnode);
			}
		}
	}
}
