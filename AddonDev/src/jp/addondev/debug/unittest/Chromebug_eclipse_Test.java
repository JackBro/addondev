package jp.addondev.debug.unittest;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import jp.addondev.debug.net.SendRequest;
import junit.framework.TestCase;

import org.eclipse.debug.core.model.IDebugTarget;
import org.junit.After;
import org.junit.Before;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

class SimpleServer {

	private HttpServer server;
	public boolean working;
	//private int port;
	
	public SimpleServer(int port) throws Exception
	{
        server = HttpServer.create(new InetSocketAddress(port), 0);
        
        //this.port = port;
        working = false;
	}
	
	public void Start()
	{
        server.createContext("/", new HelloHttpHandler());
        server.start();
        working = true;
        System.out.println("server.start"); 
	}
	
	public void Stop() {
		server.stop(0);
		working = false;
		System.out.println("server.stop"); 
	}
	
    private static class HelloHttpHandler implements HttpHandler {
    		    	
    	public HelloHttpHandler()
    	{
    	}
    	
        public void handle(HttpExchange exchange) throws IOException {
        	
        	String query = exchange.getRequestURI().getQuery();
            String data = "";
        	       
            InputStream in = exchange.getRequestBody(); 
            byte buff[] = new byte[1024];
            int len;
            while((len = in.read(buff)) != -1) {
            	data += new String(buff);
            }
            in.close();

            StringBuilder sb = new StringBuilder();
            sb.append("<html></html>");

            byte[] response = sb.toString().getBytes();
            exchange.sendResponseHeaders(200, response.length);
            OutputStream output = exchange.getResponseBody();
            output.write(response);
            output.close(); 

            if(query !=null)
            {
            	String[] params = query.split("&");
            	if(params.length > 0)
            	{
                	String cmd =  params[0].split("=")[1];
                	if(cmd.equals("suspend"))
                	{
                		//fTarget.breakpointHit(cmd, data);
                		System.out.println(cmd + " : " + data); 
                	}
                	else if(cmd.equals("ready"))
                	{
                		//fTarget.startDebug();
                		System.out.println(cmd + " : " + data); 
                	}
                	else if(cmd.equals("error"))
                	{
                		int i=0;
                		i++;
                	}
            	}
            }
        }           
    }
}

public class Chromebug_eclipse_Test{


	private SimpleServer server;
	
    public Chromebug_eclipse_Test() throws IOException {
    
    	try {
    		SendRequest.debuggerport = "8083";
			server = new SimpleServer(8084);
			server.Start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
    	
         BufferedReader buf =
             new BufferedReader(
                    new InputStreamReader(System.in),1);

         while (true) {
        	 String inputValue = buf.readLine();
        	 if("q".equals(inputValue)) 
        	 {
        		 server.Stop();
        		 break;
        	 }
        	 if(inputValue.contains(" "))
        	 {
        		 String[] cmds = inputValue.split("\\s");
        		 if(cmds.length > 1)
        		 {
        			 String cmd = cmds[0];
        			 String param = cmds[1];
        			 
        			 String xml = String.format("<breakpoint filename=\"%s\" line=\"%s\"/>",  
        					 "chrome://stacklink/content/stacklink.js", param);
        			 xml = "<xml>" +xml + "</xml>";
            		 if("b".equals(cmd))
            		 {
            			 SendRequest.setBreakPoint(xml);
            		 }
            		 else if("rb".equals(cmd))
            		 {
            			 SendRequest.removeBreakPoint(xml);
            		 }       			 
        		 }
        	 }
        	 else
        	 {
        		 if("o".equals(inputValue))
        		 {
        			 SendRequest.open("");
        		 }
        		 else if("r".equals(inputValue))
        		 {
        			 SendRequest.resume();
        		 }
        		 else if("si".equals(inputValue))
        		 {
        			 SendRequest.stepInto();
        		 }
        		 else if("so".equals(inputValue))
        		 {
        			 SendRequest.stepOver();
        		 }
        	 }
         }

    }
    
    public static void main(String[] args) throws IOException {
    	//junit.textui.TestRunner.run(Chromebug_eclipse_Test);
    	
    	
    	new Chromebug_eclipse_Test();
    }

}
