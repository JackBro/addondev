package org.addondev.plugin;

import java.io.IOException;
import java.net.URL;

import org.addondev.templates.JavaScriptTemplateContextType;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
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
	
	private ContributionContextTypeRegistry fRegistry = null;
    public ContextTypeRegistry getContextTypeRegistry() {
        if (fRegistry == null) {
            // create an configure the contexts available in the template editor
            fRegistry = new ContributionContextTypeRegistry();
            fRegistry.addContextType(JavaScriptTemplateContextType.JAVASCRIPT_CONTEXT_TYPE);
        }
        return fRegistry;
    }
    
    public static final String TEMPLATE_STORE_ID = "org.addondev.templates.store";
    
    private TemplateStore fStore;
    public TemplateStore getTemplateStore() {
        if (fStore == null) {
            fStore = new ContributionTemplateStore(getContextTypeRegistry(), getPreferenceStore(), TEMPLATE_STORE_ID);
            try {
                fStore.load();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return fStore;
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
