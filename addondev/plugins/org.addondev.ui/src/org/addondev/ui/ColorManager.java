package org.addondev.ui;


import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ColorManager implements IColorManager {
	
	private static ColorManager fInstance = null;
	private IPreferenceStore fStore;
	
	private ColorManager()
	{	
	}
	
	public static ColorManager getInstance()
	{
		if(fInstance == null)
		{
			fInstance = new ColorManager();
		}
		return fInstance;
	}
	
	public void setPreferenceStore(IPreferenceStore store)
	{
		fStore = store;
	}
	
//	public void putColor(String key, RGB vaule)
//	{
//		ColorRegistry cr = JFaceResources.getColorRegistry();
//		cr.put(key, vaule);
//	}
	
	public Color getColor(String key)
	{
		ColorRegistry cr = JFaceResources.getColorRegistry();
		//if(!cr.hasValueFor(key))
		//{
			//cr.put(key, StringConverter.asRGB(fStore.getString(key)));
			cr.put(key, PreferenceConverter.getColor(fStore, key));
		//}

		return cr.get(key);
	}
	
	public RGB getRGB(String key)
	{
		ColorRegistry cr = JFaceResources.getColorRegistry();
		if(!cr.hasValueFor(key))
		{
			//cr.put(key, StringConverter.asRGB(fStore.getString(key)));
			cr.put(key, PreferenceConverter.getColor(fStore, key));
		}	
		return cr.getRGB(key);
	}
}
