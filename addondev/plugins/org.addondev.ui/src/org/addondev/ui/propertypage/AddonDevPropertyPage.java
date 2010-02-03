package org.addondev.ui.propertypage;

import java.util.List;

import org.addondev.core.AddonDevPlugin;
import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.util.ChromeURLMap;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class AddonDevPropertyPage extends PropertyPage implements
		IWorkbenchPropertyPage {
	
	private String LOCALE = "LOCALE";
	private Combo creator = null;
	
	public AddonDevPropertyPage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		IProject project = (IProject)getElement();
		
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        Label label = new Label(composite,SWT.NONE);
        label.setText("：");
        
        creator = new Combo(composite, SWT.READ_ONLY);
        creator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        ChromeURLMap map = AddonDevPlugin.getDefault().getChromeURLMap(project, false);
        List<String> localelist = map.getLocaleList();
        for (String locale : localelist) {
        	creator.add(locale);
		}
        
		
        String locale = null;
		try {
			
			locale = project.getPersistentProperty(new QualifiedName(AddonDevUIPlugin.PLUGIN_ID , LOCALE));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(locale == null)
		{
			creator.setText(creator.getItem(0));
		}
		else
		{
			creator.setText(locale);
		}
		return null;
	}

	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		IProject project = (IProject)getElement();
		String locale =creator.getItem(creator.getSelectionIndex());
		try {
			project.setPersistentProperty(new QualifiedName(AddonDevPlugin.PLUGIN_ID , LOCALE), locale);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.performOk();
	}
	
	

}
