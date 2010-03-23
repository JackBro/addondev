package org.addondev.debug.net;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import net.arnx.jsonic.JSONException;

import org.addondev.debug.core.model.JsonData;
import org.addondev.debug.core.model.JsonUtil;
import org.eclipse.debug.core.model.IBreakpoint;

public class SendRequest {
	private static String fDebuggerPort;
	
	public static int getDebuggerPort() {
		return Integer.parseInt(fDebuggerPort);
	}

	public static void setDebuggerPort(int port) {
		SendRequest.fDebuggerPort = String.valueOf(port);
	}

	public static void setBreakPoint(JsonData json) throws IOException
	{	
		//RequestData("setbreakpoint", data);
		json.setCmd("setbreakpoint");
		String data = JsonUtil.getJsonText(json);
		post(data);
	}
	
	public static void setBreakPoint(List<IBreakpoint> breakpoints) throws IOException, JSONException
	{	
		JsonData json = JsonUtil.getJsonData(breakpoints);
		json.setCmd("setbreakpoint");
		String data = JsonUtil.getJsonText(json);
		post(data);
	}
	
	public static void removeBreakPoint(String data) throws IOException
	{	
		RequestData("removebreakpoint", data);
		//post(data);
	}
	
	public static void removeBreakPoint(JsonData json) throws IOException
	{	
		json.setCmd("removebreakpoint");
		String data = JsonUtil.getJsonText(json);
		post(data);
	}
	
	public static void removeBreakPoint(List<IBreakpoint> breakpoints) throws IOException, JSONException
	{	
		JsonData json = JsonUtil.getJsonData(breakpoints);
		json.setCmd("removebreakpoint");
		String data = JsonUtil.getJsonText(json);
		post(data);
	}
	
//	public static void load(String filename) throws IOException
//	{
//		String encStr = URLEncoder.encode("file://" + filename , "UTF-8");
//		//String encStr = URLEncoder.encode(filename , "UTF-8");
//		String cmd = "cmd=load&filename=" + encStr;
//		RequestData(cmd);
//	}
//	public static String terminate() throws IOException
//	{
//		return RequestData("terminate");
//	}
	
	public static String terminate() throws IOException, JSONException
	{
		JsonData json = new JsonData();
		json.setCmd("terminate");
		String data = JsonUtil.getJsonText(json);
		return post(data);
	}
	
	public static String closeBrowser() throws IOException
	{
		return RequestData("closebrowser");
	}
	
	public static String closeWindow() throws IOException, JSONException
	{
		JsonData json = new JsonData();
		json.setCmd("closebrowser");
		String data = JsonUtil.getJsonText(json);
		return post(data);
	}
	
	public static void open(String args) throws IOException
	{
		//String encStr = URLEncoder.encode("file://" + filename , "UTF-8");
		//String encStr = URLEncoder.encode(filename , "UTF-8");
		String cmd = "open";
		RequestData(cmd);
	}
	
	public static void open() throws IOException, JSONException
	{
		//String cmd = "open";
		//RequestData(cmd);
		JsonData json = new JsonData();
		json.setCmd("open");
		String data = JsonUtil.getJsonText(json);
		post(data);
	}
	
	public static void reload() throws IOException
	{
		String cmd = "reload";
		RequestData(cmd);
	}
	
	public static String resume() throws IOException, JSONException
	{
		//return RequestData("resume");
		JsonData json = new JsonData();
		json.setCmd("resume");
		String data = JsonUtil.getJsonText(json);
		return post(data);	
	}
	
	public static String stepInto() throws IOException, JSONException
	{
		JsonData json = new JsonData();
		json.setCmd("stepinto");
		String data = JsonUtil.getJsonText(json);
		return post(data);	
		//return RequestData("stepinto");
	}
	
	public static String stepOver() throws IOException, JSONException
	{
		JsonData json = new JsonData();
		json.setCmd("stepover");
		String data = JsonUtil.getJsonText(json);
		return post(data);	
		//return RequestData("stepover");
	}
	
	public static String getValues(String stackframedepth, String name) throws IOException, JSONException
	{		
		JsonData json = new JsonData();
		json.setCmd("getvalues");
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("depth", stackframedepth);
		map.put("valuename", name);
		json.SetProperty(map);
		
		String data = JsonUtil.getJsonText(json);
		return post(data);	
		
//		String data = String.format("<xml><stackframe depth=\"%s\" valuename=\"%s\"/></xml>", stackframedepth, name);
//		return RequestData("getvalues", data);
	}
	
