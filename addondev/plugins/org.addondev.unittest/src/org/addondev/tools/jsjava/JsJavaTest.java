package org.addondev.tools.jsjava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.addondev.parser.javascript.serialize.JsData;
import org.addondev.parser.javascript.serialize.JsElement;
import org.addondev.tools.javadoc.EasyDoclet;
import org.addondev.tools.javadoc.Java2JS;
import org.addondev.tools.javadoc.MyDoclet;
import org.addondev.tools.javadoc.ReflectJava;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.sun.javadoc.RootDoc;

public class JsJavaTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if(args.length < 2) 
		{
			System.out.println("arg1=datapath, arg2=jjsmap file path");
			return;
		}
		//String docpath = "D:/data/src/PDE/xpcom/docsrc";
		//String datapath = "D:/data/src/PDE/xpcom/docsrc";
		String docpath = args[0];
		String datapath = args[0];
		
		HashMap<String, HashMap<String, Java2JS>> jjsmap = new HashMap<String, HashMap<String,Java2JS>>();
		String mapfilepath = args[1];
        FileReader in = null;
        BufferedReader br = null;
		try {
			in = new FileReader(mapfilepath);
	        br = new BufferedReader(in);
	        String line;
	        while ((line = br.readLine()) != null) {
	            System.out.println(line);
	            String[] lines = line.split("\\s");
	            String idlname = lines[0].trim();
	            String type = lines[1].trim();
	            String javaprpname = lines[2].trim();
	            String jspropname = lines[3].trim();
	            
	            if(!jjsmap.containsKey(idlname))
	            {
	            	jjsmap.put(idlname, new HashMap<String, Java2JS>());
	            }     
	            jjsmap.get(idlname).put(javaprpname, new Java2JS(type, jspropname));
	            
	        }
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
	        try {
				if(br !=null) br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
	        	if(in !=null) in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}


		
		
		HashMap<String, JsElement> map = new HashMap<String, JsElement>();
		String path = docpath;
		File dir = new File(path);
	    File[] files = dir.listFiles();
		for (File file : files) {
			EasyDoclet doclet = new EasyDoclet(dir, file);
			RootDoc doc = doclet.getRootDoc();
			MyDoclet md = new MyDoclet("org.mozilla.interfaces");
			md.setMap(map);
			md.list(doc);
		}
		
		ReflectJava m = new ReflectJava();
		JsData data = m.makeXML(datapath, map, jjsmap);
		
		Serializer serializer = new Persister();
		File result = new File("tmp/text.xml");
		System.out.println("      " + result);
		try {
			serializer.write(data, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.exit(0);
	}

}
