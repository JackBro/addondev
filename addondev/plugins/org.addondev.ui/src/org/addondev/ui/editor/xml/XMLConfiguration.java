package org.addondev.ui.editor.xml;

import org.addondev.preferences.ResourceManager;
import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.editor.javascript.JavaScriptPartitionScanner;
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
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

//public class XMLConfiguration extends SourceViewerConfiguration {
public class XMLConfiguration extends TextSourceViewerConfiguration {
	
	private IPreferenceStore fStore;
	
	private XMLDoubleClickStrategy doubleClickStrategy;
	private XMLTagScanner tagScanner;
	private XMLScanner scanner;
	private JavaScriptScanner cdataScanner;
	private ColorManager colorManager;
	
	private RuleBasedScanner fCommentScanner;
	private Token fCommnetToken;

	public XMLConfiguration(ColorManager colorManager) {
		super();
		this.colorManager = colorManager;
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
			scanner = new XMLScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IXMLColorConstants.DEFAULT))));
		}
		return scanner;
	}
	protected XMLTagScanner getXMLTagScanner() {
		if (tagScanner == null) {
			tagScanner = new XMLTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IXMLColorConstants.TAG))));
		}
		return tagScanner;
	}
	
	
	protected RuleBasedScanner getXMLCommentScanner() {
		if (fCommentScanner == null) {
			fCommentScanner = new RuleBasedScanner();
			//fCommentScanner = new JavaScriptScanner(fStore);
			//fCommentScanner = new XMLCommentScanner(colorManager);
			//fCommentScanner = new XMLCommentScanner(fStore);
			fCommentScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IXMLColorConstants.XML_COMMENT))));
		}
		return fCommentScanner;
	}
	
	protected JavaScriptScanner getXMLCDATAScanner() {
		if (cdataScanner == null) {
			
			cdataScanner = new JavaScriptScanner(fStore);
			cdataScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IXMLColorConstants.DEFAULT))));
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
		
//		if (fCommentScanner == null) {
//			//fCommnetToken = new Token(new TextAttribute(ResourceManager.getInstance().getColor(fStore, AddonDevUIPrefConst.COLOR_JAVASCRIPT_COMMENT)));
//			fCommnetToken = new Token(new TextAttribute(colorManager.getColor(IXMLColorConstants.XML_COMMENT)));
//			//fCommentScanner = new RuleBasedScanner();
//			fCommentScanner = new XMLCommentScanner(this.colorManager);
//			fCommentScanner.setDefaultReturnToken(fCommnetToken);
//		}		
//		dr = new DefaultDamagerRepairer(fCommentScanner);
		
		dr = new DefaultDamagerRepairer(getXMLCommentScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_COMMENT);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_COMMENT);

		dr = new DefaultDamagerRepairer(getXMLCDATAScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_CDATA);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_CDATA);
		
		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		
//		NonRuleBasedDamagerRepairer ndr =
//			new NonRuleBasedDamagerRepairer(
//				new TextAttribute(
//					colorManager.getColor(IXMLColorConstants.XML_COMMENT)));
//		reconciler.setDamager(ndr, XMLPartitionScanner.XML_COMMENT);
//		reconciler.setRepairer(ndr, XMLPartitionScanner.XML_COMMENT);	


		return reconciler;
	}

}