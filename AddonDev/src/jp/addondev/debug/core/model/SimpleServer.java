package jp.addondev.debug.core.model;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.eclipse.debug.core.model.IDebugTarget;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class SimpleServer {

	private AddonDebugTarget fTarget;
	private HttpServer server;
	public boolean working;
	//private int port;
	
	public SimpleServer(IDebugTarget target, int port) throws Exception
	{
		fTarget = (AddonDebugTarget) target;
        server = HttpServer.create(new InetSocketAddress(port), 0);
        
        //this.port = port;
        working = false;
	}
	
	public void Start(IDebugTarget target)
	{
		fTarget = (AddonDebugTarget) target;
        server.createContext("/", new HelloHttpHandler(fTarget));
        server.start();
        working = true;
	}
	
	public void Stop() {
		server.stop(0);
		working = false;
	}
	
    private static class HelloHttpHandler implements HttpHandler {
    	
    	private AddonDebugTarget fTarget;
    	
    	public HelloHttpHandler(AddonDebugTarget debugTarget)
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
                		fTarget.breakpointHit(cmd, data);
                	}
                	else if(cmd.equals("ready"))
                	{
                		fTarget.startDebug();
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
