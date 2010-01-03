package org.addondev.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
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
	
	private String fXulRunnerPath;
	
	public void makePreviewManifestFile(IProject proj, String XulRunnerPath) throws IOException
	{
		IFile file = proj.getFile(MANIFEST_FILENAME);
		IPath projPath = proj.getFullPath();
		String text = FileUtil.getContent(file.getFullPath());		
		String xml = cnvContent(projPath, text);	
		String name = "xulpreview." + proj.getName() + ".manifest";
		save(XulRunnerPath, name, xml);
	}
	
	private void save(String XulRunnerPath, String filename, String data) throws IOException
	{
		IPath path = new Path(XulRunnerPath).append(filename);
		FileUtils.writeStringToFile(path.toFile(), data, "UTF-8");
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
		
		m = localepattern.matcher(text); 
		while(m.find())
		{
			String pack = m.group(1); //pack
			String type = m.group(2); //path
			String uri = m.group(3); //path
			String path = "file:///" + projPath.append(uri).toPortableString();
			xml += String.format("locale %s %s %s", pack, type, path) + "\n";
		}
		return xml;
	}
}
