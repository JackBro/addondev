package org.addondev.editor.javascript;

import java.util.ArrayList;
import java.util.List;


import org.addondev.parser.javascript.JsNode;
import org.addondev.parser.javascript.Lexer;
import org.addondev.parser.javascript.NodeManager;
import org.addondev.parser.javascript.Parser;
import org.addondev.plugin.AddonDevPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioningListener;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.osgi.service.prefs.BackingStoreException;

public class JavaScriptEditor extends TextEditor {
	
	
	public JavaScriptEditor() {
		super();
		// TODO Auto-generated constructor stub		
		setSourceViewerConfiguration(new JavaScriptSourceViewerConfiguration());
	}

	protected void doSetInput(IEditorInput input) throws CoreException {
		if(input instanceof IFileEditorInput){
			setDocumentProvider(new JavaScriptDocumentProvider());
		} else if(input instanceof IStorageEditorInput){
			setDocumentProvider(new JavaScriptDocumentProvider());
		} else {
			setDocumentProvider(new JavaScriptDocumentProvider());
		}
		super.doSetInput(input);
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return super.getAdapter(adapter);
		
	}
	
}

//
//public class JavaScriptEditor extends CompilationUnitEditor {
//	
////http://dev.eclipse.org/viewcvs/index.cgi/sourceediting/plugins/?root=WebTools_Project
//
//
//	
//	private JavaScriptOutlinePage outlinePage = null;
//	
//	@Override
//	protected void updateStateDependentActions() {
//		// TODO Auto-generated method stub
//		super.updateStateDependentActions();
//	}
//
//	public JavaScriptEditor() {
//		// TODO Auto-generated constructor stub
//		super();
////		JavaScriptTextTools textTools= JavaScriptPlugin.getDefault().getJavaTextTools();		
////		setSourceViewerConfiguration(
////				new MyJavaScriptConfiguration(textTools.getColorManager(), 
////						getPreferenceStore(), this, IDocument.DEFAULT_CONTENT_TYPE));
//		
//	}
//
//@Override
//	protected void doSetInput(IEditorInput input) throws CoreException {
//		// TODO Auto-generated method stub
//		super.doSetInput(input);
//	}
//
//	//	@Override
////	protected void initializeEditor() {
////		// TODO Auto-generated method stub
////		super.initializeEditor();
////
////		JavaScriptTextTools textTools= JavaScriptPlugin.getDefault().getJavaTextTools();	
////		setSourceViewerConfiguration(
////				new MyJavaScriptConfiguration(textTools.getColorManager(), 
////						getPreferenceStore(), this, IDocument.DEFAULT_CONTENT_TYPE));	
////		
////		
////	}
////
//	@Override
//	protected void setPreferenceStore(IPreferenceStore store) {
//		// TODO Auto-generated method stub
//		super.setPreferenceStore(store);
//		
//		
//		JavaScriptTextTools textTools= JavaScriptPlugin.getDefault().getJavaTextTools();	
//		setSourceViewerConfiguration(
//				new MyJavaScriptConfiguration(textTools.getColorManager(), 
//						store, this, IJavaScriptPartitions.JAVA_PARTITIONING));	
//		
//		
//		
//		 IDocument document = getSourceViewer().getDocument();
//		 document.addDocumentPartitioningListener(new IDocumentPartitioningListener() {
//
//			@Override
//			public void documentPartitioningChanged(IDocument document) {
//				// TODO Auto-generated method stub
//				if(document.containsPositionCategory(IDocument.DEFAULT_CONTENT_TYPE))
//				{
//					
//				}
//			}
//			 
//		 });
//	}
//
//	@Override
//	public Object getAdapter(Class required) {
//		// TODO Auto-generated method stub
//
////		if (IContentOutlinePage.class.equals(required)) {
////			//if(outlinePage == null) outlinePage = new JavaScriptOutlinePage(this);
////			//return outlinePage;
////			
////			return super.getAdapter(required);
////		}
//		
//		return super.getAdapter(required);
//	}
//
//	public IPreferenceStore getJsPreferenceStore()
//	{
//		return getPreferenceStore();
//	}
//
//	@Override
//	public void doSave(IProgressMonitor progressMonitor) {
//		// TODO Auto-generated method stub
//		super.doSave(progressMonitor);
//
//		
//		IEditorInput input = getEditorInput();
//		if (input instanceof IFileEditorInput) {
//		    IFileEditorInput fileInput = (IFileEditorInput) input;
//		    IFile ifile = fileInput.getFile();
//
//		    // Type3. ファイルの絶対パスを取得する
//		    //ifile.getLocation().toString();
//
//		    // Type3. ファイルの(プロジェクトrootからの)相対パスを取得する
//		    String path = ifile.getFullPath().toString();
//		    
//		    String src = getSourceViewer().getDocument().get();
//		    Lexer lex = new Lexer(src);
//		    Parser parser = new Parser(); // パーサーを作成。
//			parser.parse(lex);
//			JsNode node = parser.root;
//			
//			NodeManager.getInstance().SetNode(path, node);		    
//		}
//				
//
//	}
//	
////	@Override
////	protected IJavaScriptElement getCorrespondingElement(IJavaScriptElement arg0) {
////		// TODO Auto-generated method stub
////		return null;
////		
////	}
////
////	@Override
////	protected IJavaScriptElement getElementAt(int offset) {
////		// TODO Auto-generated method stub
////		//return getElementAt(offset);
////
////		return null;
////	}
//
//}
