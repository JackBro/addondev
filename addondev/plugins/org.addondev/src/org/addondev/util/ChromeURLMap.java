package org.addondev.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class ChromeURLMap {
	
	public static String MANIFEST_FILENAME = "chrome.manifest";
	
	private static Pattern contentpattern = Pattern.compile("^\\s*content\\s+([^\\s]+)\\s+([^\\s]+)");
	private static Pattern resourcepattern = Pattern.compile("^\\s*resource\\s+([^\\s]+)\\s+([^\\s]+)");
	private static Pattern skinepattern   = Pattern.compile("^\\s*skin\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)");
	private static Pattern localepattern  = Pattern.compile("^\\s*locale\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)");
	
	private static Pattern chrome_content_pattern = Pattern.compile("chrome:\\/\\/([^\\s^\\/]+)\\/content\\/(.*)"); 
	private static Pattern chrome_resource_pattern = Pattern.compile("resource:\\/\\/([^\\s^\\/]+)"); 
	private static Pattern chrome_skin_pattern = Pattern.compile("chrome:\\/\\/([^\\s^\\/]+)\\/skin\\/(.*)"); 
	private static Pattern chrome_locale_pattern = Pattern.compile("chrome:\\/\\/([^\\s^\\/]+)\\/locale\\/(.*)");  
	
	//private String fLocale;
	private Locale fLocale;
	private IPath fBasePath;
	private IPath fLocationBasePath;
	
	private ArrayList<Locale> fLocaleList = new ArrayList<Locale>();
	
	private HashMap<String, HashMap<String, String>> fContentMap = new HashMap<String, HashMap<String,String>>();
	private HashMap<String, HashMap<String, String>> fResourceMap = new HashMap<String, HashMap<String,String>>();
	private HashMap<String, HashMap<String, String>> fSkinMap = new HashMap<String, HashMap<String,String>>();
	private HashMap<String, HashMap<String, String>> fLocaleMap = new HashMap<String, HashMap<String,String>>();
	
	
	public void setLocale(Locale locate)
	{
		fLocale = locate;
	}
	public Locale getLocale()
	{
		return fLocale;
	}	
	
	
	public List<Locale> getLocaleList()
	{
		//ArrayList<Locale> tmp = new ArrayList<Locale>();
		//tmp.add(Locale.bg_BG);/
		
		return fLocaleList;
	}
	
	public ChromeURLMap()
	{
		//fLocale = "en-US";
		fLocale = Locale.en_US;
	}
	
	public void clear()
	{
		fContentMap.clear();
		fSkinMap.clear();
		fLocaleMap.clear();
	}
	
	public void readManifest(IFile manifest) throws CoreException, IOException
	{
		fLocaleList.clear();
		//fContentMap = new HashMap<String, String>();
		//IFile file = fProject.getFile(MANIFEST_FILENAME);
		IFile file = manifest;
		//fBasePath = file.getLocation().removeLastSegments(1);
		fBasePath = file.getFullPath().removeLastSegments(1);
		fLocationBasePath = file.getLocation().removeLastSegments(1);
			
		InputStreamReader in = new InputStreamReader(file.getContents(), "UTF-8");
		BufferedReader reader = new BufferedReader(in);
		String line = null;
		while((line = reader.readLine()) != null)
		{
			makeContentMap(line);
			makeResourceMap(line);
			makeSkinMap(line);
			makeLocaleMap(line);
		}
	}
	
	public void readManifest(File manifest) throws CoreException, IOException
	{
		fLocaleList.clear();
		//fContentMap = new HashMap<String, String>();
		//IFile file = fProject.getFile(MANIFEST_FILENAME);
		//IFile file = manifest;
		//fBasePath = file.getLocation().removeLastSegments(1);
		IPath path = new Path(manifest.getAbsolutePath());
		//fBasePath = path.removeLastSegments(1);
		fLocationBasePath = path.removeLastSegments(1);
			
		FileInputStream fin = new FileInputStream(manifest);
		
		InputStreamReader in = new InputStreamReader(fin);// (fin. , "UTF-8");
		BufferedReader reader = new BufferedReader(in);
		String line = null;
		while((line = reader.readLine()) != null)
		{
			makeContentMap(line);
			makeResourceMap(line);
			makeSkinMap(line);
			makeLocaleMap(line);
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
			String packagename = map.get("packagename");
			String uri = map.get("uri");
			//String dd = file.getFullPath().toPortableString();
			//String dds =path.toPortableString();
			//if(file.getFullPath().toPortableString().indexOf(path.toPortableString()) == 0)
			if(file.getFullPath().toPortableString().contains(uri))
			//if(file.getFullPath().toPortableString().contains(packagename + "/" +uri))
			{
				//chromeurl = "chrome:/" + 
				//file.getFullPath().toPortableString().replaceFirst(path.toPortableString(), key1 + "/content/");
				String lpath = file.getFullPath().removeFirstSegments(1).toPortableString();
				//chromeurl = "chrome:/" + 
				//	file.getFullPath().toPortableString().replaceFirst(uri, "content/");
				chromeurl = "chrome://" + packagename + lpath.replaceFirst(uri, "content/");
				return chromeurl;
				
			}
		}
		
		if(chromeurl == null)
		{
			for(String key1 : fResourceMap.keySet()) {
				
				HashMap<String, String> map = fResourceMap.get(key1);
				String packagename = map.get("packagename");
				String uri = map.get("uri");
				//String dd = file.getFullPath().toPortableString();
				//String dds =path.toPortableString();
				//if(file.getFullPath().toPortableString().indexOf(path.toPortableString()) == 0)
				if(file.getFullPath().toPortableString().contains(uri))
				//if(file.getFullPath().toPortableString().contains(packagename + "/" +uri))
				//if(file.getFullPath().segment(1).equals(anObject))
				{
					String lpath = file.getFullPath().removeFirstSegments(1).toPortableString();
					chromeurl = "resource:/" + lpath.replaceFirst(uri, packagename);
					return chromeurl;
					
				}
		}		
		}
		
		
		return chromeurl;
	}
	
//	public String convertLocal2Chrome(IPath fullpath)
//	{
//		String chromeurl = null;
//		for(String key1 : fContentMap.keySet()) {
//			
//			HashMap<String, String> map = fContentMap.get(key1);
//			String uri = map.get("uri");
//			IPath path = fBasePath.append(uri);
//			if(fullpath.toPortableString().indexOf(path.toPortableString()) == 0)
//			{
//				chromeurl = "chrome://" + 
//				fullpath.toPortableString().replaceFirst(path.toPortableString(), key1 + "/content/");
//				break;
//				
//			}
//		}
//		return chromeurl;
//	}
	
	public String convertChrome2Local(String path, String prefix)
	{
		prefix = prefix==null?"":prefix;
		String localpath = null;
		//String abbasepath = fBasePath.toFile().getAbsolutePath();// makeAbsolute().toPortableString();
		Matcher m = chrome_content_pattern.matcher(path);
		if (m.find()) {
			String name = m.group(1);
			String file = m.group(2);
			if(fContentMap.containsKey(name) && fContentMap.get(name).containsKey("uri"))
			{
				String uri = fContentMap.get(name).get("uri");
				localpath = prefix + fLocationBasePath.append(uri).append(file).toPortableString();
				return localpath;				
			}
		}
		
		m = chrome_resource_pattern.matcher(path);
		if (m.find()) {
			String file = m.group(1);
			if(fResourceMap.containsKey(file) && fResourceMap.get(file).containsKey("uri"))
			{
				String name = fContentMap.get(file).get("packagename");
				String uri = fContentMap.get(file).get("uri");
				
				String lpath = file.replace(name, uri);
				
				localpath = prefix + fLocationBasePath.append(lpath).toPortableString();
				return localpath;				
			}
		}	
		
		m = chrome_skin_pattern.matcher(path);
		if (m.find()) {
			String name = m.group(1);
			String file = m.group(2);
			if(fSkinMap.containsKey(name) && fContentMap.get(name).containsKey("uri"))
			{
				String uri = fSkinMap.get(name).get("uri");
				//localpath = uri + file;
				localpath = prefix + fLocationBasePath.append(uri).append(file).toPortableString();
				return localpath;
			}
		}
		
		m = chrome_locale_pattern.matcher(path);
		if (m.find()) {
			String name = m.group(1);
			String file = m.group(2);
			if(fLocaleMap.containsKey(name) && fLocaleMap.get(name).containsKey(fLocale.getName()))
			{
				String uri = fLocaleMap.get(name).get(fLocale.getName());
				//localpath = uri + file;
				localpath = prefix + fLocationBasePath.append(uri).append(file).toPortableString();
				return localpath;
			}
		}
		
		return localpath;		
	}
	
	//content 	stacklink	chrome/content/
	//chrome://stacklink/content/stacklink.js
	public String convertChrome2Local(String path)
	{
		return convertChrome2Local(path, null);
	}
	
//	public String convertDTDChrome2Local(String path)
//	{
//		String localpath = null;
//		Matcher m = chrome_locale_pattern.matcher(path);
//		if (m.find()) {
//			String name = m.group(1);
//			String file = m.group(2);
//			String uri = fLocaleMap.get(name).get(fLocale);
//			//localpath = uri + file;
//			localpath = fBasePath.append(uri).append(file).toPortableString();
//			return localpath;
//		}
//		return localpath;
//	}
	
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
	
	//resource test_modules modules/
	public void makeResourceMap(String content)
	{
		Matcher m = resourcepattern.matcher(content);
		if (m.find()) {
			HashMap<String, String> map = new HashMap<String, String>(); 
			String packagename = m.group(1);
			String uri = m.group(2);
			map.put("packagename", packagename);
			map.put("uri", uri);
			fResourceMap.put(packagename, map);
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
			
			Locale elocale = Locale.getLocale(locale);
			if(elocale != null)
				fLocaleList.add(elocale);
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
