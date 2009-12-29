package org.addondev.core;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

public class AddonIncrementalProjectBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "org.addondev.core.AddonIncrementalProjectBuilder";
	
	public AddonIncrementalProjectBuilder() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		// TODO Auto-generated method stub
		if(kind != FULL_BUILD)
		{
			IResourceDelta delta = getDelta(getProject());
			if(delta != null)
			{
				//delta.getResource().getName()
				switch (delta.getKind()) {
				case IResourceDelta.CHANGED:
					IFile file = getProject().getFile("chrome.manifest");
					IPath fBasePath = file.getLocation().removeLastSegments(1);
					IPath fBasePath2 = file.getFullPath().removeLastSegments(1);
					int i=0;
					i++;
					
					break;

				default:
					break;
				}
			}
		}
		
		return null;
	}

	private void build()
	{
		
	}
}
