package org.addondev.debug.net;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendRequest {
	private static String fDebuggerPort;
	
	
	
//	public static void setBreakPoint(String filename, int line) throws IOException
//	{	
//		String encStr = URLEncoder.encode("file://" + filename , "UTF-8"); 
//		//String encStr = URLEncoder.encode(filename , "UTF-8"); 
//		String cmd = "cmd=setbreakpoint&filename="+encStr + "&line=" + String.valueOf(line);
//		RequestData(cmd);
//	}
	
	public static int getDebuggerPort() {
		return Integer.parseInt(fDebuggerPort);
	}

	public static void setDebuggerPort(int port) {
		SendRequest.fDebuggerPort = String.valueOf(port);
	}

	public static void setBreakPoint(String data) throws IOException
	{	
		//String cmd = "setbreakpoint";
		RequestData("setbreakpoint", data);
		//RequestData("setbreakpoint");
	}
	
	public static void removeBreakPoint(String data) throws IOException
	{	
		//String cmd = "setbreakpoint";
		RequestData("removebreakpoint", data);
		//RequestData("setbreakpoint");
	}
	
//	public static void load(String filename) throws IOException
//	{
//		String encStr = URLEncoder.encode("file://" + filename , "UTF-8");
//		//String encStr = URLEncoder.encode(filename , "UTF-8");
//		String cmd = "cmd=load&filename=" + encStr;
//		RequestData(cmd);
//	}
	public static String terminate() throws IOException
	{
		return RequestData("terminate");
	}
	
	public static String closeBrowser() throws IOException
	{
		return RequestData("closebrowser");
	}
	
	public static void open(String args) throws IOException
	{
		//String encStr = URLEncoder.encode("file://" + filename , "UTF-8");
		//String encStr = URLEncoder.encode(filename , "UTF-8");
		String cmd = "open";
		RequestData(cmd);
	}
	
	public static void reload() throws IOException
	{
		String cmd = "reload";
		RequestData(cmd);
	}
	
	public static String resume() throws IOException
	{
		//String req = String.format("resume");
		return RequestData("resume");
	}
	
	public static String stepInto() throws IOException
	{
		//String req = String.format("stepinto");
		return RequestData("stepinto");
	}
	
	public static String stepOver() throws IOException
	{
		//String req = String.format("stepover");
		return RequestData("stepover");
	}
	
	public static String getValues(String stackframedepth, String name) throws IOException{		
		//String req;
//		if(name != null)
//			req = String.format("cmd=getvalues&stackframedepth=%s&name=%s", stackframedepth, name);
//		else
//			req = String.format("cmd=getvalues&stackframedepth=%s", stackframedepth);
		
		String data = String.format("<xml><stackframe depth=\"%s\" valuename=\"%s\"/></xml>", stackframedepth, name);
		return RequestData("getvalues", data);
	}
	
	public static String getValue(String stackframedepth, String name) throws IOException{				
		String data = String.format("<xml><stackframe depth=\"%s\" valuename=\"%s\"/></xml>", stackframedepth, name);
		return RequestData("getvalue", data);
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
}
