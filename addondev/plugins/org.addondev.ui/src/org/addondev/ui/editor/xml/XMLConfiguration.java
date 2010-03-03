package org.addondev.ui.editor.xml;

import org.addondev.preferences.ResourceManager;
import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.editor.PropertyChangeSourceViewerConfiguration;
import org.addondev.ui.editor.javascript.JavaScriptScanner;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;

//public class XMLConfiguration extends SourceViewerConfiguration {
public class XMLConfiguration extends PropertyChangeSourceViewerConfiguration {
	
	private IPreferenceStore fStore;
	//private ColorManager colorManager;
	private XMLDoubleClickStrategy doubleClickStrategy;
	
	private XMLTagScanner tagScanner;
	private XMLScanner scanner;
	private JavaScriptScanner cdataScanner;
	private RuleBasedScanner fCommentScanner;
	private Token fCommnetToken, fTagToken, fStringToken, fDefaulrToken;

	//public XMLConfiguration(ColorManager colorManager) {
	public XMLConfiguration() {
		super();
		//this.colorManager = colorManager;
		fStore = AddonDevUIPlugin.getDefault().getPreferenceStore();
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			XMLPartitionScanner.XML_COMMENT,
			XMLPartitionScanner.XML_TAG
			,XMLPartitionScanner.XML_CDATA
			};
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new XMLDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected XMLScanner getXMLScanner() {
		if (scanner == null) {
			scanner = new XMLScanner(fStore);
			fDefaulrToken = new Token(new TextAttribute(ResourceManager.getInstance().getColor(fStore, AddonDevUIPrefConst.COLOR_XML_FOREGROUND)));
			scanner.setDefaultReturnToken(fDefaulrToken);
		}
		return scanner;
	}
	protected XMLTagScanner getXMLTagScanner() {
		if (tagScanner == null) {
			tagScanner = new XMLTagScanner(fStore);
			fStringToken = tagScanner.getStringToken();
			fTagToken = new Token(new TextAttribute(ResourceManager.getInstance().getColor(fStore, AddonDevUIPrefConst.COLOR_XML_TAG)));
			tagScanner.setDefaultReturnToken(fTagToken);
		}
		return tagScanner;
	}
	
	
	protected RuleBasedScanner getXMLCommentScanner() {
		if (fCommentScanner == null) {
			fCommentScanner = new RuleBasedScanner();
			fCommnetToken = new Token(new TextAttribute(ResourceManager.getInstance().getColor(fStore, AddonDevUIPrefConst.COLOR_XML_COMMENT)));
			fCommentScanner.setDefaultReturnToken(fCommnetToken);
		}
		return fCommentScanner;
	}
	
	protected JavaScriptScanner getXMLCDATAScanner() {
		if (cdataScanner == null) {
			cdataScanner = new JavaScriptScanner(fStore);
			cdataScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
							ResourceManager.getInstance().getColor(fStore, AddonDevUIPrefConst.COLOR_JAVASCRIPT_FOREGROUND))));
		}
		return cdataScanner;
	}

	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		// TODO Auto-generated method stub
		return super.getReconciler(sourceViewer);
	}
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = null;
	
		dr = new DefaultDamagerRepairer(getXMLTagScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_TAG);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_TAG);
		
		dr = new DefaultDamagerRepairer(getXMLCommentScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_COMMENT);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_COMMENT);

		dr = new DefaultDamagerRepairer(getXMLCDATAScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_CDATA);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_CDATA);
		
		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}
	@Override
	public boolean update(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		if(event.getProperty().startsWith(AddonDevUIPrefConst.COLOR_XML_COMMENT))
		{
			setSyntax(event, fCommnetToken, AddonDevUIPrefConst.BOLD_SUFFIX, AddonDevUIPrefConst.ITALIC_SUFFIX);
			return true;
		}
//		else if(event.getProperty().startsWith(AddonDevUIPrefConst.COLOR_XML_KEYWORD))
//		{
//			Token token = ((JavaScriptScanner)defaultScanner).getKeywordToken();
//			setSyntax(event, token, AddonDevUIPrefConst.BOLD_SUFFIX, AddonDevUIPrefConst.ITALIC_SUFFIX);
//			return true;
//		}
		else if(event.getProperty().startsWith(AddonDevUIPrefConst.COLOR_XML_STRING))
		{
			setSyntax(event, fStringToken, AddonDevUIPrefConst.BOLD_SUFFIX, AddonDevUIPrefConst.ITALIC_SUFFIX);
			return true;
		}
		else if(event.getProperty().startsWith(AddonDevUIPrefConst.COLOR_XML_TAG))
		{
			setSyntax(event, fTagToken, AddonDevUIPrefConst.BOLD_SUFFIX, AddonDevUIPrefConst.ITALIC_SUFFIX);
			return true;
		}		
		return false;
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}