package org.addondev.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class FileUtil {
	
	public static String getContent(IPath path)
	{
		return getContent(path.toFile());
	}
	
	public static String getContent(File file)
	{	
		String res = "";
		
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buf = new byte[1024 * 8];
		try {
			while((len = in.read(buf))!=-1){
				out.write(buf,0,len);
			}
			res = out.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	public static String[] getContentLines(IPath fullpath) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		ArrayList<String> lines = new ArrayList<String>();
		
		FileInputStream fin = new FileInputStream(fullpath.toFile());
		
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
					lines.add(line);
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
		
		return lines.toArray(new String[lines.size()]);
	}
}
