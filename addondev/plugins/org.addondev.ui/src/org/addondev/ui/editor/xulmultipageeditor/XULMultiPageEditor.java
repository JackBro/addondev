package org.addondev.ui.editor.xulmultipageeditor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.editor.xul.XULEditor;
import org.addondev.ui.editor.xul.XULChecker;
import org.addondev.ui.editor.xul.preview.EnumXULWindow;
import org.addondev.ui.editor.xul.preview.XULParser;
import org.addondev.ui.editor.xul.preview.XULPreviewPage;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.addondev.util.FileUtil;
import org.addondev.util.Locale;
import org.addondev.util.ManifestUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;


public class XULMultiPageEditor extends MultiPageEditorPart {
	
	//setInput -> createPages
	private IPreferenceStore fStore;
	private XULEditor fXULEditor;
	private XULPreviewPage fXULPreviewPage;
	
	@Override
	public String getPartName() {
		// TODO Auto-generated method stub
		if( getEditorInput() instanceof FileEditorInput)
		{
			FileEditorInput input = (FileEditorInput)getEditorInput();
			return input.getName();
		}
		return super.getPartName();
	}

	public XULMultiPageEditor() {
		// TODO Auto-generated constructor stub
		fStore = AddonDevUIPlugin.getDefault().getPreferenceStore();
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
			IPath path = new Path(fStore.getString(AddonDevUIPrefConst.XULRUNNER_PATH));
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
		fXULEditor.doSave(monitor);
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		fXULEditor.doSaveAs();
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return (fXULEditor != null) && fXULEditor.isSaveAsAllowed();
	}

	@Override
	protected void createPages() {
		// TODO Auto-generated method stub
		
		try {
			fXULEditor = new XULEditor();
			int pageIndex1 = addPage(fXULEditor, getEditorInput());
			setPageText(pageIndex1, "Source");
		} catch (PartInitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		File file = makeXULPreviewFile();
		
		FileEditorInput fileinput = (FileEditorInput)getEditorInput();
		IProject project = fileinput.getFile().getProject(); 

		fXULPreviewPage = new XULPreviewPage(project, file);
		fXULPreviewPage.createControl(getContainer());
		int pageIndex2 = addPage(fXULPreviewPage.getControl());
		setPageText(pageIndex2, "Preview");
		
		
		String text = fXULEditor.getText();
//		ArrayList<String> list = XULParser.parse(project, locale, text);
//		fXULPreviewPage.setDocument(list);
		
		setDocument(fileinput);
	}
	
	private void setDocument(FileEditorInput input)
	{
		try {
			//list = checkLocale(fileinput);
			Locale locale = checkLocale(input);
			XULParser parser = new XULParser();
			parser.parse(input.getFile().getProject(), locale, input.getFile());
			List<String> list = parser.getXULs();
			EnumXULWindow windowtype = parser.getWindowType();
			fXULPreviewPage.setDocument(windowtype, list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
			IEditorSite editorSite = getEditorSite();
			IActionBars actionBars = editorSite.getActionBars();
			IStatusLineManager manager = actionBars.getStatusLineManager();
			manager.setErrorMessage("ERROR : " + e.getMessage());
		}
		
		//IProject project = ((FileEditorInput)getEditorInput()).getFile().getProject();
		//fXULPreviewPage.setDocument(XULParser.parse(project, locale, ((FileEditorInput)getEditorInput()).getFile()));
		//fXULPreviewPage.setDocument((FileEditorInput)getEditorInput());
	}
	
	public void refresh()
	{
		fXULPreviewPage.refresf();
		
	}
	
	public void reLoad()
	{
		IProject project = ((FileEditorInput)getEditorInput()).getFile().getProject();
		String text = fXULEditor.getText();
//		ArrayList<String> list = XULParser.parse(project, text);
//		fXULPreviewPage.setDocument(list);
		//setDocument(text);
		setDocument((FileEditorInput)getEditorInput());
	}
	
	private File makeXULPreviewFile()
	{
		String filename = "preview.xul";
		File file = null;
		URL entry = AddonDevUIPlugin.getDefault().getBundle().getEntry("/");
		try {
			String pluginDirectory = FileLocator.resolve(entry).getPath();
			file = new File(pluginDirectory, filename);
			if(!file.exists())
			{
				URL url = AddonDevUIPlugin.getDefault().getBundle().getEntry("files/preview.xul");
				InputStream in = url.openStream();	
				String text = FileUtil.getContent(in);
				FileUtil.write(file, text);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file;
	}
	
	public Locale checkLocale(FileEditorInput input) throws Exception
	{
		IProject project = input.getFile().getProject();
		String strlocale = null;
		Locale locale = null;
		List<String> xuls = null;
		try {
			strlocale = project.getPersistentProperty(new QualifiedName(AddonDevUIPrefConst.LOCALE , "LOCALE"));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(strlocale == null)
		{
			throw new Exception("not setting project locale");
		}

		locale = Locale.getLocale(strlocale);

		if(locale == null)
		{
			throw new Exception("not fild locale " + strlocale);
			//project
//			IStatus status = new Status(IStatus.ERROR, AddonDevUIPlugin.PLUGIN_ID, 
//					IStatus.OK, "メッセージ１", new Exception(
//			        "エラーメッセージ１"));
//			Shell shell = getSite().getWorkbenchWindow().getShell();
//			ErrorDialog.openError(shell, null, null, status);
		}
		
		return locale;
		//xuls = XULChecker.parse(project, locale, input.getFile());
		//return xuls;
	}
	
}
