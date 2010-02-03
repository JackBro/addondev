package org.addondev.debug.ui.launching;

import java.io.File;
import java.util.ArrayList;

import org.addondev.core.AddonDevNature;
import org.addondev.debug.core.AddonDevDebugPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class AddonDevLaunchMainTab extends AbstractLaunchConfigurationTab {

	public static final String TARGET_PROJECT = "org.addondev.launch.project";
	public static final String FIREFOX_PATH = "org.addondev.launch.firefox.path";
	public static final String FIREFOX_PROFILE_PATH = "org.addondev.launch.firefox.profile.path";
	public static final String FIREFOX_ARGS = "org.addondev.launch.firefox.args";
	
	private static class projectLabelProvider implements ILabelProvider
	{

		@Override
		public Image getImage(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getText(Object element) {
			// TODO Auto-generated method stub
			if(element instanceof IProject)
			{
				return ((IProject)element).getName();
			}
			return null;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}
	}
	
	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return AddonDevDebugPlugin.getDefault().getImage(AddonDevDebugPlugin.IMG_ADDON);
		//return super.getImage();
	}

	private Text fProjectText;
	private FileFieldEditor fFirefoxPathEditor;
	private DirectoryFieldEditor fProfiledir;
	private StringFieldEditor fArgsText;
	

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub   
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        int space = 5;
        
        Group projectgroup= new Group(composite, SWT.NONE);
        projectgroup.setText("project"); 		
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        projectgroup.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        projectgroup.setLayout(layout);
        
        fProjectText = new Text(projectgroup, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fProjectText.setLayoutData(gd);
         
        Button fProjectBrowseButton = createPushButton(projectgroup, "Browse...", null);
        fProjectBrowseButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IProject[] projects = workspace.getRoot().getProjects();
                ArrayList<IProject> pythonProjects = new ArrayList<IProject>();
                for (IProject project:projects) {
                   try {
                       if (project.isOpen() && project.hasNature(AddonDevNature.NATUREID)) {
                           pythonProjects.add(project);
                       }
                   } catch (CoreException exception) {
                       //PydevPlugin.log(exception);
                   }
                   
                }
				
                ILabelProvider labelProvider= new projectLabelProvider();
	            ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), labelProvider);
	            dialog.setTitle("Project selection"); 
	            dialog.setMessage("Choose a project for the run"); 
	            dialog.setElements(projects);   
	            dialog.open();	
	            
               Object object = dialog.getFirstResult();
               if ((object != null) && (object instanceof IProject)) {
                    IProject project = (IProject) object;     
                    String projectName = project.getName();
                    fProjectText.setText(projectName);     
               }
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
        Group firefoxgroup= new Group(composite, SWT.NONE);
        firefoxgroup.setText("firefox"); 
        GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
        firefoxgroup.setLayoutData(gd1);
        GridLayout layout1 = new GridLayout();
        layout1.numColumns = 1;
        firefoxgroup.setLayout(layout1);
        
		Composite pathform = new Composite(firefoxgroup, SWT.NONE);
		GridData formgd = new GridData(GridData.FILL_HORIZONTAL);
		formgd.verticalSpan = space;
		pathform.setLayoutData(formgd);
		
		fFirefoxPathEditor = new FileFieldEditor(FIREFOX_PATH, "firefox Path", pathform);
        fFirefoxPathEditor.setPropertyChangeListener(new IPropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// TODO Auto-generated method stub
				updateLaunchConfigurationDialog();
			}
		
		});     


		Composite profileform = new Composite(firefoxgroup, SWT.NONE);
		profileform.setLayoutData(formgd);
		 		
		fProfiledir = new DirectoryFieldEditor(FIREFOX_PROFILE_PATH, "Profile Path", profileform);
		fProfiledir.setPropertyChangeListener(new IPropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// TODO Auto-generated method stub
				updateLaunchConfigurationDialog();
			}
		
		});
		
		Composite argsform = new Composite(firefoxgroup, SWT.NONE);
		argsform.setLayoutData(formgd);
		fArgsText = new StringFieldEditor(FIREFOX_ARGS, "args", argsform);
		fArgsText.setPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// TODO Auto-generated method stub
				updateLaunchConfigurationDialog();
			}
		});
        
		setControl(composite);
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Main";
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub
		try {	
			fProjectText.setText(configuration.getAttribute(TARGET_PROJECT, ""));
			fFirefoxPathEditor.setStringValue(configuration.getAttribute(FIREFOX_PATH, ""));
			fProfiledir.setStringValue(configuration.getAttribute(FIREFOX_PROFILE_PATH, ""));
			fArgsText.setStringValue(configuration.getAttribute(FIREFOX_ARGS, ""));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub
		configuration.setAttribute(TARGET_PROJECT, fProjectText.getText());
		configuration.setAttribute(FIREFOX_PATH, fFirefoxPathEditor.getStringValue());
		configuration.setAttribute(FIREFOX_PROFILE_PATH, fProfiledir.getStringValue());
		configuration.setAttribute(FIREFOX_ARGS, fArgsText.getStringValue());
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		boolean result = super.isValid(launchConfig);
		if (result) {
			//String name = fprofilename.getText();
			String profiledir = fProfiledir.getStringValue();
			if (profiledir.length() == 0) {
			result = false;
			}
		}
		if (result) {
			String dir = fProfiledir.getStringValue();
			File file = new File(dir);
			if(!file.exists())
			{
				result = false;
			}
		}
		
		return result;
	}	
}