package org.addondev.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class NewJavaScriptFileWizard extends Wizard implements INewWizard {

	private NewJavaScriptFileWizardPage fNewPage;
	private IStructuredSelection fSelection;
	
	public NewJavaScriptFileWizard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		boolean res = false;

		String fileName = fNewPage.getFileName();
		IFile newfile = fNewPage.createNewFile();
		if(newfile != null)
		{
			try {
				createFile(newfile);
				res = true;
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				res = false;
			}
		}
		return res;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		fSelection = selection;
		
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		//super.addPages();
		fNewPage = new NewJavaScriptFileWizardPage("pageName", fSelection);
		addPage(fNewPage);
	}
	
	public void createFile(IFile file) throws PartInitException {
	    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	    IDE.openEditor(page, file, true, false);
	}
}
