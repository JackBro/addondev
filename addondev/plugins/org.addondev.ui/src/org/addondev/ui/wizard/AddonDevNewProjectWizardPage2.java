package org.addondev.ui.wizard;

import java.util.List;

import org.addondev.core.AddonDevPlugin;
import org.addondev.util.ChromeURLMap;
import org.addondev.util.Locale;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AddonDevNewProjectWizardPage2 extends WizardPage {

	public AddonDevNewProjectWizardPage fNewProjectPage;
	private Combo creator;
	
	protected AddonDevNewProjectWizardPage2(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
		setTitle("Fire FoxExtension");
		setDescription("Fire FoxExtension install.rdt");
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        Label label = new Label(composite,SWT.NONE);
        label.setText("：");
        
        creator = new Combo(composite, SWT.READ_ONLY);
        creator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

//		if(fNewProjectPage != null)
//		{
//	        Locale[] locales = fNewProjectPage.getLocals();
//	        for (Locale locale : locales) {
//	        	creator.add(locale.getName());
//			}
//		}
        setControl(composite);
	}
	
	public void setLocals(Locale[] locales)
	{
		if(locales != null)
		{
	        for (Locale locale : locales) {
	        	creator.add(locale.getName());
			}
		}
	}
	
	public String getDefaultLocale()
	{
		String locale = creator.getItem(creator.getSelectionIndex());
		return locale;
	}

}
