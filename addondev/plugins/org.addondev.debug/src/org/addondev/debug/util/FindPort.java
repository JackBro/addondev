package org.addondev.debug.util;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class FindPort {
	
	public static int getFreePort(String host, int start, int end)
	{
		Socket sock = null ;
		int freeport = 0;
		for (int port = start; port <= end; port++) {
			try {
				sock = new Socket( host, port ) ;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				freeport = port;
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				freeport = port;
				break;
			}
			finally
			{
				try
				{
					if(sock != null) sock.close();
				} catch (IOException e) {
					// TODO: handle exception
				}
			}
		}
		
		return freeport;
	}
}
