package org.addondev.debug.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;


import org.addondev.debug.core.model.AddonDevDebugTarget;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class SimpleServer {

	private static SimpleServer fInstance = null;
	private int fPort;
	private AddonDevDebugTarget fTarget;
	
	public int getPort() {
		return fPort;
	}

	public AddonDevDebugTarget getDebugTarget() {
		return fTarget;
	}

	private HttpServer server;
	public boolean working;
	
	private SimpleServer()
	{
		 //server = HttpServer.create(new InetSocketAddress(port), 0);
	}
	
	public static SimpleServer getInstance()
	{
		if(fInstance == null)
		{
			fInstance = new SimpleServer();
		}
		
		return fInstance;
	}
	
//	public SimpleServer(IDebugTarget target, int port) throws Exception
//	{
//		fTarget = (AddonDebugTarget) target;
//        server = HttpServer.create(new InetSocketAddress(port), 0);
//        
//        //this.port = port;
//        working = false;
//	}
	
	public void start(IDebugTarget debugtarget, int port) throws IOException
	{	
		fTarget = (AddonDevDebugTarget) debugtarget;
		fPort = port;
		
		server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new HelloHttpHandler(fTarget));
        server.start();
        working = true;
	}
	
	public void stop() {
		server.stop(0);
		working = false;
	}
	
    private static class HelloHttpHandler implements HttpHandler {
    	
    	private AddonDevDebugTarget fTarget;
    	
    	public HelloHttpHandler(AddonDevDebugTarget debugTarget)
    	{
    		fTarget = debugTarget;
    	}
    	
        public void handle(HttpExchange exchange) throws IOException {
        	
        	String query = exchange.getRequestURI().getQuery();
        	//String data = null;
        	
        	///InputStream inputsm = exchange.getRequestBody();
        	//byte[] buf = new byte[1024];
        	 //StringBuilder postsb = new StringBuilder();
             String data = "";
            //int offse=0;
//        	while(inputsm.read(buf, 0, buf.length) != -1)
//            //while(inputsm.read(buf) != -1)
//        	{
//        		System.out.println("cnt");
//        		//postsb.append(new String(buf));
//        		data += new String(buf);
//        		//offse += buf.length;
//        	}
        	
             
            InputStream in = exchange.getRequestBody(); 
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len;
            while((len = in.read(buff)) != -1) {
            	//out.write(buff, 0, len);
            	data += new String(buff);
            }
            in.close();

            //out.close();
            
//             DataInputStream dis = new DataInputStream(exchange.getRequestBody());
//             byte[] buf = new byte[1024];
//             while(dis.read(buf,0,buf.length) != -1)
//             {
//            	 //System.out.println(new String(buf));
//            	 data += new String(buf);
//             }
//             dis.close();
            
        	
            //data = out.toString();
            
        	//data += new String(buf);
        	//System.out.println(data);
            //exchange.close();
            StringBuilder sb = new StringBuilder();
            sb.append("<html></html>");

            byte[] response = sb.toString().getBytes();
            exchange.sendResponseHeaders(200, response.length);
            OutputStream output = exchange.getResponseBody();
            output.write(response);
            output.close();
            //exchange.close();
            
            if(query !=null)
            {
            	String[] params = query.split("&");
            	if(params.length > 0)
            	{
                	String cmd =  params[0].split("=")[1];
                	if(cmd.equals("suspend"))
                	{
                		try {
							fTarget.breakpointHit(cmd, data);
						} catch (DebugException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}
                	else if(cmd.equals("ready"))
                	{
                		fTarget.startDebug();
                	}
                	else if("closebrowser".equals(cmd))
                	{
                		fTarget.setCloseBrowser(true);
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
