package org.addondev.debug.core.launching;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


import org.addondev.debug.core.model.AddonDebugTarget;
import org.addondev.plugin.AddonDevPlugin;
import org.addondev.util.AddonDevUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.xml.sax.SAXException;

public class AddonDevLaunchDelegate extends LaunchConfigurationDelegate implements ILaunchConfigurationDelegate{

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub				
		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			
			try 
			{		
				
				//IPath p =  addondevutil.getPath("chrome://helloworld/content/helloWorld.js");
				
				
				
				
				AddonDebugTarget target = new AddonDebugTarget(configuration, launch);
				target.init();
				target.startPrcess(configuration, launch);
				
				launch.addDebugTarget(target);
				//launch.setSourceLocator(new JSSourceLocator());

				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
