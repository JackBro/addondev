package jp.addondev.wizard;

import java.io.File;
import java.io.IOException;

import jp.addondev.AddonDevPlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.UIPlugin;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceImportPage1;

public class AddonDevImportWizard extends Wizard implements IImportWizard {

	//private IStructuredSelection selection;
	private AddonDevImportWizardPage mainPage;
	private DirectoryFieldEditor fDirectoryFieldEditor;
	private CheckboxTableViewer ctv;
	
	public AddonDevImportWizard() {
		// TODO Auto-generated constructor stub
        IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings
                .getSection("jp.addondev.wizard.addonimportwizard");//$NON-NLS-1$
        if (section == null) {
			section = workbenchSettings.addNewSection("jp.addondev.wizard.addonimportwizard");//$NON-NLS-1$
		}
        setDialogSettings(section);
//		//setWindowTitle("addon dev");
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPages();
		mainPage = new AddonDevImportWizardPage("workbench");
        addPage(mainPage);
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		//this.selection = selection;
		 setNeedsProgressMonitor(true);
		 
	}

	@Override
	public void createPageControls(Composite pageContainer) {
		// TODO Auto-generated method stub
		
	}
	
	private void createProject(IProjectDescription projectDescription,
			IProject project, IProgressMonitor monitor) throws CoreException,
			OperationCanceledException {
		try {
			monitor.beginTask("", 2000);
			
	//		String newNatureId = "org.eclipse.wst.jsdt.core.jsNature";
	//		if (!projectDescription.hasNature(newNatureId)) {
	//			String[] ids = projectDescription.getNatureIds();
	//			String[] newIds = new String[ids.length + 1];
	//			System.arraycopy(ids, 0, newIds, 0, ids.length);
	//			newIds[ids.length] = newNatureId;
	//			projectDescription.setNatureIds(newIds);
	//			project.setDescription(projectDescription, monitor);
	//		}			
	
			project.create(projectDescription, new SubProgressMonitor(monitor, 1000));
	
			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}
	
			project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 1000));
	
			//createFolder(project, monitor);

//			try {
//				createFile(project, "chrome/content/" + param.get("name") + ".js", AddonDevWizard.class.getResourceAsStream("addon.js"), param, monitor);
//				createFile(project, "install.rdf", AddonDevWizard.class.getResourceAsStream("install.rdf"), param, monitor);
//				createFile(project, "chrome.manifest", AddonDevWizard.class.getResourceAsStream("chrome.manifest"), param, monitor);
//				createFile(project, "overlay.xul", AddonDevWizard.class.getResourceAsStream("overlay.xul"), param, monitor);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			//http://yoichiro.cocolog-nifty.com/eclipse/2004/03/post_7.html
			//http://yoichiro.cocolog-nifty.com/eclipse/2004/03/post_6.html
			String newNatureId = "AddonDev.addondevnature";
			addNature(newNatureId, projectDescription, project, monitor);			
			newNatureId = "org.eclipse.wst.jsdt.core.jsNature";
			addNature(newNatureId, projectDescription, project, monitor);

		} finally {
			monitor.done();
		}		
	}
	
	private synchronized void addNature(String newNatureId, IProjectDescription projectDescription, IProject project, IProgressMonitor monitor)
	{
		//IProjectDescription pDescription = project.getDescription();
		if (!projectDescription.hasNature(newNatureId)) {
			String[] ids = projectDescription.getNatureIds();
			String[] newIds = new String[ids.length + 1];
			System.arraycopy(ids, 0, newIds, 0, ids.length);
			newIds[ids.length] = newNatureId;
			projectDescription.setNatureIds(newIds);
			try {
				project.setDescription(projectDescription, monitor);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	

}
