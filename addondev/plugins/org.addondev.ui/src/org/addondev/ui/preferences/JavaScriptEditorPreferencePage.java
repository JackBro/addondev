package org.addondev.ui.preferences;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.addondev.core.AddonDevPlugin;
import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.editor.javascript.JavaScriptConfiguration;
import org.addondev.ui.editor.javascript.JavaScriptPartitionScanner;
import org.addondev.util.FileUtil;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class JavaScriptEditorPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	//private IPreferenceStore prefstote;
	public static final String JAVASCRIPT_COLOR_COMMENT = "org.addondev.preferences.javascript.color.comment";

	
	class JavascriptSyntaxColorPage extends SyntaxColorPage
	{
		private String[][] COLOR_STRINGS = new String[][] {
			{"keyword", AddonDevUIPrefConst.COLOR_JAVASCRIPT_KEYWORD}, 
			{"commnet", AddonDevUIPrefConst.COLOR_JAVASCRIPT_COMMENT},
			{"string", AddonDevUIPrefConst.COLOR_JAVASCRIPT_STRING}
		};
		
		@Override
		protected String[][] getColorStrings() {
			// TODO Auto-generated method stub
			return COLOR_STRINGS;
		}

		@Override
		protected IDocument getDocument() {
			// TODO Auto-generated method stub
			StringBuffer buffer = new StringBuffer();
			
			InputStream in = getClass().getResourceAsStream("javascriptpreview.js");
			String text = "";
			try {
				text = FileUtil.getContent(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			String delimiter = System.getProperty("line.separator"); //$NON-NLS-1$
//			buffer.append("function(){};"); //$NON-NLS-1$
//			buffer.append(delimiter);
//			buffer.append("//function(){};"); //$NON-NLS-1$
//			buffer.append(delimiter);
			buffer.append(text);
			IDocument document = new Document(buffer.toString());
			
			//IDocument document = super.createDocument(element);
			if (document != null) {
				IDocumentPartitioner partitioner =
					new FastPartitioner(
						new JavaScriptPartitionScanner(),
						new String[] {
							JavaScriptPartitionScanner.JS_COMMENT,
							JavaScriptPartitionScanner.JS_STRING});
				partitioner.connect(document);
				document.setDocumentPartitioner(partitioner);
			}
			return document;
		}

		@Override
		protected TextSourceViewerConfiguration getSourceViewerConfiguration() {
			// TODO Auto-generated method stub
			return new JavaScriptConfiguration();
		}
		
	}
	
	private JavascriptSyntaxColorPage fJavascriptSyntaxColorPage;
	
	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		fJavascriptSyntaxColorPage = new JavascriptSyntaxColorPage();
		fJavascriptSyntaxColorPage.createControl(parent);
		
		return parent;
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
	}

//	public JavaScriptEditorPreferencePage() {
//		// TODO Auto-generated constructor stub
//		setPreferenceStore(AddonDevUIPlugin.getDefault().getPreferenceStore());
//	}
//
//	public JavaScriptEditorPreferencePage(int style) {
//		super(style);
//		// TODO Auto-generated constructor stub
//	}
//
//	public JavaScriptEditorPreferencePage(String title, int style) {
//		super(title, style);
//		// TODO Auto-generated constructor stub
//	}
//
//	public JavaScriptEditorPreferencePage(String title,
//			ImageDescriptor image, int style) {
//		super(title, image, style);
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	protected void createFieldEditors() {
//		// TODO Auto-generated method stub
//		//prefstote = getPreferenceStore();
//		
//		Composite p = getFieldEditorParent();
//		addField(new ColorFieldEditor(
//				JAVASCRIPT_COLOR_COMMENT, "Timeout to connect to shell (secs).", p));
//	}
//
//	@Override
//	public void init(IWorkbench workbench) {
//		// TODO Auto-generated method stub
//
//	}

	
}
