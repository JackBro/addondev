package jp.addondev.editor.javascript;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.ui.text.IColorManager;
import org.eclipse.wst.jsdt.ui.text.JavaScriptSourceViewerConfiguration;

//org.eclipse.wst.jsdt.ui.text.JavaScriptSourceViewerConfiguration
public class MyJavaScriptConfiguration extends
		JavaScriptSourceViewerConfiguration {
	private IColorManager fColorManager;
	private IPreferenceStore fPreferenceStore;

	public MyJavaScriptConfiguration(IColorManager colorManager,
			IPreferenceStore preferenceStore, ITextEditor editor,
			String partitioning) {
		super(colorManager, preferenceStore, editor, partitioning);
		// TODO Auto-generated constructor stub
		fColorManager = colorManager;
		fPreferenceStore = preferenceStore;
		
		
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		// TODO Auto-generated method stub
		ContentAssistant assistant = (ContentAssistant) super.getContentAssistant(sourceViewer);
		IContentAssistProcessor javaProcessor = new JavaScriptContentAssistProcessor();
		assistant.setContentAssistProcessor(javaProcessor, IDocument.DEFAULT_CONTENT_TYPE);
		return assistant;
		//return super.getContentAssistant(sourceViewer);
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		// TODO Auto-generated method stub
		PresentationReconciler reconciler = (PresentationReconciler) super
				.getPresentationReconciler(sourceViewer);
		// PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
				new JavaScriptCodeScanner(fColorManager, fPreferenceStore));

		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		// super.getPresentationReconciler(sourceViewer).
		return reconciler;

		// return super.getPresentationReconciler(sourceViewer);
	}

	// @Override
	// public ITextHover getTextHover(ISourceViewer sourceViewer,
	// String contentType) {
	// // TODO Auto-generated method stub
	// //return super.getTextHover(sourceViewer, contentType);
	// return new JavaScriptTextHover(sourceViewer, contentType);
	// }
	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType, int stateMask) {
		// TODO Auto-generated method stub
		// return super.getTextHover(sourceViewer, contentType, stateMask);
		return new JavaScriptTextHover(sourceViewer, contentType);
	}

}