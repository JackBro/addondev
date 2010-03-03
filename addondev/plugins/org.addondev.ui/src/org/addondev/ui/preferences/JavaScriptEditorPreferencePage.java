package org.addondev.ui.preferences;


import java.io.IOException;
import java.io.InputStream;
import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.editor.PropertyChangeSourceViewerConfiguration;
import org.addondev.ui.editor.javascript.JavaScriptConfiguration;
import org.addondev.ui.editor.javascript.JavaScriptPartitionScanner;
import org.addondev.util.FileUtil;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.swt.graphics.RGB;
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
			InputStream in = getClass().getResourceAsStream("javascriptpreview.js");
			String text = "";
			try {
				text = FileUtil.getContent(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			IDocument document = new Document(text);
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new JavaScriptPartitionScanner(),
					new String[] {
						JavaScriptPartitionScanner.JS_COMMENT,
						JavaScriptPartitionScanner.JS_STRING});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
			return document;
		}

		@Override
		protected TextSourceViewerConfiguration getSourceViewerConfiguration() {
			// TODO Auto-generated method stub
			PropertyChangeSourceViewerConfiguration conf = new JavaScriptConfiguration();
			return (TextSourceViewerConfiguration)conf;
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

	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		PreferenceConverter.setValue(AddonDevUIPlugin.getDefault().getPreferenceStore(),
				AddonDevUIPrefConst.COLOR_JAVASCRIPT_KEYWORD, 
				new RGB(255, 0, 0));
		return super.performOk();
	}	
}
