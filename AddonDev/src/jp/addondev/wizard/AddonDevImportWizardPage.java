package jp.addondev.wizard;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardResourceImportPage;

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
		
		ctv = CheckboxTableViewer.newCheckList(composite, SWT.NONE);
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
				return null;
			}
		});
		
		setControl(composite);
		setPageComplete(true);
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
						try {
							copyProjects((File) selected[i],
									new SubProgressMonitor(monitor, 1));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
	
	private void copyProjects(File dir, IProgressMonitor monitor) throws IOException, CoreException
	{
		//ResourcesPlugin.getWorkspace().getRoot().
		String name = dir.getName();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		IPath path = project.getFullPath().removeLastSegments(1);
		//File dir = new File("C:\\tmp");
		String[] extensions = {};
		boolean recursive = true;
		Collection files = FileUtils.listFiles(dir, extensions, recursive);
		
		File[] srcfile = FileUtils.convertFileCollectionToFileArray(files);
		
		for (File src : srcfile) {
			
			IFile file = project.getFile(path.append(src.getName()));

			//file.create(new ByteArrayInputStream(text.getBytes("UTF-8")), true, monitor);
			file.create(new FileInputStream(src), true, monitor);
			//File dist = new File(.toOSString());
			//FileUtils.copyFile(src, dist);
		}
	}
}
