package org.addondev.ui.editor.javascript;


import java.util.List;

import org.addondev.core.ExtensionLoader;
import org.addondev.preferences.ResourceManager;
import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.editor.PropertyChangeSourceViewerConfiguration;
import org.addondev.ui.editor.hover.IJavaScriptTextHover;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IAutoEditStrategy;
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
import org.eclipse.jface.util.PropertyChangeEvent;

public class JavaScriptConfiguration extends PropertyChangeSourceViewerConfiguration {
	private RuleBasedScanner defaultScanner;
	private RuleBasedScanner fCommentScanner;
	private RuleBasedScanner fStringScanner;
	
	private Token fDefaultToken;
	private Token fCommnetToken;
	private Token fStringToken;
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		// TODO Auto-generated method stub
		return new String[] {
				IDocument.DEFAULT_CONTENT_TYPE,
				JavaScriptPartitionScanner.JS_COMMENT,
				JavaScriptPartitionScanner.JS_STRING};
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		// TODO Auto-generated method stub
		IPreferenceStore store = AddonDevUIPlugin.getDefault().getPreferenceStore();
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr = null;
		
		if (defaultScanner == null) {
			fDefaultToken = new Token(new TextAttribute(ResourceManager.getInstance().getColor(store, AddonDevUIPrefConst.COLOR_JAVASCRIPT_FOREGROUND)));
			defaultScanner = new JavaScriptScanner(store);
			defaultScanner.setDefaultReturnToken(fDefaultToken);
		}
		
		dr = new DefaultDamagerRepairer(defaultScanner);
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
			
		if (fCommentScanner == null) {
			fCommnetToken = new Token(new TextAttribute(ResourceManager.getInstance().getColor(store, AddonDevUIPrefConst.COLOR_JAVASCRIPT_COMMENT)));
			fCommentScanner = new RuleBasedScanner();
			fCommentScanner.setDefaultReturnToken(fCommnetToken);
		}		
		DefaultDamagerRepairer commnetdr = new DefaultDamagerRepairer(fCommentScanner);
		reconciler.setDamager(commnetdr, JavaScriptPartitionScanner.JS_COMMENT);
		reconciler.setRepairer(commnetdr, JavaScriptPartitionScanner.JS_COMMENT);
		
		if (fStringScanner == null) {
			fStringToken = new Token(new TextAttribute(ResourceManager.getInstance().getColor(store, AddonDevUIPrefConst.COLOR_JAVASCRIPT_STRING)));
			fStringScanner = new RuleBasedScanner();
			fStringScanner.setDefaultReturnToken(fStringToken);
		}		
		DefaultDamagerRepairer stringdr = new DefaultDamagerRepairer(fStringScanner);
		reconciler.setDamager(stringdr, JavaScriptPartitionScanner.JS_STRING);
		reconciler.setRepairer(stringdr, JavaScriptPartitionScanner.JS_STRING);
		
		return reconciler;		
	}	

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		// TODO Auto-generated method stub
		//return super.getContentAssistant(sourceViewer);
		//ContentAssistant assistant = (ContentAssistant) super.getContentAssistant(sourceViewer);
		ContentAssistant assistant = new ContentAssistant();
		IContentAssistProcessor javaProcessor = new JavaScriptContentAssistProcessor();
		assistant.setContentAssistProcessor(javaProcessor, IDocument.DEFAULT_CONTENT_TYPE);
		assistant.install(sourceViewer);
		
		assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		
		return assistant;
	}

	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(
			ISourceViewer sourceViewer, String contentType) {
		// TODO Auto-generated method stub
		IAutoEditStrategy strategy = new JavaScriptAutoIndentStrategy();
		IAutoEditStrategy[] result = new IAutoEditStrategy[1];
		result[0] = strategy;
		
		return result;
	}

	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType) {
		// TODO Auto-generated method stub
		
		List<IJavaScriptTextHover> visitors = ExtensionLoader.getExtensions(AddonDevUIPlugin.EXTENSION_POINT_ID_JAVASCRIPTTEXTHOVER);
		for (IJavaScriptTextHover textHover : visitors) {
			if(textHover.isEnable(contentType))
			{
				return textHover;
			}
		}
		return super.getTextHover(sourceViewer, contentType);
	}
	
	
	
	public boolean update(PropertyChangeEvent event)
	{
		if(event.getProperty().startsWith(AddonDevUIPrefConst.COLOR_JAVASCRIPT_COMMENT))
		{
			setSyntax(event, fCommnetToken, AddonDevUIPrefConst.BOLD_SUFFIX, AddonDevUIPrefConst.ITALIC_SUFFIX);
			return true;
		}
		else if(event.getProperty().startsWith(AddonDevUIPrefConst.COLOR_JAVASCRIPT_KEYWORD))
		{
			Token token = ((JavaScriptScanner)defaultScanner).getKeywordToken();
			setSyntax(event, token, AddonDevUIPrefConst.BOLD_SUFFIX, AddonDevUIPrefConst.ITALIC_SUFFIX);
			return true;
		}
		else if(event.getProperty().startsWith(AddonDevUIPrefConst.COLOR_JAVASCRIPT_STRING))
		{
			setSyntax(event, fStringToken, AddonDevUIPrefConst.BOLD_SUFFIX, AddonDevUIPrefConst.ITALIC_SUFFIX);
			return true;
		}
		
		return false;
	}
	
	public void update()
	{
		
	}

}
