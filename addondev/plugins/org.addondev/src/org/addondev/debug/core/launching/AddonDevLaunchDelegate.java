package org.addondev.debug.core.launching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


import org.addondev.debug.core.model.AddonDebugTarget;
import org.addondev.debug.net.SimpleServer;
import org.addondev.debug.ui.launching.AddonDevLaunchMainTab;
import org.addondev.plugin.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.addondev.util.FileUtil;
import org.addondev.util.FindPort;
import org.addondev.util.XMLUtils;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.jface.preference.IPreferenceStore;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.xml.sax.SAXException;

public class AddonDevLaunchDelegate extends LaunchConfigurationDelegate implements ILaunchConfigurationDelegate{
	
	private String fProjectName;
	private String fFirefoxPath;
	private String fProfileDir;
	private String fArgs;
	
	private int fEclipsePort;
	private int fDebuggerPort;
	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub				

		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			
			try {
				checkParam(configuration);
			} catch (CoreException e) {
				throw new CoreException(new MultiStatus(AddonDevPlugin.PLUGIN_ID, IStatus.OK, "message", e));
			}
			
			AddonDebugTarget target = new AddonDebugTarget(configuration, launch);
			try {
				target.startPrcess(fEclipsePort, fDebuggerPort, getCommandLine());
				launch.addDebugTarget(target);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				SimpleServer.getInstance().stop();
				
//		        IWorkbench workbench = PlatformUI.getWorkbench();
//		        IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
//				Shell shell = activeWorkbenchWindow.getShell();
//				ErrorDialog.openError(shell, null, null, mStatus);
				throw new CoreException(new MultiStatus(AddonDevPlugin.PLUGIN_ID, IStatus.OK, "message", e));
			}	
		}
	}
	
	private void checkParam(ILaunchConfiguration configuration) throws CoreException
	{
		IPreferenceStore store = AddonDevPlugin.getDefault().getPreferenceStore();
		
		MultiStatus mStatus = null;
		
		try {
			fProjectName = configuration.getAttribute(AddonDevLaunchMainTab.TARGET_PROJECT, "");
			fFirefoxPath = configuration.getAttribute(AddonDevLaunchMainTab.FIREFOX_PATH, "");
			fProfileDir = configuration.getAttribute(AddonDevLaunchMainTab.FIREFOX_PROFILE_PATH , "");
			fArgs = configuration.getAttribute(AddonDevLaunchMainTab.FIREFOX_ARGS , "");				
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mStatus = new MultiStatus(AddonDevPlugin.PLUGIN_ID, IStatus.OK, "message", e);
		}
		
		if(!new File(fFirefoxPath).exists())
		{
			mStatus.add(new Status(IStatus.ERROR, 
					AddonDevPlugin.PLUGIN_ID, IStatus.OK, "メッセージ１", new FileNotFoundException("")));
		}
		if(!new File(fProfileDir).exists())
		{
			mStatus.add(new Status(IStatus.ERROR, 
					AddonDevPlugin.PLUGIN_ID, IStatus.OK, "メッセージ１", new FileNotFoundException("")));
		}
		
		if(store.getBoolean(PrefConst.DEBUG_PORT_AUTO))
		{
			fEclipsePort = FindPort.getFreePort("localhost", 
					store.getInt(PrefConst.DEBUG_PORT_AUTO_ECLIPSE_START),
					store.getInt(PrefConst.DEBUG_PORT_AUTO_ECLIPSE_END));
			fDebuggerPort = FindPort.getFreePort("localhost", 
					store.getInt(PrefConst.DEBUG_PORT_AUTO_DEBUGGER_START), 
					store.getInt(PrefConst.DEBUG_PORT_AUTO_DEBUGGER_END));			
		}
		else
		{
			int eport = store.getInt(PrefConst.DEBUG_PORT_MANUAL_ECLIPSE);
			int dport = store.getInt(PrefConst.DEBUG_PORT_MANUAL_DEBUGGER);
			fEclipsePort = FindPort.getFreePort("localhost", eport, eport);
			fDebuggerPort = FindPort.getFreePort("localhost", dport, dport);				
		}
		
		if(fEclipsePort == 0)
		{
			mStatus.add(new Status(IStatus.ERROR, 
					AddonDevPlugin.PLUGIN_ID, IStatus.OK, "メッセージ１", new Exception("")));
		}
		if(fDebuggerPort == 0)
		{
			mStatus.add(new Status(IStatus.ERROR, 
					AddonDevPlugin.PLUGIN_ID, IStatus.OK, "メッセージ１", new Exception("")));
		}
		
		if(mStatus != null)
		{
//	        IWorkbench workbench = PlatformUI.getWorkbench();
//	        IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
//			Shell shell = activeWorkbenchWindow.getShell();
//			ErrorDialog.openError(shell, null, null, mStatus);
//			return;
			throw new CoreException(mStatus);
		}
		
		try {
			checkAddonIDFile(configuration);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mStatus = new MultiStatus(AddonDevPlugin.PLUGIN_ID, IStatus.OK, "message", e);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mStatus = new MultiStatus(AddonDevPlugin.PLUGIN_ID, IStatus.OK, "message", e);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mStatus = new MultiStatus(AddonDevPlugin.PLUGIN_ID, IStatus.OK, "message", e);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mStatus = new MultiStatus(AddonDevPlugin.PLUGIN_ID, IStatus.OK, "message", e);
		}
		if(mStatus != null)
		{
//	        IWorkbench workbench = PlatformUI.getWorkbench();
//	        IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
//			Shell shell = activeWorkbenchWindow.getShell();
//			ErrorDialog.openError(shell, null, null, mStatus);
//			return;
			throw new CoreException(mStatus);
		}
	}
	
	private void checkAddonIDFile(ILaunchConfiguration configuration) throws CoreException, IOException, ParserConfigurationException, SAXException, TransformerException
	{		
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject project = workspace.getRoot().getProject(fProjectName);
        if(project == null)
        {
        	
        }
		IPath extensionsDirPath = new Path(fProfileDir).append("extensions");
		InputStream in = project.getFile("install.rdf").getContents();
		String rdf = FileUtil.getContent(in);
		
		String id = XMLUtils.getAddonIDFromRDF(rdf);
			String path = extensionsDirPath.append(id).toOSString();
			File file = new File(path);	
			if(file.exists())
			{
				
			}
			else
			{		
				FileUtils.writeStringToFile(file, path);
			}	
	}
	
	private String[] getCommandLine()
	{

//		ArrayList<String> commands = new ArrayList<String>();	
//		commands.add(fFirefoxPath);	
//		commands.add("-no-remote");
//		commands.add("-profile");
//		commands.add("\"" + fProfileDir + "\"");
//				
//		commands.add("-ce_eport");
//		commands.add(String.valueOf(fEclipsePort));
//		commands.add("-ce_cport");
//		commands.add(String.valueOf(fDebuggerPort));
//		
//		commands.add("-chrome");
//		commands.add("chrome://chromebug/content/chromebug.xul");
//
//		if(fArgs.trim().length() > 0)
//		{
//			String args = fArgs.trim();
//			if(args.contains(" "))
//			{
//				for (String arg : args.split(" ")) {
//					commands.add(arg);
//				}					
//			}
//			else
//			{
//				commands.add(args);
//			}
//	
//		}
//		
//		return commands;
		
		String cmdlines = String.format("%s -no-remote -profile \"%s\" -ce_eport %d -ce_cport %d chrome chrome://chromebug/content/chromebug.xul",
				fFirefoxPath, fProfileDir, fEclipsePort, fDebuggerPort);
		
		return cmdlines.split("\\s");
	}
}
