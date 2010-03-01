package org.addondev.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.addondev.core.AddonDevPlugin;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

public class FileUtil {
	
	public static String getContent(IPath path) throws IOException
	{
		return getContent(path.toFile());
	}
	
	public static String getContent(File file) throws IOException
	{	
		InputStream in = new FileInputStream(file);
		String res = getContent(in);
		return res;
	}
	
	public static IPath getPath(String url)
	{
		if(url.indexOf("file:///") == 0)
		{
			int len = "file:///".length();
			String location = url.substring(0, len);
			if(location.equals("file:///"))
			{
				return new Path(location);
			}
		}
		return null;
	}
	
	//public static String getContentFormUri(URL url) throws IOException
	public static String getContent(URL url) throws IOException
	{
		//URL url = AddonDevPlugin.getDefault().getBundle().getEntry(uri);
		InputStream in = url.openStream();
		String res = getContent(in);
		
		return res;	
	}
	
	public static String getContent(InputStream input) throws IOException
	{			
		String res = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buf = new byte[1024 * 8];
		try {
			while((len = input.read(buf))!=-1){
				out.write(buf,0,len);
			}
			res = out.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try {
				if(input !=null) input.close();
				if(out !=null) out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		return res;
	}
	
	public static void copyDirectory(File src, File dist, FileFilter filter, Boolean foce) throws IOException
	{
		FileUtils.copyDirectory(src, dist, filter, true);
	}
	
	public static void write(File file, String text) throws IOException
	{
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.print(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if(pw != null) pw.close(); 
		}
		
	}
}
