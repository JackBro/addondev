package org.addondev.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

	
	public void makePreviewManifestFile(IProject proj, String XulRunnerPath) throws IOException
	{
		String filename = "xulpreview." + proj.getName() + ".manifest";
		IPath path = new Path(XulRunnerPath).append("chrome").append(filename);
		if(!path.toFile().exists())
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
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path.toFile())));
		pw.println(data);
		pw.flush();
		pw.close();
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
