package org.addondev.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class ExtensionLoader {
	
	private static Map<String, IExtension[]> cache = new HashMap<String, IExtension[]>();
	
	public static final String EXTENSION_POINT_ID = "org.addondev.builder";
	
	public static List getExtensions(String id)
	{
		ArrayList list = new ArrayList();
		IExtension[] extensions = cache.get(id);
		if(extensions == null)
		{
			IExtensionRegistry registry = Platform.getExtensionRegistry();		
			IExtensionPoint point = registry.getExtensionPoint(id);
		}
		
		for (int i = 0; i < extensions.length; i++) {
		
			IConfigurationElement[] cfgElems = extensions[i].getConfigurationElements();
			for (int j = 0; j < cfgElems.length; j++) {
				IConfigurationElement cfgElem = cfgElems[j];
				try {
					list.add(cfgElem.createExecutableExtension("class"));
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			}
		}
		
		return list;
	}
}
