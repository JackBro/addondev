package org.addondev.ui.wizard;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


import org.addondev.core.AddonDevNature;
import org.addondev.core.AddonDevPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class AddonDevImportWizardPage extends WizardPage {
	
	private DirectoryFieldEditor fDirectoryFieldEditor;
	private CheckboxTableViewer ctv;
	
	protected AddonDevImportWizardPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
		setPageComplete(false);
		
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Composite dfeparent = new Composite(composite, SWT.NONE);
		fDirectoryFieldEditor = new DirectoryFieldEditor("name", "labelText", dfeparent);
		dfeparent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		//fDirectoryFieldEditor.fillIntoGrid(composite, 1);
		fDirectoryFieldEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// TODO Auto-generated method stub
				String path = fDirectoryFieldEditor.getStringValue();
				if(path != null && path.length() > 0)
				{
					File dir = new File(path);
					if(dir.exists())
					{
						ctv.setInput(dir.listFiles());
					}
				}
				
			}
		});
		
		ctv = CheckboxTableViewer.newCheckList(composite, SWT.BORDER);
		ctv.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		ctv.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				// TODO Auto-generated method stub
				return (File[])inputElement;
			}
		});
		ctv.setLabelProvider(new ILabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String getText(Object element) {
				// TODO Auto-generated method stub
				File file = (File)element;
				return file.getName();
			}
			
			@Override
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
				if (element instanceof File){
					File file = (File)element;
					if(file.isDirectory())
					{
						imageKey = ISharedImages.IMG_OBJ_FOLDER;
					}
					else
					{
						imageKey = ISharedImages.IMG_OBJ_FILE;
					}
					return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
				}
				return null;
			}
		});
		
		setControl(composite);
		setPageComplete(true);
		String importdir = "";
		//String importdir = AddonDevPlugin.getDefault().getPreferenceStore().getString(AddonDevPlugin.IMPORT_DIR);
		//if(!new File(importdir).exists())
		//{
			importdir = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		//}
		fDirectoryFieldEditor.setStringValue(importdir);
	}
	
	public boolean finish() {
	
		final Object[] selected = ctv.getCheckedElements();
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				try {
					monitor.beginTask("", selected.length); //$NON-NLS-1$
					if (monitor.isCanceled()) {
						throw new OperationCanceledException();
					}
					for (int i = 0; i < selected.length; i++) {
						File file = (File) selected[i];
						//ZipFile zipFile = new ZipFile((File) selected[i]);
						//zipFile.getEntry("").
						Path path = new Path(file.getAbsolutePath());
						if("xpi".equals(path.getFileExtension()))
						{
							IProject project = null;
							try {
								project = createProject(file.getName(), monitor);
							} catch (OperationCanceledException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CoreException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								unZipxpi(file, project.getLocation().toFile());
							} catch (ZipException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
						{
							try {
								copyFiles(file,
										new SubProgressMonitor(monitor, 1));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CoreException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} finally {
					monitor.done();
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
		
		return true;
	}
	
	private void unZipxpi(File file, File distDir) throws ZipException, IOException
	{
		 ZipFile zipFile = new ZipFile(file);
		 Enumeration<? extends ZipEntry> entries = zipFile.entries();
		 while (entries.hasMoreElements()) {
			 ZipEntry ze = entries.nextElement();
			 
	            File outFile = new File(distDir.getAbsoluteFile(), ze.getName());
	            if (ze.isDirectory()) {
	                // ZipEntry がディレクトリの場合はディレクトリを作成。
	                outFile.mkdirs();
	            } else {
	            	
	            	 BufferedInputStream bis = null;
	                 BufferedOutputStream bos = null;
	            	
	                 try {	
	                	 
	                	 if (!outFile.getParentFile().exists()) {
	                         outFile.getParentFile().mkdirs();
	                     }
	                	 
	            	bis = new BufferedInputStream(zipFile.getInputStream(ze));
	            	bos = new BufferedOutputStream(new FileOutputStream(outFile));
	            	
	            	 int i = 0;
	                 while( ( i = bis.read() ) != -1 ) {
	                     bos.write( i );
	                 }
	                 }catch(IOException e)
	                 {
	                	 e.printStackTrace();
	                 }
	                finally
	                {
	                    try {
	                        if (bis != null) bis.close();
	                    } catch (IOException e) {
	                    	e.printStackTrace();
	                    }
	                    try {
	                        if (bos != null) bos.close();
	                    } catch (IOException e) {
	                    	e.printStackTrace();
	                    }	                	
	                }
	            }
		 }
		
	}
	
	private void copyFiles(File srcdir, IProgressMonitor monitor) throws IOException, CoreException
	{
        IProject project = createProject(srcdir.getName(), monitor);
		//.settings, .project
		FileFilter filter = new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				
				if(".project".equals(pathname.getName()))
					return false;
				
				if(pathname.isDirectory() && ".settings".equals(pathname.getName()))
					return false;
					
				return true;
			}
			
		};
		
		//FileUtils.copyDirectory(srcdir, project.getLocation().toFile(), filter, true);
		
		
//		IProjectDescription description = project.getDescription();
//		if(!project.hasNature(AddonDevPlugin.NATUREID))
//		{
//			addNature(AddonDevPlugin.NATUREID, description, project, monitor);			
//		}
//		String newNatureId = "org.eclipse.wst.jsdt.core.jsNature";
//		if(!project.hasNature(newNatureId))
//		{
//			addNature(newNatureId, description, project, monitor);
//		}
		
//		//File dir = new File("C:\\tmp");
//		String[] extensions = {"*"};
//		boolean recursive = true;
//		Collection files = FileUtils.listFiles(dir, null, recursive);
//		
//		File[] srcfile = FileUtils.convertFileCollectionToFileArray(files);
//		
//		for (File src : srcfile) {
//			
//			IFile file = project.getFile(path.append(src.getName()));
//
//			//file.create(new ByteArrayInputStream(text.getBytes("UTF-8")), true, monitor);
//			file.create(new FileInputStream(src), true, monitor);
//			//File dist = new File(.toOSString());
//			//FileUtils.copyFile(src, dist);
//		}
	}
	
	private IProject createProject(String projactname, IProgressMonitor monitor) throws CoreException, OperationCanceledException {
		
		IProject project = null;
		try {
			monitor.beginTask("", 2000);
			
			//ResourcesPlugin.getWorkspace().getRoot().
			//String name = distdir.getName();
			project = ResourcesPlugin.getWorkspace().getRoot().getProject(projactname);
//	        IPath defaultPath = Platform.getLocation();
//	        IPath newPath = projectPage.getLocationPath();
//	        if (defaultPath.equals(newPath)){
//	            newPath = null;
//	        }
	        IWorkspace workspace = ResourcesPlugin.getWorkspace();
	        IProjectDescription description = workspace.newProjectDescription(project.getName());
	        //description.setLocation(newPath);		
			
	//		String newNatureId = "org.eclipse.wst.jsdt.core.jsNature";
	//		if (!projectDescription.hasNature(newNatureId)) {
	//			String[] ids = projectDescription.getNatureIds();
	//			String[] newIds = new String[ids.length + 1];
	//			System.arraycopy(ids, 0, newIds, 0, ids.length);
	//			newIds[ids.length] = newNatureId;
	//			projectDescription.setNatureIds(newIds);
	//			project.setDescription(projectDescription, monitor);
	//		}			
	
			project.create(description, new SubProgressMonitor(monitor, 1000));
	
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
			//String newNatureId = "AddonDev.addondevnature";
			addNature(AddonDevNature.NATUREID, description, project, monitor);			
			String newNatureId = "org.eclipse.wst.jsdt.core.jsNature";
			addNature(newNatureId, description, project, monitor);

		} finally {
			monitor.done();
		}	
		
		return project;
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
