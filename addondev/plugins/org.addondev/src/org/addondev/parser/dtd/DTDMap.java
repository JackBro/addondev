package org.addondev.parser.dtd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

public class DTDMap {
	
	//<!ENTITY stacklink.pref.behavior "Behavior">
	//<!ENTITY stacklink.pref.label "labelの表示方法">
	
	private static Pattern dtdpattern = Pattern.compile("<\\s*!ENTITY\\s+([^\\s]+)\\s+\"([^\"]+)\"\\s*>");
	
	private HashMap<String, HashMap<String, String>> fLocateEntityMap;
	private String fLocale;
	
	public DTDMap()
	{
		fLocateEntityMap = new HashMap<String, HashMap<String,String>>();
	}
	
//	public void parse(IProject prject, String locale)
//	{
//		
//	}
	
	public void parse(String locale, IPath fullpath)
	{
		fLocale = locale;
		
		InputStreamReader inputreader = null;
		BufferedReader bufferreader = null;
		try {
			FileInputStream fin = new FileInputStream(fullpath.toFile());
			inputreader = new InputStreamReader(fin, "UTF-8");
			bufferreader = new BufferedReader(inputreader);
			String line = null;
			while((line = bufferreader.readLine()) != null)
			{
				if(line.length() > 0)
				{
					makeMap(line);
				}
			}			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		finally
		{
			try {
				if(inputreader != null ) inputreader.close();
				if(bufferreader != null ) bufferreader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	private void makeMap(String text)
	{
		Matcher m = dtdpattern.matcher(text);
		if (m.find()) {
			String key = m.group(1);
			String word = m.group(2);	
			
			if(fLocateEntityMap.containsKey(fLocale))
			{
				fLocateEntityMap.get(fLocale).put(key, word);
			}
			else
			{			
				HashMap<String, String> map = new HashMap<String, String>(); 
				map.put(key, word);
				fLocateEntityMap.put(fLocale, map);
			}
		}
	}
	
	public void setLocate(String Locate)
	{
		fLocale = Locate;
	}
	
	public String getLocate()
	{
		return fLocale;
	}
	
	public boolean hasLocate(String Locate)
	{
		return fLocateEntityMap.containsKey(Locate);
	}
	
	public Map<String, String> getEntityMap(String Locate)
	{
		if(fLocateEntityMap.containsKey(Locate))
		{
			return fLocateEntityMap.get(Locate);
		}
		return null;
	}
	
	public String getWord(String key)
	{
		String word = "";
		if(fLocateEntityMap.containsKey(fLocale))
		{
			word = fLocateEntityMap.get(fLocale).get(key);
		}
		else
		{
			
		}
		
		return word;
	}
	
//	public String getEntity(String key)
//	{
//		
//	}
}
