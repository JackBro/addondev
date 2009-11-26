package org.addondev.debug.core;

import java.util.ArrayList;
import java.util.List;

import org.addondev.plugin.AddonDevPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate;
import org.eclipse.debug.core.sourcelookup.containers.ProjectSourceContainer;

public class AddonDevSourcePathComputerDelegate implements
		ISourcePathComputerDelegate {

	@Override
	public ISourceContainer[] computeSourceContainers(
			ILaunchConfiguration configuration, IProgressMonitor monitor)
			throws CoreException {
		// TODO Auto-generated method stub
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<ISourceContainer> containers = new ArrayList<ISourceContainer>();
		for (IProject project : projects) {
			if(project.hasNature(AddonDevPlugin.NATUREID))
			{
				containers.add(new ProjectSourceContainer(project, false));
			}
		}
		
		return containers.toArray(new ISourceContainer[containers.size()]);
	}

}
