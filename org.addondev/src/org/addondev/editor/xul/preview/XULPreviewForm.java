package org.addondev.editor.xul.preview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import jp.aonir.fuzzyxml.FuzzyXMLDocument;
import jp.aonir.fuzzyxml.FuzzyXMLElement;
import jp.aonir.fuzzyxml.FuzzyXMLNode;
import jp.aonir.fuzzyxml.FuzzyXMLParser;

import org.addondev.parser.xul.XULParser;
import org.addondev.plugin.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.addondev.util.FileUtil;
import org.addondev.util.ManifestUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import sun.misc.FpUtils;

public class XULPreviewForm extends MultiPageEditorPart {
//public class XULPreviewForm extends FormEditor {
	
	//setInput -> createPages
	private IPreferenceStore fStore;
	//private BrowserFormPage fBrowserFormPage;
	private XULPreviewPage fXULPreviewPage;
	
	
	
	@Override
	public String getPartName() {
		// TODO Auto-generated method stub
		if( getEditorInput() instanceof FileEditorInput)
		{
			FileEditorInput input = (FileEditorInput)getEditorInput();
			String n= input.getName();
			return input.getName();
		}
		return super.getPartName();
	}

	public XULPreviewForm() {
		// TODO Auto-generated constructor stub
		fStore = AddonDevPlugin.getDefault().getPreferenceStore();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
		//fBrowserFormPage.dispose();		
		super.dispose();

	}

	@Override
	protected void setInput(IEditorInput input) {
		// TODO Auto-generated method stub

		if(input instanceof FileEditorInput)
		{
			IPath path = new Path(fStore.getString(PrefConst.XULRUNNER_PATH));
			if(path.toFile().exists())
			{
				FileEditorInput fileinput = (FileEditorInput)input;
				IProject proj = fileinput.getFile().getProject();
				ManifestUtil util = new ManifestUtil();
				//String path = "D:/program/xulrunner";
				try {
					util.makePreviewManifestFile(proj, path, false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		super.setInput(input);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void createPages() {
		// TODO Auto-generated method stub
		File file = makeXULPreviewFile();
//		fBrowserFormPage = new BrowserFormPage(file);
//		fBrowserFormPage.createControl(getContainer());
//		int pageIndex = addPage(fBrowserFormPage.getControl());
//		setPageText(pageIndex, "Preview");

		fXULPreviewPage = new XULPreviewPage(file);
		fXULPreviewPage.createControl(getContainer());
		int pageIndex2 = addPage(fXULPreviewPage.getControl());
		setPageText(pageIndex2, "Preview2");
		
		FileEditorInput fileinput = (FileEditorInput)getEditorInput();
		String text = null;
		try {
			text = FileUtil.getContent(fileinput.getFile().getContents());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> list = XULParser.parse(text);
		fXULPreviewPage.setDocument(list);
	}
	
	public void setDocument(String text)
	{
		//ArrayList<String> list = XULParser.parse(text);
		//fBrowserFormPage.setDocument(text);
		fXULPreviewPage.setDocument(XULParser.parse(text));
	}
	
	private File makeXULPreviewFile()
	{
		String filename = "preview.xul";
		File file = null;
		URL entry = AddonDevPlugin.getDefault().getBundle().getEntry("/");
		try {
			String pluginDirectory = FileLocator.resolve(entry).getPath();
			file = new File(pluginDirectory, filename);
			if(!file.exists())
			{
				URL url = AddonDevPlugin.getDefault().getBundle().getEntry("files/preview.xul");
				InputStream in = url.openStream();
				StringBuffer buf = new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));

				String line = null;
				//String res = "";
				while ((line = reader.readLine()) != null) {
					buf.append(line + "\n");
				}

				in.close();
				reader.close();
				
				String text = buf.toString();
				file.createNewFile();
				FileWriter fw = new FileWriter(file);
				fw.write(text);
				fw.close();
				
				//FileUtils.writeStringToFile(file, text);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file;
	}
}
