package org.addondev.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.addondev.parser.javascript.JsNode;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class ChromeURLMap {
	
	public static String MANIFEST_FILENAME = "chrome.manifest";
	
	private static Pattern contentpattern = Pattern.compile("content\\s+([^\\s]+)\\s+([^\\s]+)");
	private static Pattern skinepattern   = Pattern.compile("skin\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)");
	private static Pattern localepattern  = Pattern.compile("locale\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)");
	
	private static Pattern chrome_content_pattern = Pattern.compile("chrome:\\/\\/([^\\s^\\/]+)\\/content\\/(.*)"); 
	private static Pattern chrome_skin_pattern = Pattern.compile("chrome:\\/\\/([^\\s^\\/]+)\\/skin\\/(.*)"); 
	private static Pattern chrome_locale_pattern = Pattern.compile("chrome:\\/\\/([^\\s^\\/]+)\\/locale\\/(.*)");  
	
	private String fLocale;
	private IPath fBasePath;
	
	private HashMap<String, HashMap<String, String>> fContentMap = new HashMap<String, HashMap<String,String>>();
	private HashMap<String, HashMap<String, String>> fSkinMap = new HashMap<String, HashMap<String,String>>();
	private HashMap<String, HashMap<String, String>> fLocaleMap = new HashMap<String, HashMap<String,String>>();
	
	
	public void setLocale(String locate)
	{
		fLocale = locate;
	}
	
	public ChromeURLMap()
	{
		fLocale = "en-US";
	}
	
	public void clear()
	{
		fContentMap.clear();
		fSkinMap.clear();
		fLocaleMap.clear();
	}
	
	public void readManifest(IFile manifest) throws CoreException, IOException
	{
		//fContentMap = new HashMap<String, String>();
		//IFile file = fProject.getFile(MANIFEST_FILENAME);
		IFile file = manifest;
		//fBasePath = file.getLocation().removeLastSegments(1);
		fBasePath = file.getFullPath().removeLastSegments(1);
		
		InputStreamReader in = new InputStreamReader(file.getContents(), "UTF-8");
		BufferedReader reader = new BufferedReader(in);
		String line = null;
		while((line = reader.readLine()) != null)
		{
			makeContentMap(line);
			makeSkinMap(line);
			makeLocaleMap(line);
		}
	}
	

	public void readManifest(IPath manifestfullpath) throws FileNotFoundException {
		// TODO Auto-generated method stub
		//fBasePath = file.getLocation().removeLastSegments(1);
		fBasePath = manifestfullpath.removeLastSegments(1);
		
		FileInputStream fin = new FileInputStream(manifestfullpath.toFile());
		
		InputStreamReader inputreader = null;
		BufferedReader bufferreader = null;
		try {
			inputreader = new InputStreamReader(fin, "UTF-8");
			bufferreader = new BufferedReader(inputreader);
			String line = null;
			while((line = bufferreader.readLine()) != null)
			{
				if(line.length() > 0)
				{
				makeContentMap(line);
				makeSkinMap(line);
				makeLocaleMap(line);
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
	
	// /stacklink/chrome/content/tmp/stacklink.js //file
	// /stacklink/chrome/content/stacklink.js
	// chrome/content/ uri
	// /stacklink/chrome/content/ //path
	// chrome://stacklink/content/stacklink.js //path
	public String convertLocal2Chrome(IFile file)
	{
		String chromeurl = null;
		for(String key1 : fContentMap.keySet()) {
			
			HashMap<String, String> map = fContentMap.get(key1);
			String uri = map.get("uri");
			IPath path = fBasePath.append(uri);
			if(file.getFullPath().toPortableString().indexOf(path.toPortableString()) == 0)
			{
				chromeurl = "chrome://" + 
				file.getFullPath().toPortableString().replaceFirst(path.toPortableString(), key1 + "/content/");
				break;
				
			}
		}
		return chromeurl;
	}
	
	public String convertLocal2Chrome(IPath fullpath)
	{
		String chromeurl = null;
		for(String key1 : fContentMap.keySet()) {
			
			HashMap<String, String> map = fContentMap.get(key1);
			String uri = map.get("uri");
			IPath path = fBasePath.append(uri);
			if(fullpath.toPortableString().indexOf(path.toPortableString()) == 0)
			{
				chromeurl = "chrome://" + 
				fullpath.toPortableString().replaceFirst(path.toPortableString(), key1 + "/content/");
				break;
				
			}
		}
		return chromeurl;
	}
	
	//content 	stacklink	chrome/content/
	//chrome://stacklink/content/stacklink.js
	public String convertChrome2Local(String path)
	{
		
		
		String localpath = null;
		Matcher m = chrome_content_pattern.matcher(path);
		if (m.find()) {
			String name = m.group(1);
			String file = m.group(2);
			String uri = fContentMap.get(name).get("uri");
			localpath = "file:///" + fBasePath.append(uri).append(file).toPortableString();
			return localpath;
		}
		
		m = chrome_skin_pattern.matcher(path);
		if (m.find()) {
			String name = m.group(1);
			String file = m.group(2);
			String uri = fSkinMap.get(name).get("uri");
			//localpath = uri + file;
			localpath = "file:///" + fBasePath.append(uri).append(file).toPortableString();
			return localpath;
		}
		
		m = chrome_locale_pattern.matcher(path);
		if (m.find()) {
			String name = m.group(1);
			String file = m.group(2);
			String uri = fLocaleMap.get(name).get(fLocale);
			//localpath = uri + file;
			localpath = "file:///" + fBasePath.append(uri).append(file).toPortableString();
			return localpath;
		}
		
		return localpath;
	}
	
	public void makeContentMap(String content)
	{
		Matcher m = contentpattern.matcher(content);
		if (m.find()) {
			HashMap<String, String> map = new HashMap<String, String>(); 
			String packagename = m.group(1);
			String uri = m.group(2);
			map.put("packagename", packagename);
			map.put("uri", uri);
			fContentMap.put(packagename, map);
			
			//System.out.println(m.group(0));
			//System.out.println(m.group(1));
			//System.out.println(m.group(2));
		}		
	}
	
	public void makeSkinMap(String content)
	{
		Matcher m = skinepattern.matcher(content);
		if (m.find()) {
			HashMap<String, String> map = new HashMap<String, String>(); 
			String packagename = m.group(1);
			String uri = m.group(3);
			map.put("packagename", packagename);
			map.put("uri", uri);
			fSkinMap.put(packagename, map);
			
			//System.out.println(m.group(0));
			//System.out.println(m.group(1));
			//System.out.println(m.group(2));
			//System.out.println(m.group(3));
		}		
	}	
	
	//chrome://stacklink/locale/stacklink.dtd
	//locale	stacklink	en-US		locale/en-US/
	//locale	stacklink	ja-JP		locale/ja-JP/
	public void makeLocaleMap(String content)
	{
		Matcher m = localepattern.matcher(content);
		if (m.find()) {
			
			String packagename = m.group(1);
			String locale = m.group(2);
			String uri = m.group(3);			

			//map.put("uri", uri);
			if(fLocaleMap.containsKey(packagename))
			{
				fLocaleMap.get(packagename).put(locale, uri);
			}
			else
			{
				HashMap<String, String> map = new HashMap<String, String>(); 
				map.put(locale, uri);
				fLocaleMap.put(packagename, map);
			}
			
			//System.out.println(m.group(0));
			//System.out.println(m.group(1));
			//System.out.println(m.group(2));
			//System.out.println(m.group(3));
		}		
	}

	

	
}
