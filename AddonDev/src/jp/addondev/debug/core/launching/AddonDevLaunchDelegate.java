package jp.addondev.debug.core.launching;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import jp.addondev.debug.core.model.AddonDebugTarget;
import jp.addondev.debug.core.model.JSSourceLocator;
import jp.addondev.plugin.AddonDevPlugin;
import jp.addondev.util.AddonDevUtil;

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

//		IProject pro = workspace.getRoot().getProject("/helloworld-0.0.6"); //ok
//		IProject pro2 = workspace.getRoot().getProject("helloworld-0.0.6"); //ok
//		
//		IPath pp = configuration.getLocation();
//		
//		String pn = configuration.getName();
//		
//		//helloworld-0.0.6/chrome/content/helloworld/helloWorld.js
//		String p="helloworld-0.0.6/chrome/content/helloworld/helloWorld.js";
//		String chpath = AddonDevUtil.getURI(p);	
//		
		//String startuofile = configuration.getAttribute(JSMainTab.STARTUPFILE, (String)null);
		//String file = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		
		//IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(startuofile));
		//String fullpath = file.getFullPath().toString();
		
//		String startuofile = configuration.getAttribute(JSMainTab.STARTUPFILE, (String)null);
//		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(startuofile));
//		String fullpath  = file.getLocation().toString();
		//IFile file;
		//file.getContents(force)
		
		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			
			AddonDevUtil addondevutil = new AddonDevUtil();	
			try {
				addondevutil.init(configuration);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (TransformerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try 
			{		
				
				//IPath p =  addondevutil.getPath("chrome://helloworld/content/helloWorld.js");
				
				AddonDebugTarget target = new AddonDebugTarget(configuration, launch, addondevutil);
				
				target.startPrcess(configuration, launch);
				
				launch.addDebugTarget(target);
				launch.setSourceLocator(new JSSourceLocator());

				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
