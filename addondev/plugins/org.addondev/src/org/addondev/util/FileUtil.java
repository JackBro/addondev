package org.addondev.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.addondev.plugin.AddonDevPlugin;
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
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		int len = 0;
//		byte[] buf = new byte[1024 * 8];
//		try {
//			while((len = in.read(buf))!=-1){
//				out.write(buf,0,len);
//			}
//			res = out.toString();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		try {
//			in.close();
//			out.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
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
	
	public static String getContentFormUri(String uri) throws IOException
	{
		URL url = AddonDevPlugin.getDefault().getBundle().getEntry(uri);
		InputStream in = url.openStream();
		String res = getContent(in);
		
		return res;	
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		int len = 0;
//		byte[] buf = new byte[1024 * 8];
//		while((len = in.read(buf))!=-1){
//			out.write(buf,0,len);
//		}
//
//		in.close();
//		out.close();	
//		
//		return out.toString();
	}
	
	public static String getContent(InputStream input) throws IOException
	{		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		int len = 0;
//		byte[] buf = new byte[1024 * 8];
//		while((len = input.read(buf))!=-1){
//			out.write(buf,0,len);
//		}
//
//		input.close();
//		out.close();	
//		
//		return out.toString();
		
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
	
	
}
