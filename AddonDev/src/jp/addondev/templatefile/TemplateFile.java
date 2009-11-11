package jp.addondev.templatefile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class TemplateFile {
	private static TemplateFile inst = null;
	
	private TemplateFile()
	{	
	}
	
	public static TemplateFile getInstance()
	{
		if(inst == null)
			inst = new TemplateFile();
		
		return inst;
	}
	
	public String getText(String filename) throws IOException
	{
		String text = "";
		InputStream in = getClass().getResourceAsStream(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		try {
			while (reader.ready()) 
			{
				text += reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
		finally
		{
			reader.close();
			in.close();
		}
		return text;
	}
}
