package jp.addondev.debug.ui.launching;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;

public class AddonDevTabGroup extends AbstractLaunchConfigurationTabGroup {

	public AddonDevTabGroup() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		// TODO Auto-generated method stub
		setTabs(new ILaunchConfigurationTab[] {
				new AddonDevMainTab(),
				//new SourceLookupTab(),
				new CommonTab()
		});
	}

}
