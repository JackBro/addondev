package org.addondev.editor.xul.formeditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.MultiPageEditorPart;

public class XULFormEditor extends MultiPageEditorPart {

	private BrowserFormPage fBrowserFormPage;
	
	public XULFormEditor() {
		// TODO Auto-generated constructor stub
	}

//	@Override
//	protected void addPages() {
//		// TODO Auto-generated method stub
//		try {
//			fBrowserFormPage = new BrowserFormPage(this, BrowserFormPage.ID, "title");
//			//fBrowserFormPage.createFormContent(managedForm)
//			addPage(fBrowserFormPage);
//		} catch (PartInitException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

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
		addPage(fBrowserFormPage.getControl());
	}
	
	public void settest(String text)
	{
		fBrowserFormPage.setDocument(text);
	}
	
	public void setFile(IFile file)
	{
		fBrowserFormPage.setFile(file);
	}

}
