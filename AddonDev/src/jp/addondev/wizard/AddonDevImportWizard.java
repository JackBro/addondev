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
		return mainPage.finish();
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
}
