package org.addondev.rhino;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.ScriptOrFnNode;

public class JavascriptSyntaxCheck {

    static class SyntaxCheckContextFactory extends ContextFactory {
        @Override
        public boolean hasFeature(Context cx, int featureIndex) {
            if (featureIndex == Context.FEATURE_DYNAMIC_SCOPE)
                return true;
            return super.hasFeature(cx, featureIndex);
        }
    }
	
	private ContextFactory factory;
	// public static ShellContextFactory shellContextFactory = new
	// ShellContextFactory();
	static protected JavaScriptSyntaxErrorReporter errorReporter;

	public JavascriptSyntaxCheck()
	{
		factory = new SyntaxCheckContextFactory();
	}

	public void check(String sourceString) {
		errorReporter = new JavaScriptSyntaxErrorReporter();

		// shellContextFactory.setStrictMode(true);
		// shellContextFactory.setWarningAsError(true);
		// shellContextFactory.setErrorReporter(errorReporter);

		// Context cx = shellContextFactory.enterContext();

		Context cx = factory.enterContext();
		//Context cx = Context.enter();
		
		// cx.setLocale(Locale.getDefault());
		// cx.setOptimizationLevel(-1);

		CompilerEnvirons compilerEnv = new CompilerEnvirons();
		compilerEnv.initFromContext(cx);
		compilerEnv.setLanguageVersion(170);
		//compilerEnv.setAllowMemberExprAsFunctionName(false);
		// compilerEnv.setErrorReporter(errorReporter);
		// cx.setOptimizationLevel(-1);
		Parser p = null;
		ScriptOrFnNode root = null;

		long time1 = System.currentTimeMillis();
		try {
			p = new Parser(compilerEnv, errorReporter);
			root = p.parse(sourceString, "<cmd>", 1);
			
		} catch (EvaluatorException ee) {
			// TODO: handle exception
			System.out.println("EvaluatorException " + ee.lineNumber() + " : " + ee.columnNumber()
					+ " : " + ee.details());
		} catch (RhinoException rex) {
			System.out.println("RhinoException " + rex.details());
		} catch (VirtualMachineError ex) {
			System.out.println("VirtualMachineError " + ex.getMessage());
		}

		long time2 = System.currentTimeMillis();
		System.out.println("time = " + (time2 - time1));
	}

}
