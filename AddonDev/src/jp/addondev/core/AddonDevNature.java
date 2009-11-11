package jp.addondev.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class AddonDevNature implements IProjectNature {

	private IProject project;
	public static final String ADDONDEV_NATURE_ID = "AddonDev.addondevnature";
	
	@Override
	public void configure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProject getProject() {
		// TODO Auto-generated method stub
		return project;
	}

	@Override
	public void setProject(IProject project) {
		// TODO Auto-generated method stub
		this.project = project;
	}

}
