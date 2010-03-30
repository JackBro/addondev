package org.addondev.ui.editor.javascript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;


import org.addondev.core.AddonDevPlugin;
import org.addondev.parser.javascript.JsNode;
import org.addondev.parser.javascript.Parser;
import org.addondev.parser.javascript.Scope;
import org.addondev.parser.javascript.ScopeManager;
import org.addondev.parser.javascript.util.JavaScriptParserManager;
import org.addondev.parser.javascript.util.XULJsMap;
import org.addondev.ui.template.JavaScriptTemplateCompletionProcessor;
import org.addondev.util.ChromeURLMap;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class JavaScriptContentAssistProcessor implements
		IContentAssistProcessor {

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IEditorPart editor = window.getActivePage().getActiveEditor();
		if (editor instanceof JavaScriptEditor) {
			JavaScriptEditor jsEditor = (JavaScriptEditor) editor;
			
			
//			//
//			XULJsMap xulmap = new XULJsMap();
//			if(jsEditor.getEditorInput() instanceof IFileEditorInput)
//			{
//				IFile file = ((IFileEditorInput)jsEditor.getEditorInput()).getFile();
//				List<String> jslist = xulmap.getList(file);
//				for (String js : jslist) {
//					System.out.println("jslist = " + js);
//				}	
//			}
			
			
			if(jsEditor.getEditorInput() instanceof IFileEditorInput)
			{
				IFile file = ((IFileEditorInput)jsEditor.getEditorInput()).getFile();
				IProject project = file.getProject();

			String src = jsEditor.getDocument().get();
		
			JavaScriptParserManager.instance().parse(project, file, src);
			
			ChromeURLMap p = AddonDevPlugin.getDefault().getChromeURLMap(project, false);
			String path = p.convertLocal2Chrome(file);
			
			//String path = file.getFullPath().toPortableString();
			//Parser parser = new Parser("test");
			//parser.parse(src);
			ScopeManager scopemanager = JavaScriptParserManager.instance().getScopeManager(project);
			Scope scope;
			if(offset == src.length())
			{
				//scope = ScopeManager.instance().getScope("test", 0);
				scope = scopemanager.getScope(path, 0);
			}
			else
			{
				//scope = ScopeManager.instance().getScope("test", offset);
				scope = scopemanager.getScope(path, offset);
			}

			String text = getAssistTarget(src, offset);
			JsNode tnode = scope.getNode();
			
			ArrayList<JsNode> compnodes = new ArrayList<JsNode>();
			
			if(text.length() > 0)
			{
				if(text.contains("."))
				{
					String[] ts = text.split("\\.");
					String tsf = ts[0];
					if("this".equals(tsf))
					{
						tnode = tnode.getParent();
					}
					else
					{
						tnode = tnode.getChild(tsf);
					}
					
					if(tnode == null)
					{
						//scope = ScopeManager.instance().getUpScopes("test", scope);
						
						ArrayList<Scope> scopes = new ArrayList<Scope>();
						//scopes.add(scope);
						//scopes.addAll(ScopeManager.instance().getUpScopes("test", scope));
						scopes.addAll(scopemanager.getUpScopes(path, scope));
						for (Scope scope2 : scopes) {
							if(scope2.getNode(tsf) != null)
							{
								//compnodes.addAll(Arrays.asList(scope2.getNode(tsf).getChildNodes()));
								tnode = scope2.getNode(tsf);
								break;
							}
						}
					}
					
					if(ts.length > 1)
					{
						int len = text.endsWith(".")?ts.length:ts.length-1;
						for (int i = 1; i < len; i++) {
							if(tnode == null) break;
							
							tnode = tnode.getChild(ts[i]);
						}
						if(text.endsWith("."))
						{
							compnodes.addAll(Arrays.asList(tnode.getChildNodes()));
						}
						else
						{
							for (JsNode node : tnode.getChildNodes()) {
								if(node.getName().startsWith(ts[ts.length]))
								{
									compnodes.add(node);
								}
							}
						}
					}
					else if(ts.length == 1)
					{
						compnodes.addAll(Arrays.asList(tnode.getChildNodes()));
					}
					//}
				}
				else
				{
					ArrayList<Scope> scopes = new ArrayList<Scope>();
					scopes.add(scope);
					//scopes.addAll(ScopeManager.instance().getUpScopes("test", scope));
					scopes.addAll(scopemanager.getUpScopes(path, scope));
					for (Scope scope2 : scopes) {
						for (JsNode node : scope2.getNode().getChildNodes()) {
							if(node.getName().startsWith(text))
							{
								compnodes.add(node);
							}
						}						
					}
				}
			}
			else
			{
				ArrayList<Scope> scopes = new ArrayList<Scope>();
				scopes.add(scope);
				//scopes.addAll(ScopeManager.instance().getUpScopes("test", scope));
				scopes.addAll(scopemanager.getUpScopes(path, scope));
				for (Scope scope2 : scopes) {
					compnodes.addAll(Arrays.asList(scope2.getNode().getChildNodes()));
				}
			}
			
			
			List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
				//JsNode[] chnodes = scope2.getNode().getChildNodes();
				for (JsNode node : compnodes) {
					result.add(
							new CompletionProposal(
									node.getName(), 
									offset, 
									0,
									node.getName().length(),
									null,
									node.getName(), 
									null,
									node.getfJsDoc()
							)			
					);
				}
//			JsNode[] chnodes =tnode.getChildNodes();//getChildNode();
//			for (JsNode node : chnodes) {
//				result.add(
//						new CompletionProposal(
//								node.getName(), 
//								offset, 
//								0,
//								node.getName().length(),
//								null,
//								node.getName(), 
//								null,
//								node.getfJsDoc()
//						)			
//				);
//			}
				return result.toArray(new ICompletionProposal[result.size()]);
			}
		}
		
		//addTemplateCompletionProposal(viewer, offset, result);
		
		return null;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void addTemplateCompletionProposal(ITextViewer viewer, int documentOffset,
			List<ICompletionProposal> result) {
		JavaScriptTemplateCompletionProcessor templateProcessor = new JavaScriptTemplateCompletionProcessor();
		ICompletionProposal[] templateProposals = templateProcessor.computeCompletionProposals(
				viewer, documentOffset);
		for (ICompletionProposal prop : templateProposals) {
			result.add(prop);
		}
	}
	
	private String getAssistTarget(String src, int offset)
	{
		StringBuffer buf = new StringBuffer();
		for (int i = offset-1; i >=0; i--) {
			char c = src.charAt(i);
			while((c == '\n') || (c == '\r'))
			{
				i--;
				c = src.charAt(i);
			}
			if(c == ')')
			{
				int n = skipr(src, i);
				i=n-1;
			}
			c = src.charAt(i);
			if (!Character.isJavaIdentifierPart((char) c) && c !='.') {	
				break;
			}
			buf.append(c);
			
		}
		//System.out.println("buf = " + buf.reverse());	
		String text = buf.reverse().toString();
//		if(text.endsWith("."))
//		{
//			text = text.substring(0, text.length()-1);
//		}
		return text;
	}

	private int skipr(String src, int offset)
	{
		Stack<String> rl = new Stack<String>();
		char c = src.charAt(offset);

		while( offset >0 )
		{
			if(c == '(')
			{
				rl.pop();
				if(rl.isEmpty())
					return offset;
			}
			else if(c == ')')
			{
				rl.push(")");
			}
			offset--;
			offset = skipsreing(src, offset);
			c = src.charAt(offset);
		}		
		
		return offset;
	}
	
	private int skipsreing(String src, int offset)
	{
		char c = src.charAt(offset);
		if(c == '"')
		{
			offset--;
		}
		else return offset;
		
		c = src.charAt(offset);

		while( offset >0 )
		{
			if(c == '"' && src.charAt(offset-1) != '\\')
			{
				return offset-1;
			}
			offset--;
			c = src.charAt(offset);	
		}			
		return offset;
	}
}
