package jp.addondev.debug.ui.launching;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.addondev.plugin.AddonDevPlugin;
import jp.addondev.preferences.PrefConst;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;


public class AddonDevMainTab extends AbstractLaunchConfigurationTab {

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return AddonDevPlugin.getDefault().getImage(AddonDevPlugin.IMG_ADDON);
		//return super.getImage();
	}

	private FileFieldEditor ffirefoxPathEditor;
	private Text fprofilename;
	private DirectoryFieldEditor fprofiledir;	
	private Text ffirefoxargs;	
	private CheckboxTableViewer viewer;

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub   
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
				
        Group firefoxpathgroup= new Group(composite, SWT.NONE);
        firefoxpathgroup.setText("firefox path"); 
        GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
        firefoxpathgroup.setLayoutData(gd1);
        GridLayout layout1 = new GridLayout();
        layout1.numColumns = 1;
        firefoxpathgroup.setLayout(layout1);
        
	    Composite composite5 = new Composite(firefoxpathgroup, SWT.NONE);
		GridLayout layout5 = new GridLayout();
		layout5.numColumns = 3;
		composite5.setLayout(layout5);
		composite5.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
        ffirefoxPathEditor = new FileFieldEditor("test", "firefox Path", composite5);
        ffirefoxPathEditor.setPropertyChangeListener(new IPropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// TODO Auto-generated method stub
				updateLaunchConfigurationDialog();
			}
		
		});        
        
		
        Group profilegroup= new Group(composite, SWT.NONE);
        profilegroup.setText("Profile"); 
        GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
        profilegroup.setLayoutData(gd2);
        GridLayout layout2 = new GridLayout();
        layout2.numColumns = 1;
        profilegroup.setLayout(layout2);
        
	    Composite composite3 = new Composite(profilegroup, SWT.NONE);
		GridLayout layout3 = new GridLayout();
		layout3.numColumns = 3;
		composite3.setLayout(layout3);
		composite3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		fprofilename = addText(composite3, "Profile Name");
		fprofilename.addListener(SWT.Modify, new Listener(){

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				updateLaunchConfigurationDialog();
			}
        	
        });
		
		fprofiledir = new DirectoryFieldEditor("test", "Profile Path", composite3);
		fprofiledir.setPropertyChangeListener(new IPropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// TODO Auto-generated method stub
				updateLaunchConfigurationDialog();
			}
		
		});
		
		
        Group firefixargsgroup= new Group(composite, SWT.NONE);
        firefixargsgroup.setText("Args"); 
        firefixargsgroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        firefixargsgroup.setLayout(new GridLayout(1, false));
		
        ffirefoxargs =  new Text(firefixargsgroup, SWT.SINGLE | SWT.BORDER );
        ffirefoxargs.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        ffirefoxargs.addListener(SWT.Modify, new Listener(){

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				updateLaunchConfigurationDialog();
			}
        	
        });		
        
        Group addongroup= new Group(composite, SWT.NONE);
        addongroup.setText("Debug Addons"); 
        addongroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        addongroup.setLayout(new GridLayout(1, true));

        Table table = new Table(addongroup, SWT.CHECK | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        viewer = new CheckboxTableViewer(table);

        TableColumn column1 = new TableColumn(table, SWT.NULL);
        column1.setText("Project Name");
        column1.setWidth(200);

        TableColumn column2 = new TableColumn(table, SWT.NULL);
        column2.setText("Project Path");
        column2.setWidth(400);
;
        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setLabelProvider(new MyLabelProvider());
        viewer.addSelectionChangedListener(new ISelectionChangedListener()
        {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
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

	@SuppressWarnings("unchecked")
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub
		try {	
			
			String firefoxpath = configuration.getAttribute(PrefConst.DEBUG_APP_PATH, "");
			ffirefoxPathEditor.setStringValue(firefoxpath);
			
			fprofilename.setText(configuration.getAttribute(PrefConst.DEBUG_PROFILENANE, ""));
			String dir = configuration.getAttribute(PrefConst.DEBUG_PROFILEDIR, "");
			fprofiledir.setStringValue(dir);
			ffirefoxargs.setText(configuration.getAttribute(PrefConst.DEBUG_ARGS, ""));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(viewer.getTable().getItemCount() >0) return;
		
		//IPath chrome = new Path("chrome.manifest");
		//IPath install = new Path("install.rdf");
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject[] projects = workspace.getRoot().getProjects();
        for(int i=0; i<projects.length; i++)
        {
        	IProject project = projects[i];
//        	if(project.exists(chrome) && project.exists(install))
//        	{
//        		viewer.add(new Customer(project.getName(), project.getFullPath().toString()));
//        	}
        	try {
				if(project.hasNature(AddonDevPlugin.NATUREID))
				{
					viewer.add(new Customer(project.getName(), project.getFullPath().toString()));
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
		List<String> excheckedlist = null;
		try {
			excheckedlist = configuration.getAttribute(PrefConst.FIREFOX_DEBUGTARGETADDONS, new ArrayList<String>());
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(excheckedlist == null) return;
		int itemcnt = viewer.getTable().getItemCount();
		for(int i=0; i<itemcnt; i++)
		{
			Customer customer  = (Customer)viewer.getElementAt(i);
			if(excheckedlist.contains(customer.name))
			{
				viewer.setChecked(customer, true);
			}
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub		
		configuration.setAttribute(PrefConst.DEBUG_APP_PATH, ffirefoxPathEditor.getStringValue());
		
		configuration.setAttribute(PrefConst.DEBUG_PROFILENANE, fprofilename.getText());
		
		String value = fprofiledir.getStringValue();
		configuration.setAttribute(PrefConst.DEBUG_PROFILEDIR, value);
		
		configuration.setAttribute(PrefConst.DEBUG_ARGS, ffirefoxargs.getText());

		//Map<String, String> excheckedmap = new HashMap<String, String>();
		List<String> excheckedlist = new ArrayList<String>();
		Object[] objects = viewer.getCheckedElements();
		for (int i = 0; i < objects.length; i++) {
			Customer customer = (Customer) objects[i];
			//excheckedmap.put(customer.name, customer.path);
			excheckedlist.add(customer.name);
		}
		configuration.setAttribute(AddonDevPlugin.DEBUG_ADDONS, excheckedlist);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		boolean result = super.isValid(launchConfig);
		if (result) {
		String name = fprofilename.getText();
		if (name.length() == 0) {
			result = true;
		}
		
		}
		if (result) {
			String dir = fprofiledir.VALUE;
			File file = new File(dir);
			if(file.exists())
			{
				result = false;
			}
		}
		
		return result;
	}
	
	private Text addText(Composite parent, String label) 
	{		
        Label L = new Label(parent, SWT.NONE);
        L.setText(label);

        Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Label L2 = new Label(parent, SWT.NONE);
        L2.setText("");    

        return text;
	}
	
	public class Customer {
	    public String name;
	    public String path;
	    
	    public Customer(String name, String path)
	    {
	    	this.name = name;
	    	this.path = path;
	    }
	}
	
	private class MyLabelProvider extends LabelProvider implements
    ITableLabelProvider {
	    public Image getColumnImage(Object element, int columnIndex) {
	        return null;
	    }
	    public String getColumnText(Object element, int columnIndex) {
	        Customer customer = (Customer)element;
	        switch (columnIndex){
	        case 0:
	            return customer.name;
	        case 1:
	            return customer.path;
	        }
	        return "";
	    }
	}
}