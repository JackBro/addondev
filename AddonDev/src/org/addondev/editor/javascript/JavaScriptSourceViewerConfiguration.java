package org.addondev.editor.javascript;


import org.addondev.plugin.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.addondev.preferences.ResourceManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

public class JavaScriptSourceViewerConfiguration extends
		SourceViewerConfiguration {
	private RuleBasedScanner defaultScanner;
	private RuleBasedScanner fCommentScanner;
	
	private Token fDefaultToken;
	private Token fCommnetToken;
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		// TODO Auto-generated method stub
		//return super.getConfiguredContentTypes(sourceViewer);
		return new String[] {
				IDocument.DEFAULT_CONTENT_TYPE,
				JavaScriptPartitionScanner.JS_COMMENT};
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		// TODO Auto-generated method stub
		//return super.getPresentationReconciler(sourceViewer);
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr = null;
		
		if (defaultScanner == null) {
			fDefaultToken = new Token(new TextAttribute(ResourceManager.getInstance().getColor(PrefConst.COLOR_JAVASCRIPT_FOREGROUND)));
			defaultScanner = new JavaScriptScanner(AddonDevPlugin.getDefault().getPreferenceStore());
			defaultScanner.setDefaultReturnToken(fDefaultToken);
		}
		
		dr = new DefaultDamagerRepairer(defaultScanner);
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
			
		if (fCommentScanner == null) {
			fCommnetToken = new Token(new TextAttribute(ResourceManager.getInstance().getColor(PrefConst.COLOR_JAVASCRIPT_COMMENT)));
			fCommentScanner = new RuleBasedScanner();
			fCommentScanner.setDefaultReturnToken(fCommnetToken);
		}		
		dr = new DefaultDamagerRepairer(fCommentScanner);
		reconciler.setDamager(dr, JavaScriptPartitionScanner.JS_COMMENT);
		reconciler.setRepairer(dr, JavaScriptPartitionScanner.JS_COMMENT);
		
		return reconciler;		
	}
	
	
//	@Override
//	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
//		// TODO Auto-generated method stub
//		ContentAssistant assistant = (ContentAssistant) super.getContentAssistant(sourceViewer);
//		IContentAssistProcessor javaProcessor = new JavaScriptContentAssistProcessor();
//		assistant.setContentAssistProcessor(javaProcessor, IDocument.DEFAULT_CONTENT_TYPE);
//		return assistant;
//		//return super.getContentAssistant(sourceViewer);
//	}
//	
//	@Override
//	public ITextHover getTextHover(ISourceViewer sourceViewer,
//			String contentType, int stateMask) {
//		// TODO Auto-generated method stub
//		// return super.getTextHover(sourceViewer, contentType, stateMask);
//		return new JavaScriptTextHover(sourceViewer, contentType);
//	}

}
