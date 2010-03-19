package org.addondev.debug.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;


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
			//throw e;
		}
		return data;
	}
	
	public String getJsonText(JsonData json)
	{
		String text  = null;
		try
		{
			text  = JSON.encode(json);
		}catch (JSONException e) {
			// TODO: handle exception
			//throw e;
		}
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

}
