package org.addondev.ui.wizard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.addondev.core.AddonDevNature;
import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.addondev.util.FileUtil;
import org.addondev.util.Locale;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.xml.sax.SAXException;


public class AddonDevNewProjectWizard extends Wizard implements INewWizard {

	private WizardNewProjectCreationPage page1;
	private AddonDevNewProjectWizardPage page2;
	private AddonDevNewProjectWizardPage2 page3;
	//private HashMap<String, String> param;

	public AddonDevNewProjectWizard() {
		// TODO Auto-generated constructor stub
		setWindowTitle("addon dev");
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		// super.addPages();
		page1 = new WizardNewProjectCreationPage("page1");
		addPage(page1);

		page2 = new AddonDevNewProjectWizardPage("page2");
		page2.page1 = page1;
		addPage(page2);
		
		page3 = new AddonDevNewProjectWizardPage2("page3");
		page3.fNewProjectPage = page2;
		addPage(page3);
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		createNewProject();
		return true;
	}

	private void createNewProject() {
		// TODO Auto-generated method stub
		final IProject project = page1.getProjectHandle();	

		IPath newPath = null;
		if (!page1.useDefaults()) {
			newPath = page1.getLocationPath();
			newPath = newPath.append(project.getName());
		}

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription projectDescription = workspace
				.newProjectDescription(project.getName());
		projectDescription.setLocation(newPath);
		
		final Map<String, String> installparam = page2.getInstallParam();
		final List<String> filesparam = page2.getFilesParam();
		
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
					try {
						createProject(projectDescription, project, installparam, filesparam, monitor);
					} catch (OperationCanceledException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TemplateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		};

		try {
			getContainer().run(true, true, op);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private void createProject(IProjectDescription projectDescription,
			IProject project, Map<String, String> installparam, List<String> filesparam, IProgressMonitor monitor) throws CoreException,
			OperationCanceledException, BadLocationException, TemplateException, ParserConfigurationException, SAXException, IOException {
//		try {
//			monitor.beginTask("", 2000);
//			
//	//		String newNatureId = "org.eclipse.wst.jsdt.core.jsNature";
//	//		if (!projectDescription.hasNature(newNatureId)) {
//	//			String[] ids = projectDescription.getNatureIds();
//	//			String[] newIds = new String[ids.length + 1];
//	//			System.arraycopy(ids, 0, newIds, 0, ids.length);
//	//			newIds[ids.length] = newNatureId;
//	//			projectDescription.setNatureIds(newIds);
//	//			project.setDescription(projectDescription, monitor);
//	//		}			
//	
//			
//			
//			project.create(projectDescription, new SubProgressMonitor(monitor, 1000));
//	
//			if (monitor.isCanceled()) {
//				throw new OperationCanceledException();
//			}
//	
//			project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 1000));
//	
//			createFolder(project, monitor);
//	
//			try {
//				//createFile(project, "chrome/content/" + param.get("name") + ".js", AddonDevNewProjectWizard.class.getResourceAsStream("addon.js"), param, monitor);
//				//createFile(project, "install.rdf", AddonDevNewProjectWizard.class.getResourceAsStream("install.rdf"), param, monitor);
//				//createFile(project, "chrome.manifest", AddonDevNewProjectWizard.class.getResourceAsStream("chrome.manifest"), param, monitor);
//				//createFile(project, "overlay.xul", AddonDevNewProjectWizard.class.getResourceAsStream("overlay.xul"), param, monitor);
//				
//				createFile(project, "templates/project/addon.js", "chrome/content/" + param.get("name") + ".js", param, monitor);
//				createFile(project, "templates/project/common/install.rdf", "install.rdf", param, monitor);
//				//createFile(project, "templates/project/common/chrome.manifest", "chrome.manifest", param, monitor);
//				createManifestFile(project, "templates/project/common/chrome.manifest", "chrome.manifest", param, page2.getLocals(), monitor);
//				createFile(project, "templates/project/common/overlay.xul", "overlay.xul", param, monitor);
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			//http://yoichiro.cocolog-nifty.com/eclipse/2004/03/post_7.html
//			//http://yoichiro.cocolog-nifty.com/eclipse/2004/03/post_6.html
//			//String newNatureId = "AddonDev.addondevnature";
//			addNature(AddonDevNature.NATUREID, projectDescription, project, monitor);			
//			//newNatureId = "org.eclipse.wst.jsdt.core.jsNature";
//			//addNature(newNatureId, projectDescription, project, monitor);
//			project.setPersistentProperty(new QualifiedName(AddonDevUIPrefConst.LOCALE , "LOCALE"), page3.getDefaultLocale());
//			
//
//		} finally {
//			monitor.done();
//		}		
		
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


		CreateFireFoxTemplate ftmp = new CreateFireFoxTemplate();
		//ftmp.createPtoject(project, installparam, fileparam, optionparam, monitor);
		ftmp.createPtoject(project, installparam, filesparam, monitor);

		addNature(AddonDevNature.NATUREID, projectDescription, project, monitor);			

		//project.setPersistentProperty(new QualifiedName(AddonDevUIPrefConst.LOCALE , "LOCALE"), page3.getDefaultLocale());
		

	} finally {
		monitor.done();
	}		
	
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
	}
}

