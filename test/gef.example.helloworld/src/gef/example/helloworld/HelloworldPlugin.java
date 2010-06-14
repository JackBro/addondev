package gef.example.helloworld;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.resources.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

import java.net.URL;
import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class HelloworldPlugin extends AbstractUIPlugin {
	
	public static final String IMG_CHECKBOX = "checkbox";
	public static final String IMG_COLORPICKER = "colorpicker";
	public static final String IMG_RADIO = "dummy";
	public static final String IMG_DUMMY = "radio";
	
	//The shared instance.
	private static HelloworldPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	/**
	 * The constructor.
	 */
	public HelloworldPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		try {
			resourceBundle= ResourceBundle.getBundle("gef.example.helloworld.HelloworldPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static HelloworldPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle= HelloworldPlugin.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		// TODO Auto-generated method stub
		super.initializeImageRegistry(reg);
		registerImage(reg, IMG_CHECKBOX, "checkbox.png");
		registerImage(reg, IMG_COLORPICKER, "colorpicker.png");
		registerImage(reg, IMG_RADIO, "radio.png");
		registerImage(reg, IMG_DUMMY, "dummy.png");
	}
	
	private void registerImage(ImageRegistry registry, String key,String fileName){
		URL url = getBundle().getEntry("icon/" + fileName);
		if (url != null) {
			ImageDescriptor desc = ImageDescriptor.createFromURL(url);
			registry.put(key, desc);
		}
	}
	
	public static IEditorPart getActiveEditorPart(){
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
		IEditorPart editorPart = workbenchPage.getActiveEditor();
		
		return editorPart;
	}
}
