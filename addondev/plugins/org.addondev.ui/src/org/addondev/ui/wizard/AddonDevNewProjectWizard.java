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
	public IWizardPage getNextPage(IWizardPage page) {
		// TODO Auto-generated method stub
		if(page instanceof AddonDevNewProjectWizardPage2)
		{

			page3.setLocals(page2.getLocals());
		}
		return super.getNextPage(page);
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

//		String name = project.getName();
//		String id = page2.getID();
//		String version = page2.getVersion();
//		String firefoxid = page2.getTargetApplicationID();
//		String minversion = page2.getMinVersion();
//		String maxversion = page2.getMaxVersion();
//		String description = page2.getDescription();
//		String creator = page2.getCreator();
//		String homepageurl = page2.geHomepageURL();
//
//		param = new HashMap<String, String>();
//		param.put("name", name);
//		param.put("id", id);
//		param.put("version", version);
//		param.put("appid", firefoxid);
//		param.put("minversion", minversion);
//		param.put("maxversion", maxversion);
//		param.put("description", description);
//		param.put("creator", creator);
//		param.put("homepageurl", homepageurl);		
		

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
		final Map<String, String> fileparam = page2.getFileParam();
		final Map<String, String> optionparam = page2.getOptionParam();
		
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				try {
					createProject(projectDescription, project, installparam, fileparam, optionparam, monitor);
				} catch (OperationCanceledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TemplateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				CreateFireFoxTemplate ftmp = new CreateFireFoxTemplate();
//				try {
//					ftmp.createPtoject(project, installparam, fileparam, optionparam, monitor);
//				} catch (BadLocationException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (TemplateException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
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
			IProject project, Map<String, String> installparam, Map<String, String> fileparam, Map<String, String> optionparam, IProgressMonitor monitor) throws CoreException,
			OperationCanceledException, BadLocationException, TemplateException {
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
		ftmp.createPtoject(project, installparam, fileparam, optionparam, monitor);		

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
	
	private void createFile(IProject project, String srcpath, String distpath,
			Map param, IProgressMonitor monitor) throws IOException, CoreException {
		
		URL url = AddonDevUIPlugin.getDefault().getBundle().getEntry(srcpath);
		//URL dirUrl = FileLocator.toFileURL(url);
		//File file = new File(dirUrl.getFile());
		//String tt = FileUtils.readFileToString(file);
		//FileInputStream in = new FileInputStream(file);
		
		InputStream in = url.openStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buf = new byte[1024 * 8];
		while((len = in.read(buf))!=-1){
			out.write(buf,0,len);
		}

		in.close();
		out.close();	
		
		String text = out.toString();
		for (Object obj : param.entrySet()) {
			if(obj instanceof String)
			{
				String key = (String) obj;
				text = text.replaceAll("\\$\\{" + key + "\\}", (String) param.get(key));
			}
		}
		
		IFile distfile = project.getFile(distpath);
		distfile.create(new ByteArrayInputStream(text.getBytes()), true, monitor);
	}
	
	private void createManifestFile(IProject project, String srcpath, String distpath,
			Map param, Locale[] locales, IProgressMonitor monitor) throws IOException, CoreException {
		URL url = AddonDevUIPlugin.getDefault().getBundle().getEntry(srcpath);
		
//		InputStream in = url.openStream();
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		int len = 0;
//		byte[] buf = new byte[1024 * 8];
//		while((len = in.read(buf))!=-1){
//			out.write(buf,0,len);
//		}
//
//		in.close();
//		out.close();	
//		String text = out.toString();	
		
		//locale	${name}	en-US	locale/en-US/
		
		String text = FileUtil.getContent(url.openStream());
		

		for (Object obj : param.entrySet()) {
			if(obj instanceof String)
			{
				String key = (String) obj;
				text = text.replaceAll("\\$\\{" + key + "\\}", (String) param.get(key));
			}
		}
		String name = (String) param.get("name");
		text += "\n";
		for (Locale locale : locales) {
			text += String.format("locale %s %s	locale/%s/", name, locale.getName(), locale.getName()) + "\n";
		}
		
		IFile distfile = project.getFile(distpath);
		distfile.create(new ByteArrayInputStream(text.getBytes()), true, monitor);		
	}
	
	private void createLocaleFile(IProject project, Map param, Locale[] locales, IProgressMonitor monitor) throws CoreException {
		String name = (String) param.get("name");
		for (Locale locale : locales) {
			IFile file;
			file = project.getFile(String.format("chrome/locale/%s/%s.dtd", locale.getName(), name));
			file.create(null, true, monitor);			
		}
	}
}

