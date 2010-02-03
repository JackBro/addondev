package org.addondev.ui.editor.javascript;

import org.addondev.core.AddonDevPlugin;
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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

public class JavaScriptEditor extends TextEditor {
	
	public static final String JAVASCRIPT_EDIT_CONTEXT = "#AddonDevJavascriptEditContext";
	public static final String ID = "org.addondev.ui.editor.javascript";
	
	public JavaScriptEditor() {
		super();
		// TODO Auto-generated constructor stub		
		setSourceViewerConfiguration(new JavaScriptConfiguration());
	}

	@Override
	protected void initializeEditor() {
		// TODO Auto-generated method stub
		super.initializeEditor();
		setEditorContextMenuId(JAVASCRIPT_EDIT_CONTEXT);
		//getSourceViewer().
	}

	
	
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		// TODO Auto-generated method stub
		super.doSave(progressMonitor);
		
		//JavaScriptValidate.parse(this);
		
	}

	protected void doSetInput(IEditorInput input) throws CoreException {
		
		//super.doSetInput(input);
		
		if(input instanceof IFileEditorInput){
			setDocumentProvider(new JavaScriptDocumentProvider());
			//super.doSetInput(input);
		} else if(input instanceof IStorageEditorInput){
			setDocumentProvider(new JavaScriptDocumentProvider());
		} 
//		else if(input instanceof SeqEditorInput){
//			//setDocumentProvider(new JavaScriptDocumentProvider());
//			setDocumentProvider(new JavaScriptDocumentProvider());
//			//getDocumentProvider().getDocument(null).set("test");
//		}
//		else if(input instanceof SeqStorageEditorInput)
//		{
//			setDocumentProvider(new JavaScriptDocumentProvider());
//		}
		super.doSetInput(input);
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return super.getAdapter(adapter);
		
	}
	
	public void setSelection(int offset, int length)
	{
	    ISourceViewer sourceViewer = getSourceViewer();
	    sourceViewer.setSelectedRange(offset, length);
	    sourceViewer.revealRange(offset, length);		
	}
	
	public IDocument getDocument()
	{
		return getSourceViewer().getDocument();
	}
	
}
