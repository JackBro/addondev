package jp.addondev.preferences;

import jp.addondev.plugin.AddonDevPlugin;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;

public class ResourceManager {
	
	private static ResourceManager fInstance = null;
	
	private ResourceManager()
	{	
	}
	
	public static ResourceManager getInstance()
	{
		if(fInstance == null)
		{
			fInstance = new ResourceManager();
		}
		return fInstance;
	}
	
	public Color getColor(String key)
	{
		ColorRegistry cr = JFaceResources.getColorRegistry();
		if(!cr.hasValueFor(key))
		{
			//String tmp = AddonDevPlugin.getDefault().getPreferenceStore().getString(key);
			cr.put(key, StringConverter.asRGB(AddonDevPlugin.getDefault().getPreferenceStore().getString(key)));
		}	
		return cr.get(key);
	}
}
