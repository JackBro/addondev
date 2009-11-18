package jp.addondev.editor.javascript;

import jp.addondev.AddonDevPlugin;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
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
	Token token;
	
	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		// TODO Auto-generated method stub
		//return super.getPresentationReconciler(sourceViewer);
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr = null;
		
		if (defaultScanner == null) {
			 JFaceResources.getColorRegistry().put("test", new RGB(255, 0, 0));
			token = new Token(new TextAttribute(JFaceResources.getColorRegistry().get("test")));
			defaultScanner = new JavaScriptScanner(AddonDevPlugin.getDefault().getPreferenceStore());
			defaultScanner.setDefaultReturnToken(token);
		}
		
		dr = new DefaultDamagerRepairer(defaultScanner);
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
//		dr = new DefaultDamagerRepairer(getCommentScanner());
//		reconciler.setDamager(dr, JavaScriptPartitionScanner.JS_COMMENT);
//		reconciler.setRepairer(dr, JavaScriptPartitionScanner.JS_COMMENT);
		
		return reconciler;		
	}

}
