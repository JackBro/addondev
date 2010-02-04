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
	
	@SuppressWarnings("restriction")
	public void start(IDebugTarget debugtarget, int port) throws IOException
	{	
		fTarget = (AddonDevDebugTarget) debugtarget;
		fPort = port;
		
		server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new HelloHttpHandler(fTarget));
        server.start();
        working = true;
	}
	
	@SuppressWarnings("restriction")
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
        	///InputStream inputsm = exchange.getRequestBody();
        	//byte[] buf = new byte[1024];
        	StringBuffer data = new StringBuffer();
            // String data = "";
            InputStream in = exchange.getRequestBody(); 
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len;
            while((len = in.read(buff)) != -1) {
            	//out.write(buff, 0, len);
            	data.append(new String(buff));
            }
            in.close();

            //out.close();

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
							fTarget.breakpointHit(cmd, data.toString());
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
