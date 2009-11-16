package jp.addondev.wizard;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.WorkbenchPlugin;

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
