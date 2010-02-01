package org.addondev.debug.core;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class AddonDevDebugPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.addondev.debug";

	// The shared instance
	private static AddonDevDebugPlugin plugin;
	
	
	public static final String IMG_BP_ENABLE = "bp_enable";
	public static final String IMG_BP_DISABLE = "bp_disable.png";
	public static final String IMG_ADDON = "addondev";
	
	/**
	 * The constructor
	 */
	public AddonDevDebugPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static AddonDevDebugPlugin getDefault() {
		return plugin;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		// TODO Auto-generated method stub
		registerImage(reg, IMG_BP_ENABLE, "icon/bp_enable.png");
		registerImage(reg, IMG_BP_DISABLE, "icon/bp_disable.png");
	}

	private void registerImage(ImageRegistry registry, String key, String path){
		URL url = getBundle().getEntry(path);
		if (url != null) {
			ImageDescriptor desc = ImageDescriptor.createFromURL(url);
			registry.put(key, desc);
		}
	}
	
	public Image getImage(String key) {
		return getImageRegistry().get(key);
	}
}