	public static String getValue(String stackframedepth, String name) throws IOException{				
//		String data = String.format("<xml><stackframe depth=\"%s\" valuename=\"%s\"/></xml>", stackframedepth, name);
//		return RequestData("getvalue", data);
		
		JsonData json = new JsonData();
		json.setCmd("getvalue");
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("depth", stackframedepth);
		map.put("valuename", name);
		json.SetProperty(map);
		
		String data = JsonUtil.getJsonText(json);
		return post(data);	
	}
	
	private static String RequestData(String command) throws IOException
	{			
		URL url1 = new URL("http://localhost:" + fDebuggerPort + "/?cmd=" + command);
		HttpURLConnection http = (HttpURLConnection)url1.openConnection();
		http.setRequestMethod("GET");
		http.connect();

		//String res="";
		//StringBuilder
		StringBuffer res = new StringBuffer();
		
        InputStream in = http.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte buff[] = new byte[1024];
        int len;
        while((len = in.read(buff)) != -1) {
        	//out.write(buff, 0, len);
        	//res += new String(buff);
        	res.append(new String(buff));
        }
        in.close();
        out.close();
    	
        //res = out.toString();
			
		if(http!=null)  http.disconnect();
		
		return res.toString();
	}
	
	private static String RequestData(String command, String data) throws IOException
	{			
		URL url1 = new URL("http://localhost:" + fDebuggerPort + "/?cmd=" + command);
		HttpURLConnection http = (HttpURLConnection)url1.openConnection();
		http.setRequestMethod("POST");

		http.setDoInput(true);
		http.setDoOutput(true); 
		
		http.connect();

		//OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream());

		BufferedOutputStream os = new BufferedOutputStream(http.getOutputStream());
		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

		osw.write(data);
		osw.flush();
		osw.close();
		os.close();

		//String res="";	
		StringBuffer res = new StringBuffer();
		
        InputStream in = http.getInputStream();
//        //ByteArrayOutputStream out = new ByteArrayOutputStream();
//        byte buff[] = new byte[1024];
//        int len;
//        while((len = in.read(buff)) != -1) {
//        	//out.write(buff, 0, len);
//        	res += new String(buff);
//        }
//        in.close();
//        //out.close();
//        //res = out.toString();
        
        String str;
        BufferedReader sockin = new BufferedReader(new InputStreamReader(in));
        while ((str = sockin.readLine()) != null) {
            //System.out.println(str);
        	//res += str;
        	res.append(str);
          }
        sockin.close();
        in.close();
      
        
        
//        DataInputStream dis = new DataInputStream(in);
//        byte[] buf = new byte[1024];
//        while(dis.read(buf,0,buf.length) != -1)
//        {
//       	 //System.out.println(new String(buf));
//        	res += new String(buf);
//        }
//        dis.close();
//        in.close();
			
		if(http!=null)  http.disconnect();
		
		return res.toString();
	}
	
	private static String post(String data) throws IOException 
	{			
		URL url1 = null;
		HttpURLConnection http = null;
		try {
			url1 = new URL("http://localhost:" + fDebuggerPort);
			http = (HttpURLConnection)url1.openConnection();
			http.setRequestMethod("POST");
			http.setDoInput(true);
			http.setDoOutput(true); 			
			http.connect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		
		BufferedOutputStream os = null;
		OutputStreamWriter osw = null;
		try {
			os = new BufferedOutputStream(http.getOutputStream());
			osw = new OutputStreamWriter(os, "UTF-8");
			osw.write(data);
			osw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally{
			try {
				if(osw != null) osw.close();
				if(os != null) os.close();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		StringBuffer res = new StringBuffer();
		
        InputStream in=null;
        BufferedReader sockin=null;
		try {
			in = http.getInputStream();
	        sockin = new BufferedReader(new InputStreamReader(in));
	        String str;
	        while ((str = sockin.readLine()) != null) {
	            //System.out.println(str);
	        	//res += str;
	        	res.append(str);
	          }	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		finally{
			try {
				if(sockin != null) sockin.close();
				if(in != null) in.close();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(http!=null) 
		{		
			http.disconnect();	
		}
		
		return res.toString();
	}
}
