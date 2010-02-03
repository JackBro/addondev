package org.addondev.ui;

import java.io.IOException;

import org.addondev.ui.template.JavaScriptTemplateContextType;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class AddonDevUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.addondev.ui";

	// The shared instance
	private static AddonDevUIPlugin plugin;
	
    private TemplateStore fTemplateStore;
	private ContributionContextTypeRegistry fRegistry = null;
	public static final String TEMPLATE_STORE_ID = "org.addondev.templates.store";
	
	/**
	 * The constructor
	 */
	public AddonDevUIPlugin() {
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
	public static AddonDevUIPlugin getDefault() {
		return plugin;
	}

	public ContextTypeRegistry getContextTypeRegistry() {
		if (fRegistry == null) {
			fRegistry = new ContributionContextTypeRegistry();
			fRegistry
					.addContextType(JavaScriptTemplateContextType.JAVASCRIPT_CONTEXT_TYPE);
		}
		return fRegistry;
	}

	public TemplateStore getTemplateStore() {
		if (fTemplateStore == null) {
			fTemplateStore = new ContributionTemplateStore(getContextTypeRegistry(),
					getPreferenceStore(), TEMPLATE_STORE_ID);
			try {
				fTemplateStore.load();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return fTemplateStore;
	}
}
