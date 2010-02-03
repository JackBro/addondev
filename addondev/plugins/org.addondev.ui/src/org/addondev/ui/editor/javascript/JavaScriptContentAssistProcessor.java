package org.addondev.ui.editor.javascript;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


import org.addondev.core.AddonDevPlugin;
import org.addondev.parser.javascript.JsNode;
import org.addondev.parser.javascript.JsNodeHelper;
import org.addondev.parser.javascript.Lexer;
import org.addondev.parser.javascript.Parser;
import org.addondev.parser.javascript.Scope;
import org.addondev.parser.javascript.ScopeManager;
import org.addondev.ui.template.JavaScriptTemplateCompletionProcessor;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class JavaScriptContentAssistProcessor implements
		IContentAssistProcessor {

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		
		List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
		
		// ワークベンチの取得
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

		//アクティブなエディタの取得
		IEditorPart editor = window.getActivePage().getActiveEditor();
		if(editor instanceof JavaScriptEditor)
		{
			JavaScriptEditor jsEditor = (JavaScriptEditor) editor;
			String src = jsEditor.getDocument().get();
//			
//			Parser parser = new Parser("");
//			parser.parse(src);
//			Scope scope = ScopeManager.instance().getScope("", offset);
			
			String t = getAssistTarget(src, offset);
			System.out.println("Target = " + t);
		}


//		
//		
//		JsNode node2 = parser.root.getNodeFromOffset(offset);
//		JsNode node = JsNodeHelper.findChildNode(node2, t);
//		
////		JsNode valuenode = node.getValueNode();
////		List<CompletionProposal> result = new ArrayList<CompletionProposal>();
////		for (int i = 0; i < valuenode.getChildrenNum(); i++) {
////			result.add(
////			        new CompletionProposal(
////			        		valuenode.getChild(i).getImage(),
////			          offset,
////			          0,
////			          valuenode.getChild(i).getImage().length()));			
////		}
////
////	    ICompletionProposal[] proposals =
////	        new ICompletionProposal[result.size()];
////	      result.toArray(proposals);
////	      return proposals;
		addTemplateCompletionProposal(viewer, offset, result);
		
		return result.toArray(new ICompletionProposal[result.size()]);
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
		if(text.endsWith("."))
		{
			text = text.substring(0, text.length()-1);
		}
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
