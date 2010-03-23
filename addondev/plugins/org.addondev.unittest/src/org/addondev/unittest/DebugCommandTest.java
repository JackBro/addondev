package org.addondev.unittest;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.addondev.debug.core.model.JsonData;
import org.addondev.debug.core.model.JsonUtil;
import org.addondev.debug.net.SendRequest;
import org.addondev.debug.net.SimpleServer;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.jface.resource.StringConverter;
import org.junit.After;
import org.junit.Before;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

class HelloHttpHandler implements HttpHandler {

	public HelloHttpHandler() {
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {

//		String query = exchange.getRequestURI().getQuery();
//		String data = "";
//
//		InputStream in = exchange.getRequestBody();
//		byte buff[] = new byte[1024];
//		int len;
//		while ((len = in.read(buff)) != -1) {
//			data += new String(buff);
//		}
//		in.close();
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("<html></html>");
//
//		byte[] response = sb.toString().getBytes();
//		exchange.sendResponseHeaders(200, response.length);
//		OutputStream output = exchange.getResponseBody();
//		output.write(response);
//		output.close();
//
//		if (query != null) {
//			String[] params = query.split("&");
//			if (params.length > 0) {
//				String cmd = params[0].split("=")[1];
//				if (cmd.equals("suspend")) {
//					// fTarget.breakpointHit(cmd, data);
//					System.out.println(cmd + " : " + data);
//				} else if (cmd.equals("ready")) {
//					// fTarget.startDebug();
//					System.out.println(cmd + " : " + data);
//				} else if (cmd.equals("error")) {
//					int i = 0;
//					i++;
//				}
//			}
//		}
		
			// TODO Auto-generated method stub
//			String query = exchange.getRequestURI().getQuery();     
			
			StringBuilder data = new StringBuilder();
            InputStream in = exchange.getRequestBody(); 
            byte buff[] = new byte[1024];
            int len;
            try {
				while((len = in.read(buff)) != -1) {
					data.append(new String(buff));
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			finally
			{
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			JsonData writejsondata = new JsonData();
			writejsondata.setCmd("accept");
			//StringBuilder sb = new StringBuilder(JsonUtil.getJsonText(writejsondata));
			StringBuilder sb = new StringBuilder("<accept/>");
			
            byte[] response = sb.toString().getBytes();
            OutputStream output = null;
            try {
				exchange.sendResponseHeaders(200, response.length);
	            output = exchange.getResponseBody();
	            output.write(response);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally{
	            if(output != null) 
	            {
	            	try {
						output.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
			}
			System.out.println("handle data = " + data);
			JsonData jsondata = JsonUtil.getJsonData(data.toString());
			if(jsondata == null) return;

			String cmd = jsondata.getCmd();
			if("suspend".equals(cmd))
			{
				//breakpointHit(jsondata);
			}
        	else if("ready".equals(cmd))
        	{
        		//startDebug();
        	}
        	else if("closebrowser".equals(cmd))
        	{
        		//setCloseBrowser(true);
        	}
        	else if("error".equals(cmd))
        	{
        		int i=0;
        		i++;
        	}	
			String text = JsonUtil.getJsonText(jsondata);
			System.out.println("handle = " + text);
			
		}
}

public class DebugCommandTest {

	private SimpleServer server;

	public DebugCommandTest() throws IOException {

		try {
			SendRequest.setDebuggerPort(8083);
			SimpleServer.getInstance().start(new HelloHttpHandler(), 8084);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		BufferedReader buf = new BufferedReader(
				new InputStreamReader(System.in), 1);

		while (true) {
			String inputValue = buf.readLine();
			if ("q".equals(inputValue)) {
				SimpleServer.getInstance().stop();
				break;
			}
			if (inputValue.contains(" ")) {
				String[] cmds = inputValue.split("\\s");
				if (cmds.length > 1) {
					String cmd = cmds[0];
					String param = cmds[1];

//					String xml = String.format(
//							"<breakpoint filename=\"%s\" line=\"%s\"/>",
//							"chrome://stacklink/content/stacklink.js", param);
//					xml = "<xml>" + xml + "</xml>";
					
					String chromeurl = "chrome://stacklink/content/stacklink.js";
					String line = param;
					ArrayList<Map<String, String>> propertylist = new ArrayList<Map<String,String>>();
					HashMap<String, String> prop = new HashMap<String, String>();
					prop.put("filename", chromeurl);
					prop.put("line", line);
					propertylist.add(prop);					
					JsonData json = new JsonData();
					json.setPropertylist(propertylist);
					
					if ("b".equals(cmd)) {
						SendRequest.setBreakPoint(json);
					} else if ("rb".equals(cmd)) {
						SendRequest.removeBreakPoint(json);
					}
				}
			} else {
				if ("o".equals(inputValue)) {
					SendRequest.open();
				} else if ("r".equals(inputValue)) {
					SendRequest.resume();
				} else if ("si".equals(inputValue)) {
					SendRequest.stepInto();
				} else if ("so".equals(inputValue)) {
					SendRequest.stepOver();
				}
			}
		}

	}

	public static void main(String[] args) throws IOException {
		new DebugCommandTest();
	}

}
