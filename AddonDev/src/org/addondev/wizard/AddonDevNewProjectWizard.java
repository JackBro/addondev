package org.addondev.wizard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.addondev.plugin.AddonDevPlugin;
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
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;


public class AddonDevNewProjectWizard extends Wizard implements INewWizard {

	private WizardNewProjectCreationPage page1;
	private AddonDevNewProjectWizardPage page2;
	private HashMap<String, String> param;

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

		String name = project.getName();
		String version = page2.getVersion();
		String firefoxid = page2.getTargetApplicationID();
		String minversion = page2.getMinVersion();
		String maxversion = page2.getMaxVersion();
		String description = page2.getDescription();
		String creator = page2.getCreator();
		String homepageurl = page2.geHomepageURL();

		param = new HashMap<String, String>();
		param.put("name", name);
		param.put("version", version);
		param.put("firefoxid", firefoxid);
		param.put("minversion", minversion);
		param.put("maxversion", maxversion);
		param.put("description", description);
		param.put("creator", creator);
		param.put("homepageurl", homepageurl);		
		

		IPath newPath = null;
		if (!page1.useDefaults()) {
			newPath = page1.getLocationPath();
			newPath = newPath.append(project.getName());
		}

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription projectDescription = workspace
				.newProjectDescription(project.getName());
		projectDescription.setLocation(newPath);
		

		// final String projectType = getProjectType();
		// define the operation to create a new project
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				createProject(projectDescription, project, monitor);
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
	
			createFolder(project, monitor);
	
			try {
				//AddonDevPlugin.getDefault().openStream(file)
				createFile(project, "chrome/content/" + param.get("name") + ".js", AddonDevNewProjectWizard.class.getResourceAsStream("addon.js"), param, monitor);
				createFile(project, "install.rdf", AddonDevNewProjectWizard.class.getResourceAsStream("install.rdf"), param, monitor);
				createFile(project, "chrome.manifest", AddonDevNewProjectWizard.class.getResourceAsStream("chrome.manifest"), param, monitor);
				createFile(project, "overlay.xul", AddonDevNewProjectWizard.class.getResourceAsStream("overlay.xul"), param, monitor);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//http://yoichiro.cocolog-nifty.com/eclipse/2004/03/post_7.html
			//http://yoichiro.cocolog-nifty.com/eclipse/2004/03/post_6.html
			//String newNatureId = "AddonDev.addondevnature";
			addNature(AddonDevPlugin.NATUREID, projectDescription, project, monitor);			
			//newNatureId = "org.eclipse.wst.jsdt.core.jsNature";
			//addNature(newNatureId, projectDescription, project, monitor);

		} finally {
			monitor.done();
		}		
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
	}

	private void createFolder(IProject project, IProgressMonitor monitor)
			throws CoreException {
		IFolder folder;

		folder = project.getFolder("chrome");
		folder.create(false, true, monitor);

		folder = project.getFolder("chrome/content");
		folder.create(false, true, monitor);
		
		folder = project.getFolder("defaults");
		folder.create(false, true, monitor);

		folder = project.getFolder("skin");
		folder.create(false, true, monitor);
		
		folder = project.getFolder("skin/classic");
		folder.create(false, true, monitor);
	}

	private void createFile(IProject project, String path, InputStream in,
			Map param, IProgressMonitor monitor) throws IOException {
		byte[] buf;
		String text = null;
		try {
			buf = new byte[in.available()];
			in.read(buf);
			text = new String(buf, "UTF-8");
			for (Iterator iterator = param.keySet().iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();
				text = text.replaceAll("\\$\\{" + key + "\\}", (String) param
						.get(key));
			}

			IFile file = project.getFile(path);
			file.create(new ByteArrayInputStream(text.getBytes("UTF-8")), true,
					monitor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			in.close();
		}
	}
}