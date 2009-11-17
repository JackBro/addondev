package jp.addondev.editor.javascript;

import jp.addondev.parser.javascript.JsNode;
import jp.addondev.parser.javascript.Lexer;
import jp.addondev.parser.javascript.NodeManager;
import jp.addondev.parser.javascript.Parser;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioningListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.wst.jsdt.ui.text.IJavaScriptPartitions;
import org.eclipse.wst.jsdt.ui.text.JavaScriptTextTools;


public class JavaScriptEditor extends TextEditor {
	private JavaScriptOutlinePage outlinePage = null;
	
	public JavaScriptEditor() {
		super();
		// TODO Auto-generated constructor stub
		
		JavaScriptTextTools textTools= JavaScriptPlugin.getDefault().getJavaTextTools();
		setSourceViewerConfiguration(new MyJavaScriptConfiguration(textTools.getColorManager(), 
				getPreferenceStore(), this, IJavaScriptPartitions.JAVA_PARTITIONING));
	}

//	@Override
//	public Object getAdapter(Class adapter) {
//		//TODO Auto-generated method stub
//		if (IContentOutlinePage.class.equals(adapter)) {
//			if(outlinePage == null) outlinePage = new JavaScriptOutlinePage(this);
//			return outlinePage;
//			
//			//return super.getAdapter(adapter);
//		}
//
//		return super.getAdapter(adapter);
//	}
	
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
