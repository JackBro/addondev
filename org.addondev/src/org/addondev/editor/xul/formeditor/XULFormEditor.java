package org.addondev.editor.xul.formeditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.addondev.plugin.AddonDevPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

public class XULFormEditor extends MultiPageEditorPart {

	private BrowserFormPage fBrowserFormPage;
	
	public XULFormEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setInput(IEditorInput input) {
		// TODO Auto-generated method stub
		super.setInput(input);
		
		if(input instanceof FileEditorInput)
		{
			FileEditorInput fileinput = (FileEditorInput)input;
			IProject project = fileinput.getFile().getProject();
			
		}
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
		fBrowserFormPage = new BrowserFormPage();
		fBrowserFormPage.createControl(getContainer());
		int pageIndex = addPage(fBrowserFormPage.getControl());
		setPageText(pageIndex, "Preview");
	}
	
	public void settest(String text)
	{
		File file = makeXULPreviewFile();
		fBrowserFormPage.setDocument(file, text);
		
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
	
//	public void setFile(IFile file)
//	{
//		
//		fBrowserFormPage.setFile(file);
//	}
	
//	public void setFile(File file, String xml)
//	{
//		fBrowserFormPage.setFile(file, xml);
//	}

	private void make()
	{

		URL entry = AddonDevPlugin.getDefault().getBundle().getEntry("/");
		String pluginDirectory;
		try {
			pluginDirectory = FileLocator.resolve(entry).getPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i=0;
		i++;

	}
}
