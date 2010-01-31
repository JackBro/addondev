package org.addondev.editor.javascript;

import java.util.Locale;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.tools.shell.ShellContextFactory;

public class JavaScriptValidate {
	
	public static ShellContextFactory shellContextFactory = new ShellContextFactory();
	protected static JavaScriptParserErrorReporter errorReporter = new JavaScriptParserErrorReporter();

	public static void parse(JavaScriptEditor editor)
	{
		IEditorInput in = editor.getEditorInput();
		IResource resource = (IResource)in.getAdapter(IResource.class);
		try {
			resource.deleteMarkers(IMarker.PROBLEM, false, 0);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String src = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		errorReporter.setEditor(editor);
		
		//errorReporter = new JavaScriptParserErrorReporter();
		shellContextFactory.setStrictMode(true);
        //shellContextFactory.setWarningAsError(true);
	    shellContextFactory.setErrorReporter(errorReporter);
	        
	        Context cx =  shellContextFactory.enterContext();
	        
	        cx.setLocale(Locale.getDefault());
	        
	        CompilerEnvirons compilerEnv = new CompilerEnvirons();
	        compilerEnv.initFromContext(cx);	
	        compilerEnv.setLanguageVersion(170);

	        Parser p = null;
	        //ScriptOrFnNode root = null;
	        //Script script = null;

	        long time1 = System.currentTimeMillis();
			try {
				p = new Parser(compilerEnv, errorReporter);			
				p.parse(src, "javascript", 1);
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
	}
	
	
}
