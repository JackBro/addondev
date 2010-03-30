package org.addondev.ui.editor.javascript;

import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.editor.PropertyChangeSourceViewerConfiguration;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.RhinoException;

public class JavaScriptEditor extends TextEditor implements IPropertyChangeListener  {

	public static final String JAVASCRIPT_EDIT_CONTEXT = "#AddonDevJavascriptEditContext";
	public static final String ID = "org.addondev.ui.editor.javascript";
	
	private ContextFactory fContextFactory;

	static class SyntaxCheckContextFactory extends ContextFactory {
		@Override
		public boolean hasFeature(Context cx, int featureIndex) {
			if (featureIndex == Context.FEATURE_DYNAMIC_SCOPE)
				return true;
			return super.hasFeature(cx, featureIndex);
		}
	}

	private PropertyChangeSourceViewerConfiguration fSourceViewerConfiguration;
	
	public JavaScriptEditor() {
		super();
		// TODO Auto-generated constructor stub
		fSourceViewerConfiguration = new JavaScriptConfiguration();
		setSourceViewerConfiguration(fSourceViewerConfiguration);
		AddonDevUIPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
		
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		AddonDevUIPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
	}

	@Override
	protected void initializeEditor() {
		// TODO Auto-generated method stub
		super.initializeEditor();
		setEditorContextMenuId(JAVASCRIPT_EDIT_CONTEXT);
		// getSourceViewer().
	}

	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		// TODO Auto-generated method stub
		super.doSave(progressMonitor);

//		getSourceViewer().setRangeIndication(0, 10, true);
//		StyleRange range = new StyleRange();
//		range.start = 0;
//		range.length = 4;
//		Display  display = getSite().getShell().getDisplay();
//		org.eclipse.swt.graphics.Color blue = display.getSystemColor(SWT.COLOR_BLUE);
//        org.eclipse.swt.graphics.Color red = display.getSystemColor(SWT.COLOR_RED);
//
//		//range.foreground = blue;
//		range.background = red;
//		//range.fontStyle = SWT.BOLD;
//		getSourceViewer().getTextWidget().setStyleRange(range);
		//IDocumentProvider documentProvider= getDocumentProvider();
		//IAnnotationModel annotationModel= documentProvider.getAnnotationModel(getEditorInput());
		
		
		
//		IAnnotationModel annotationModel = getSourceViewer().getAnnotationModel();
//		String msg = "";
//		try {
//			msg = getDocument().get(0, 10);
//		} catch (BadLocationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Annotation an = new Annotation("org.addondev.ui.specification1", false, msg);
//
//		//an.
//		//AnnotationRulerColumn ran = new AnnotationRulerColumn(3);
//		//ran.toDocumentLineNumber(2);
//		Position position = new Position(0,10);
//		//am.addAnnotation(new ProjectionAnnotation(), position);
//		//annotationModel.addAnnotation(an, position);
//		
//		
//		Annotation an2 = new Annotation("org.addondev.ui.specification1", false, "msg");
//		Position position2 = new Position(32,5);
//		//am.addAnnotation(new ProjectionAnnotation(), position);
//		annotationModel.addAnnotation(an2, position2);
		//annotationModel.addAnnotation(new ProjectionAnnotation(), position);
		
		// JavaScriptValidate.parse(this);
		doValidate();

	}

	protected void doSetInput(IEditorInput input) throws CoreException {

		// super.doSetInput(input);

		if (input instanceof IFileEditorInput) {
			setDocumentProvider(new JavaScriptDocumentProvider());
			// super.doSetInput(input);
		} else if (input instanceof IStorageEditorInput) {
			setDocumentProvider(new JavaScriptDocumentProvider());
		}
		//getSourceViewer().invalidateTextPresentation()
		// else if(input instanceof SeqEditorInput){
		// //setDocumentProvider(new JavaScriptDocumentProvider());
		// setDocumentProvider(new JavaScriptDocumentProvider());
		// //getDocumentProvider().getDocument(null).set("test");
		// }
		// else if(input instanceof SeqStorageEditorInput)
		// {
		// setDocumentProvider(new JavaScriptDocumentProvider());
		// }
		super.doSetInput(input);
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return super.getAdapter(adapter);

	}

	public void setSelection(int offset, int length) {
		ISourceViewer sourceViewer = getSourceViewer();
		sourceViewer.setSelectedRange(offset, length);
		sourceViewer.revealRange(offset, length);
	}

	public IDocument getDocument() {
		return getSourceViewer().getDocument();
	}



	public void doValidate() {
		if(fContextFactory == null) fContextFactory = new SyntaxCheckContextFactory();
		
		IEditorInput in = getEditorInput();
		IResource resource = (IResource) in.getAdapter(IResource.class);
		try {
			resource.deleteMarkers(IMarker.PROBLEM, false, 0);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String src = getDocumentProvider().getDocument(getEditorInput()).get();
		JavaScriptParserErrorReporter errorReporter = new JavaScriptParserErrorReporter();
		errorReporter.setEditor(this);

		Context cx = fContextFactory.enterContext();
		// cx.setLocale(Locale.getDefault());
		// cx.setOptimizationLevel(-1);

		CompilerEnvirons compilerEnv = new CompilerEnvirons();
		compilerEnv.initFromContext(cx);
		compilerEnv.setLanguageVersion(170);

		Parser p = null;
		// ScriptOrFnNode root = null;
		// Script script = null;

		// long time1 = System.currentTimeMillis();
		try {
			p = new Parser(compilerEnv, errorReporter);
			p.parse(src, "javascript", 1);
		} catch (EvaluatorException ee) {
			// TODO: handle exception
			System.out.println(ee.lineNumber() + " : " + ee.columnNumber()
					+ " : " + ee.details());
		} catch (RhinoException rex) {
			System.out.println("RhinoException " + rex.details());
		} catch (VirtualMachineError ex) {
			System.out.println("VirtualMachineError " + ex.getMessage());
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		if(fSourceViewerConfiguration.update(event))
		{
			getSourceViewer().invalidateTextPresentation();
		}
	}
}
