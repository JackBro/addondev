package org.addondev.ui.template;

import java.util.ArrayList;
import java.util.List;

import org.addondev.ui.AddonDevUIPlugin;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.swt.graphics.Image;

public class JavaScriptTemplateCompletionProcessor extends
		TemplateCompletionProcessor {

	@Override
	protected TemplateContextType getContextType(ITextViewer viewer,
			IRegion region) {
		// TODO Auto-generated method stub
		//return null;
		return AddonDevUIPlugin.getDefault()
		.getContextTypeRegistry().getContextType(JavaScriptTemplateContextType.JAVASCRIPT_CONTEXT_TYPE);
	}

	@Override
	protected Image getImage(Template template) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Template[] getTemplates(String contextTypeId) {
		// TODO Auto-generated method stub
		//return null;
		return AddonDevUIPlugin.getDefault().getTemplateStore().getTemplates();
	}

//	@Override
//	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
//			int offset) {
//		// TODO Auto-generated method stub
//		ITextSelection selection= (ITextSelection) viewer.getSelectionProvider().getSelection();
//
//		// adjust offset to end of normalized selection
//		if (selection.getOffset() == offset)
//			offset= selection.getOffset() + selection.getLength();
//
//		String prefix= extractPrefix(viewer, offset);
//		Region region= new Region(offset - prefix.length(), prefix.length());
//		TemplateContext context= createContext(viewer, region);
//		Template[] templates= getTemplates("org.addondev.ui.template.javascript");//context.getContextType().getId());
//		//return super.computeCompletionProposals(viewer, offset);
//		
//		
//		List<ICompletionProposal> matches= new ArrayList<ICompletionProposal>();
//		for (int i= 0; i < templates.length; i++) {
//			Template template= templates[i];
//			try {
//				context.getContextType().validate(template.getPattern());
//			} catch (TemplateException e) {
//				continue;
//			}
//			if (template.getName().startsWith(prefix) && 
//					template.matches(prefix, context.getContextType().getId()))
//				matches.add(createProposal(template, context, (IRegion) region, getRelevance(template, prefix)));
//		}
//
//		return matches.toArray(new ICompletionProposal[matches.size()]);		
////		ICompletionProposal[] proposals = super.computeCompletionProposals(viewer, offset);		
////		String prefix = extractPrefix(viewer, offset);
////		List<ICompletionProposal> matches = new ArrayList<ICompletionProposal>();
////		for (ICompletionProposal p : proposals) {
////			if (p.getDisplayString().startsWith(prefix)) {
////				matches.add(p);
////			}
////		}
////		return (ICompletionProposal[]) matches.toArray(new ICompletionProposal[matches.size()]);
//	}
//
//	@Override
//	protected TemplateContext createContext(ITextViewer viewer, IRegion region) {
//		// TODO Auto-generated method stub
//		//return super.createContext(viewer, region);
//		TemplateContextType contextType = getContextType(viewer, region);
//		if (contextType != null) {
//			IDocument document = viewer.getDocument();
//			return new JavaScriptTemplateContext(contextType);
//		}
//		return null;
//	}

}
