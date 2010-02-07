package org.addondev.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class ManifestUtil {
	
	public static String MANIFEST_FILENAME = "chrome.manifest";
	
	private static String contentaccessible = "contentaccessible=yes";
	
	private static Pattern contentpattern = Pattern.compile("content\\s+([^\\s]+)\\s+([^\\s]+)");
	private static Pattern skinepattern   = Pattern.compile("skin\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)");
	private static Pattern localepattern  = Pattern.compile("locale\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)");

//	private static String[] locales = {"bg-BG", "ca-AD", "cs-CZ", "da-DK", "de-DE", 
//											"en-US", "es-AR", "es-ES", "fr-FR", "hu-HU", 
//											"it-IT", "ja-JP", "ko-KR", "nl-NL", "pl-PL", 
//											"pt-BR", "ro-RO", "ru-RU", "sk-SK", "sv-SE", 
//											"tr-TR", "uk-UA", "zh-CN", "zh-TW"};
	
	public void makePreviewManifestFile(IProject proj, IPath xulrunnerpath, boolean force) throws IOException
	{
		String filename = "xulpreview." + proj.getName() + ".manifest";
		//IPath path = new Path(XulRunnerPath).append("chrome").append(filename);
		IPath path = xulrunnerpath.removeLastSegments(1).append("chrome").append(filename);
		if(force || !path.toFile().exists())
		{	
			IFile file = proj.getFile(MANIFEST_FILENAME);
			IPath projPath = proj.getLocation();
			String text = FileUtil.getContent(file.getLocation());		
			String xml = cnvContent(projPath, text);	
			
			save(path, xml);
		}
	}
	
	private void save(IPath path, String data) throws IOException
	{
		//FileUtils.writeStringToFile(path.toFile(), data, "UTF-8");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(path.toFile())));
			pw.println(data);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw e;
		}
		finally
		{
			if(pw != null)pw.close();
		}
	}
	
	private String cnvContent(IPath projPath, String text)
	{
		//String path = "file:///" + projPath.toPortableString();
		Matcher m = contentpattern.matcher(text);
		String xml = ""; 
		while(m.find())
		{
			String pack = m.group(1); //all
			String uri = m.group(2); //path
			String path = "file:///" + projPath.append(uri).toPortableString();
			xml += String.format("content %s %s %s", pack, path, contentaccessible) + "\n";
		}
		
		m = skinepattern.matcher(text); 
		while(m.find())
		{
			String pack = m.group(1); //pack
			String type = m.group(2); //path
			String uri = m.group(3); //path
			String path = "file:///" + projPath.append(uri).toPortableString();
			xml += String.format("skin %s %s %s", pack, type, path) + "\n";
		}
		
		//locale stacklink en-US file:///path
		m = localepattern.matcher(text); 
		if(m.find())
		{
			String pack = m.group(1); //pack
			String type = m.group(2); //locale
			String uri = m.group(3);  //path
			
			IPath ls = projPath.append(uri).removeLastSegments(1);
//			for (String locale : locales) {
//				IPath l = ls.append(locale);
//				String path = "file:///" + l.toPortableString() + "/";
//				xml += String.format("locale %s%s %s %s", pack, locale, locale, path) + "\n";
//			}
			for (Locale locale : Locale.values()) {
				IPath l = ls.append(locale.getName());
				String path = "file:///" + l.toPortableString() + "/";
				xml += String.format("locale %s%s %s %s", pack, locale.getName(), locale.getName(), path) + "\n";				
			}
			//String path = "file:///" + projPath.append(uri).toPortableString();
			//xml += String.format("locale %s%s %s %s", pack, type, type, path) + "\n";
		}
		return xml;
	}
}
