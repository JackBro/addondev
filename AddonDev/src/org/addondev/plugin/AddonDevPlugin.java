package org.addondev.plugin;

import java.net.URL;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class AddonDevPlugin extends AbstractUIPlugin {
	
	public static final String IMG_BP_ENABLE = "bp_enable";
	public static final String IMG_BP_DISABLE = "bp_disable.png";
	public static final String IMG_ADDON = "addondev";
	
	public static final String NATUREID = "AddonDev.addondevnature";
	//static public final String ADDON_BREAK_MARKER = "AddonDev.addonStopBreakpointMarker";
	
	//public static final String ID_BREAK_MARKER = "org.eclipse.debug.core.breakpointMarker";
	//public static final String ID_DEBUG_MODEL = "org.eclipse.debug.core.breakpointMarker";
		
	private static AddonDevPlugin plugin;
	
	public AddonDevPlugin(){
        super();
        plugin = this;
	}
	
	public static AddonDevPlugin getDefault() {
		// TODO Auto-generated method stub
		return plugin;
	}


	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		registerImage(registry, IMG_BP_ENABLE, "bp_enable.png");
		registerImage(registry, IMG_BP_DISABLE, "bp_disable.png");
		registerImage(registry, IMG_ADDON, "addon.png");
	}
	
	@SuppressWarnings("deprecation")
	private void registerImage(ImageRegistry registry, String key,String fileName){
		try {
			IPath path = new Path("icons/" + fileName);
			URL url = find(path);
			if (url != null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				registry.put(key, desc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	
	public Image getImage(String key) {
		return getImageRegistry().get(key);
	}
	
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	
//	public static void startServer(IDebugTarget target, int port) throws Exception
//	{
//		if(eclipseServer == null)
//		{
//			eclipseServer = new SimpleServer(target, port);
//		}
//		if(!eclipseServer.working)
//			eclipseServer.Start(target);
//	}
//	
//	public static void stopServer()
//	{
//		if(eclipseServer != null && eclipseServer.working)
//			eclipseServer.Stop();
//	}
}
