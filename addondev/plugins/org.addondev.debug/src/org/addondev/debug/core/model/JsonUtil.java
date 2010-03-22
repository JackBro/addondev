package org.addondev.debug.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.addondev.core.AddonDevPlugin;
import org.addondev.util.ChromeURLMap;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jface.resource.StringConverter;


import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;


public class JsonUtil {
	
	private static boolean checkProp(String[] keys, Map<String, String> map)
	{
		for (String key : keys) {
			if(!map.containsKey(key))
				return false;
		}	
		return true;
	}
	
	public static JsonData getJsonData(String json)
	{
		JsonData data = null;
		try
		{
			data = JSON.decode(json, JsonData.class);
		}catch (JSONException e) {
			// TODO: handle exception
			throw e;
		}
		return data;
	}
	
	public static String getJsonText(JsonData json)
	{
		String text  = JSON.encode(json);
		return text;
	}
	
	//public static IStackFrame[] getStackFrames(IDebugTarget target, JsonData jsondata) throws DebugException
	public static List<IStackFrame> getStackFrames(IDebugTarget target, JsonData jsondata) throws DebugException
	{
		ArrayList<IStackFrame> stacks = new ArrayList<IStackFrame>();
		IThread thread = target.getThreads()[0];
		
		List<Map<String, String>> props = jsondata.getPropertylist();
		for (Map<String, String> map : props) {
			
			String[] keys = {"depth", "url", "filename", "functionname", "line", "fn"};
			if(checkProp(keys, map))
			{
				String depth = map.get("depth");
				String url = map.get("url");
	            String filename = map.get("filename");
				String functionname = map.get("functionname");
	            String line = map.get("line");
	            String fn = map.get("fn");
	            AddonDevStackFrame stackframe = new AddonDevStackFrame(thread, target, depth, url, filename, functionname, line, fn);
	            stacks.add(stackframe);
			}
			else
			{
				
			}
		}
		
		//return stacks.toArray(new IStackFrame[stacks.size()]);
		return stacks;
	}
	
	public static List<IVariable> getVariables(IDebugTarget target, JsonData jsondata, String stackframedepth, String parent) throws DebugException
	{
		ArrayList<IVariable> variables = new ArrayList<IVariable>();
		
		List<Map<String, String>> props = jsondata.getPropertylist();
		for (Map<String, String> map : props) {
			
			String[] keys = {"name", "type", "value", "haschildren"};
			if(checkProp(keys, map))
			{
				String name = map.get("name");
				String type = map.get("type");
				String value = map.get("value");
				boolean haschildren = Boolean.parseBoolean(map.get("haschildren"));
	            AddonDevVariable variable = new AddonDevVariable(target, stackframedepth, parent, name, type, value, haschildren);
	            variables.add(variable);
			}
			else
			{
				
			}
		}
		
		return variables;		
	}

	public static JsonData getJsonData(List<IBreakpoint> breakpoints)
	{
		ArrayList<Map<String, String>> propertylist = new ArrayList<Map<String,String>>();
		
		for (IBreakpoint breakpoint : breakpoints) {
			if (breakpoint instanceof AddonDevBreakpoint) {
				
				AddonDevBreakpoint addonbreakpoint = (AddonDevBreakpoint)breakpoint;
				IProject project = addonbreakpoint.getProject();
				ChromeURLMap chromeurlmap = AddonDevPlugin.getDefault().getChromeURLMap(project, false);
				String chromeurl = chromeurlmap.convertLocal2Chrome(addonbreakpoint.getFile());
				int line;
				try {
					line = addonbreakpoint.getLineNumber();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				
				HashMap<String, String> prop = new HashMap<String, String>();
				prop.put("filename", chromeurl);
				prop.put("line", StringConverter.asString(line));
				
				propertylist.add(prop);
			}			
		}
		JsonData json = new JsonData();
		json.setPropertylist(propertylist);
		return json;		
	}
}
