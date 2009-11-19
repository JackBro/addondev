package jp.addondev.editor.javascript;

import java.util.ArrayList;
import java.util.List;

import jp.addondev.parser.javascript.JsNode;
import jp.addondev.parser.javascript.Lexer;
import jp.addondev.parser.javascript.NodeManager;
import jp.addondev.parser.javascript.Parser;
import jp.addondev.plugin.AddonDevPlugin;

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
}
//public class JavaScriptEditor extends TextEditor {
//	private JavaScriptOutlinePage outlinePage = null;
//	
//	private static class EclipsePreferencesAdapter implements IPreferenceStore {
//
//		/**
//		 * Preference change listener. Listens for events preferences
//		 * fires a {@link org.eclipse.jface.util.PropertyChangeEvent}
//		 * on this adapter with arguments from the received event.
//		 */
//		private class PreferenceChangeListener implements IEclipsePreferences.IPreferenceChangeListener {
//
//			/**
//			 * {@inheritDoc}
//			 */
//			public void preferenceChange(final IEclipsePreferences.PreferenceChangeEvent event) {
//				if (Display.getCurrent() == null) {
//					Display.getDefault().asyncExec(new Runnable() {
//						public void run() {
//							firePropertyChangeEvent(event.getKey(), event.getOldValue(), event.getNewValue());
//						}
//					});
//				} else {
//					firePropertyChangeEvent(event.getKey(), event.getOldValue(), event.getNewValue());
//				}
//			}
//		}
//
//		/** Listeners on on this adapter */
//		private ListenerList fListeners= new ListenerList(ListenerList.IDENTITY);
//
//		/** Listener on the node */
//		private IEclipsePreferences.IPreferenceChangeListener fListener= new PreferenceChangeListener();
//
//		/** wrapped node */
//		private final IScopeContext fContext;
//		private final String fQualifier;
//
//		/**
//		 * Initialize with the node to wrap
//		 *
//		 * @param context the context to access
//		 * @param qualifier the qualifier
//		 */
//		public EclipsePreferencesAdapter(IScopeContext context, String qualifier) {
//			fContext= context;
//			fQualifier= qualifier;
//		}
//
//		private IEclipsePreferences getNode() {
//			return fContext.getNode(fQualifier);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void addPropertyChangeListener(IPropertyChangeListener listener) {
//			if (fListeners.size() == 0)
//				getNode().addPreferenceChangeListener(fListener);
//			fListeners.add(listener);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void removePropertyChangeListener(IPropertyChangeListener listener) {
//			fListeners.remove(listener);
//			if (fListeners.size() == 0) {
//				getNode().removePreferenceChangeListener(fListener);
//			}
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public boolean contains(String name) {
//			return getNode().get(name, null) != null;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
//			PropertyChangeEvent event= new PropertyChangeEvent(this, name, oldValue, newValue);
//			Object[] listeners= fListeners.getListeners();
//			for (int i= 0; i < listeners.length; i++)
//				((IPropertyChangeListener) listeners[i]).propertyChange(event);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public boolean getBoolean(String name) {
//			return getNode().getBoolean(name, BOOLEAN_DEFAULT_DEFAULT);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public boolean getDefaultBoolean(String name) {
//			return BOOLEAN_DEFAULT_DEFAULT;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public double getDefaultDouble(String name) {
//			return DOUBLE_DEFAULT_DEFAULT;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public float getDefaultFloat(String name) {
//			return FLOAT_DEFAULT_DEFAULT;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public int getDefaultInt(String name) {
//			return INT_DEFAULT_DEFAULT;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public long getDefaultLong(String name) {
//			return LONG_DEFAULT_DEFAULT;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public String getDefaultString(String name) {
//			return STRING_DEFAULT_DEFAULT;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public double getDouble(String name) {
//			return getNode().getDouble(name, DOUBLE_DEFAULT_DEFAULT);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public float getFloat(String name) {
//			return getNode().getFloat(name, FLOAT_DEFAULT_DEFAULT);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public int getInt(String name) {
//			return getNode().getInt(name, INT_DEFAULT_DEFAULT);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public long getLong(String name) {
//			return getNode().getLong(name, LONG_DEFAULT_DEFAULT);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public String getString(String name) {
//			return getNode().get(name, STRING_DEFAULT_DEFAULT);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public boolean isDefault(String name) {
//			return false;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public boolean needsSaving() {
//			try {
//				return getNode().keys().length > 0;
//			} catch (BackingStoreException e) {
//				// ignore
//			}
//			return true;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void putValue(String name, String value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setDefault(String name, double value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setDefault(String name, float value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setDefault(String name, int value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setDefault(String name, long value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setDefault(String name, String defaultObject) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setDefault(String name, boolean value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setToDefault(String name) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setValue(String name, double value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setValue(String name, float value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setValue(String name, int value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setValue(String name, long value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setValue(String name, String value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setValue(String name, boolean value) {
//			throw new UnsupportedOperationException();
//		}
//
//	}
//	
//	public JavaScriptEditor() {
//		super();
//		
//
//		//setDocumentProvider(provider);
//		// TODO Auto-generated constructor stub
//		
////        IPreferenceStore store= createCombinedPreferenceStore(null);
////		setPreferenceStore(store);
////		IPreferenceStore store = getPreferenceStore();
////		JavaScriptTextTools textTools= JavaScriptPlugin.getDefault().getJavaTextTools();		
////		setSourceViewerConfiguration(new MyJavaScriptConfiguration(textTools.getColorManager(), 
////				store, this, IDocument.DEFAULT_CONTENT_TYPE));
//	}
//
//	@Override
//	protected void initializeEditor() {
//		// TODO Auto-generated method stub
//		super.initializeEditor();
//		
//		IPreferenceStore store= createCombinedPreferenceStore(null);
//		JavaScriptTextTools textTools= JavaScriptPlugin.getDefault().getJavaTextTools();
//		setSourceViewerConfiguration(
//				new MyJavaScriptConfiguration(textTools.getColorManager(), 
//						store, this, IDocument.DEFAULT_CONTENT_TYPE));	
//	}
//
//	@Override
//	protected void setPreferenceStore(IPreferenceStore store) {
//		// TODO Auto-generated method stub
//		super.setPreferenceStore(store);
//		
//		if (getSourceViewerConfiguration() instanceof MyJavaScriptConfiguration) {		
//			//if (getSourceViewerConfiguration() instanceof JavaScriptSourceViewerConfiguration
//			///		|| getSourceViewerConfiguration() instanceof MyJavaScriptConfiguration) {
//			JavaScriptTextTools textTools= JavaScriptPlugin.getDefault().getJavaTextTools();	
//			setSourceViewerConfiguration(
//					new MyJavaScriptConfiguration(textTools.getColorManager(), 
//							store, this, IDocument.DEFAULT_CONTENT_TYPE));	
//			}
//	}
//
//	private IPreferenceStore createCombinedPreferenceStore(IEditorInput input) {
//		List stores= new ArrayList(3);
//
//		IJavaScriptProject project= EditorUtility.getJavaProject(input);
//		if (project != null) {
//			stores.add(new EclipsePreferencesAdapter(new ProjectScope(project.getProject()), JavaScriptCore.PLUGIN_ID));
//		}
//
//		stores.add(JavaScriptPlugin.getDefault().getPreferenceStore());
//		stores.add(new PreferencesAdapter(JavaScriptCore.getPlugin().getPluginPreferences()));
//		stores.add(EditorsUI.getPreferenceStore());
//
//		return new ChainedPreferenceStore((IPreferenceStore[]) stores.toArray(new IPreferenceStore[stores.size()]));
//	}
	
	
//	private IPreferenceStore createCombinedPreferenceStore(IEditorInput input) {
//		List stores= new ArrayList(3);
//
//		IJavaScriptProject project= EditorUtility.getJavaProject(input);
//		if (project != null) {
//			//stores.add(new EclipsePreferencesAdapter(new ProjectScope(project.getProject()), JavaScriptCore.PLUGIN_ID));
//		}
//
//		stores.add(JavaScriptPlugin.getDefault().getPreferenceStore());
//		stores.add(new PreferencesAdapter(JavaScriptCore.getPlugin().getPluginPreferences()));
//		stores.add(EditorsUI.getPreferenceStore());
//
//		return new ChainedPreferenceStore((IPreferenceStore[]) stores.toArray(new IPreferenceStore[stores.size()]));
//	}
	
//	@Override
//	protected ISourceViewer createSourceViewer(Composite parent,
//			IVerticalRuler ruler, int styles) {
//		// TODO Auto-generated method stub
//		return new AdaptedSourceViewer(parent, verticalRuler, overviewRuler, isOverviewRulerVisible, styles, store);
//		//return super.createSourceViewer(parent, ruler, styles);
//	}

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
//	
//}



//public class JavaScriptEditor extends CompilationUnitEditor
//{
//	
//	private static class EclipsePreferencesAdapter implements IPreferenceStore {
//
//		/**
//		 * Preference change listener. Listens for events preferences
//		 * fires a {@link org.eclipse.jface.util.PropertyChangeEvent}
//		 * on this adapter with arguments from the received event.
//		 */
//		private class PreferenceChangeListener implements IEclipsePreferences.IPreferenceChangeListener {
//
//			/**
//			 * {@inheritDoc}
//			 */
//			public void preferenceChange(final IEclipsePreferences.PreferenceChangeEvent event) {
//				if (Display.getCurrent() == null) {
//					Display.getDefault().asyncExec(new Runnable() {
//						public void run() {
//							firePropertyChangeEvent(event.getKey(), event.getOldValue(), event.getNewValue());
//						}
//					});
//				} else {
//					firePropertyChangeEvent(event.getKey(), event.getOldValue(), event.getNewValue());
//				}
//			}
//		}
//
//		/** Listeners on on this adapter */
//		private ListenerList fListeners= new ListenerList(ListenerList.IDENTITY);
//
//		/** Listener on the node */
//		private IEclipsePreferences.IPreferenceChangeListener fListener= new PreferenceChangeListener();
//
//		/** wrapped node */
//		private final IScopeContext fContext;
//		private final String fQualifier;
//
//		/**
//		 * Initialize with the node to wrap
//		 *
//		 * @param context the context to access
//		 * @param qualifier the qualifier
//		 */
//		public EclipsePreferencesAdapter(IScopeContext context, String qualifier) {
//			fContext= context;
//			fQualifier= qualifier;
//		}
//
//		private IEclipsePreferences getNode() {
//			return fContext.getNode(fQualifier);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void addPropertyChangeListener(IPropertyChangeListener listener) {
//			if (fListeners.size() == 0)
//				getNode().addPreferenceChangeListener(fListener);
//			fListeners.add(listener);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void removePropertyChangeListener(IPropertyChangeListener listener) {
//			fListeners.remove(listener);
//			if (fListeners.size() == 0) {
//				getNode().removePreferenceChangeListener(fListener);
//			}
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public boolean contains(String name) {
//			return getNode().get(name, null) != null;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
//			PropertyChangeEvent event= new PropertyChangeEvent(this, name, oldValue, newValue);
//			Object[] listeners= fListeners.getListeners();
//			for (int i= 0; i < listeners.length; i++)
//				((IPropertyChangeListener) listeners[i]).propertyChange(event);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public boolean getBoolean(String name) {
//			return getNode().getBoolean(name, BOOLEAN_DEFAULT_DEFAULT);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public boolean getDefaultBoolean(String name) {
//			return BOOLEAN_DEFAULT_DEFAULT;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public double getDefaultDouble(String name) {
//			return DOUBLE_DEFAULT_DEFAULT;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public float getDefaultFloat(String name) {
//			return FLOAT_DEFAULT_DEFAULT;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public int getDefaultInt(String name) {
//			return INT_DEFAULT_DEFAULT;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public long getDefaultLong(String name) {
//			return LONG_DEFAULT_DEFAULT;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public String getDefaultString(String name) {
//			return STRING_DEFAULT_DEFAULT;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public double getDouble(String name) {
//			return getNode().getDouble(name, DOUBLE_DEFAULT_DEFAULT);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public float getFloat(String name) {
//			return getNode().getFloat(name, FLOAT_DEFAULT_DEFAULT);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public int getInt(String name) {
//			return getNode().getInt(name, INT_DEFAULT_DEFAULT);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public long getLong(String name) {
//			return getNode().getLong(name, LONG_DEFAULT_DEFAULT);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public String getString(String name) {
//			return getNode().get(name, STRING_DEFAULT_DEFAULT);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public boolean isDefault(String name) {
//			return false;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public boolean needsSaving() {
//			try {
//				return getNode().keys().length > 0;
//			} catch (BackingStoreException e) {
//				// ignore
//			}
//			return true;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void putValue(String name, String value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setDefault(String name, double value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setDefault(String name, float value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setDefault(String name, int value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setDefault(String name, long value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setDefault(String name, String defaultObject) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setDefault(String name, boolean value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setToDefault(String name) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setValue(String name, double value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setValue(String name, float value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setValue(String name, int value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setValue(String name, long value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setValue(String name, String value) {
//			throw new UnsupportedOperationException();
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		public void setValue(String name, boolean value) {
//			throw new UnsupportedOperationException();
//		}
//
//	}	
//	
//	public JavaScriptEditor() {
//		// TODO Auto-generated constructor stub
//		super();
//		IPreferenceStore store= createCombinedPreferenceStore(null);
//		JavaScriptTextTools textTools= JavaScriptPlugin.getDefault().getJavaTextTools();
//		setSourceViewerConfiguration(
//				new MyJavaScriptConfiguration(textTools.getColorManager(), 
//						store, this, IJavaScriptColorConstants.JAVA_DEFAULT));	
//	}
//
//
//
//	@Override
//	protected void initializeEditor() {
//		// TODO Auto-generated method stub
//		super.initializeEditor();
//		
//		//IJavaScriptPartitions.JAVA_PARTITIONING
//		IPreferenceStore store= createCombinedPreferenceStore(null);
//		JavaScriptTextTools textTools= JavaScriptPlugin.getDefault().getJavaTextTools();
//		setSourceViewerConfiguration(
//				new MyJavaScriptConfiguration(textTools.getColorManager(), 
//						store, this, IJavaScriptColorConstants.JAVA_DEFAULT));	
//	}
//
//
//
//	@Override
//	protected JavaScriptSourceViewerConfiguration createJavaSourceViewerConfiguration() {
//		// TODO Auto-generated method stub
//		//return super.createJavaSourceViewerConfiguration();
//		
//		JavaScriptTextTools textTools= JavaScriptPlugin.getDefault().getJavaTextTools();
//		
//		return new MyJavaScriptConfiguration(textTools.getColorManager(), 
//						getPreferenceStore(), this, IJavaScriptColorConstants.JAVA_DEFAULT);
//	}
//	
//	
//	
//	@Override
//	protected void setPreferenceStore(IPreferenceStore store) {
//		// TODO Auto-generated method stub
//		super.setPreferenceStore(store);
////		
////		if (getSourceViewerConfiguration() instanceof JavaScriptSourceViewerConfiguration) {
////			JavaScriptTextTools textTools= JavaScriptPlugin.getDefault().getJavaTextTools();
////			setSourceViewerConfiguration(new JavaScriptSourceViewerConfiguration(textTools.getColorManager(), store, this, IJavaScriptPartitions.JAVA_PARTITIONING));
////		}
//
//		if (getSourceViewerConfiguration() instanceof MyJavaScriptConfiguration) {		
//		//if (getSourceViewerConfiguration() instanceof JavaScriptSourceViewerConfiguration
//		///		|| getSourceViewerConfiguration() instanceof MyJavaScriptConfiguration) {
//		JavaScriptTextTools textTools= JavaScriptPlugin.getDefault().getJavaTextTools();	
//		setSourceViewerConfiguration(
//				new MyJavaScriptConfiguration(textTools.getColorManager(), 
//						store, this, IJavaScriptColorConstants.JAVA_DEFAULT));	
//		}
//		
////		 IDocument document = getSourceViewer().getDocument();
////		 document.addDocumentPartitioningListener(new IDocumentPartitioningListener() {
////
////			@Override
////			public void documentPartitioningChanged(IDocument document) {
////				// TODO Auto-generated method stub
////				if(document.containsPositionCategory(IDocument.DEFAULT_CONTENT_TYPE))
////				{
////					
////				}
////			}
////			 
////		 });
//		 
//			//if (getSourceViewer() instanceof JavaSourceViewer)
//			//	((JavaSourceViewer)getSourceViewer()).setPreferenceStore(store);
//	}
//	
//	private IPreferenceStore createCombinedPreferenceStore(IEditorInput input) {
//		List stores= new ArrayList(3);
//
//		IJavaScriptProject project= EditorUtility.getJavaProject(input);
//		if (project != null) {
//			stores.add(new EclipsePreferencesAdapter(new ProjectScope(project.getProject()), JavaScriptCore.PLUGIN_ID));
//		}
//
//		stores.add(JavaScriptPlugin.getDefault().getPreferenceStore());
//		stores.add(new PreferencesAdapter(JavaScriptCore.getPlugin().getPluginPreferences()));
//		stores.add(EditorsUI.getPreferenceStore());
//
//		return new ChainedPreferenceStore((IPreferenceStore[]) stores.toArray(new IPreferenceStore[stores.size()]));
//	}
//
//
//
//	@Override
//	protected void doSetInput(IEditorInput input) throws CoreException {
//		// TODO Auto-generated method stub
//		super.doSetInput(input);
//		
//	}
//	
//	
//}

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
