package org.addondev.debug.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;


import org.addondev.debug.core.model.AddonDevDebugTarget;
import org.addondev.debug.core.model.AddonDevStackFrame;
import org.addondev.debug.core.model.AddonDevVariable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.org.apache.xpath.internal.XPathAPI;

public class XMLUtils {
	static SAXParserFactory parserFactory = SAXParserFactory.newInstance();
	
	static SAXParser getSAXParser() throws ParserConfigurationException, SAXException {
		SAXParser parser = null;
		 synchronized(parserFactory) {
			 parser = parserFactory.newSAXParser();
		 }
		 return parser;
	}
	
	/**
	 * 
	 * @author sin
	 *
	 */
	static class XMLToStackFrameInfo extends DefaultHandler{
		public IThread thread;
		public AddonDevDebugTarget target;
		//public List<JSStackFrame> stacks = new ArrayList<JSStackFrame>();
		public List<AddonDevStackFrame> stacks;
		//public List<JSVariable> locals = new ArrayList<JSVariable>();
		
		public XMLToStackFrameInfo(AddonDevDebugTarget target) {
            this.target = target;
            stacks = new ArrayList<AddonDevStackFrame>();
        }

		public void startElement(String uri, String localName, String qName, Attributes attributes){			
			if (qName.equals("stackframe")) {
				String depth = attributes.getValue("depth");
				String url = attributes.getValue("url");
                String filename = attributes.getValue("filename");
				String functionname = attributes.getValue("functionname");
                String line = attributes.getValue("line");
                String fn = attributes.getValue("fn");
                try {
					thread = (IThread) target.getThreads()[0];
				} catch (DebugException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                try {
                    if (url != null && filename != null){
                    	//file:/D:/data/src/PDE/eclipsePDE/runtime-EclipseApplication/webtest/WebContent/index.html
                        //name = URLDecoder.decode(name, "UTF-8");
                    	//filename = ResourcePathUtil.getLocalFormUrl(URLDecoder.decode(filename, "UTF-8"));
                        //name = name.substring(6);
                    	//filename = target.URLDecode(filename);
                    	//URLEncoder.encode(arg0)
                    	
                    	String deURL = URLDecoder.decode(url, "UTF-8");
                    	String defn = null;
                    	if(fn.length() > 0)
                    		defn = URLDecoder.decode(fn, "UTF-8");
                    	//String filepath = URLDecoder.decode(filename, "UTF-8");
                    	//File file = new File(filepath);
                    	//if(file.exists())
                    	//{
	                    	filename = URLDecoder.decode(filename, "UTF-8");
	                    	IPath ipath = new Path(filename);
	                    	filename = ipath.toOSString();
	                    	
	                    	AddonDevStackFrame frame = new AddonDevStackFrame(thread, target, depth, deURL, filename, functionname, line, defn);
	                    	
	                    	stacks.add(frame);
                    	//}
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
		}
	}
	static public AddonDevStackFrame[] stackFramesFromXML(AddonDevDebugTarget target, String payload) throws CoreException {
        SAXParser parser = null;
		try {
			parser = getSAXParser();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        XMLToStackFrameInfo info = new XMLToStackFrameInfo(target);
        try {
			//parser.parse(new ByteArrayInputStream(payload.getBytes()), info);
			parser.parse(new ByteArrayInputStream(payload.trim().getBytes("UTF-8")), info);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (AddonDevStackFrame[]) info.stacks.toArray(new AddonDevStackFrame[0]);
	}
	
	
	
	/**
	 * 
	 * @author 
	 *
	 */
	static class XMLToVariableInfo extends DefaultHandler {
		public AddonDevDebugTarget target;
		public ArrayList<IVariable> locals = new ArrayList<IVariable>();
		private String stackFrameDepth;
		private String parent;
		
		public XMLToVariableInfo(AddonDevDebugTarget target, String stackFrameDepth, String parent) {
            this.target = target;
            this.stackFrameDepth = stackFrameDepth;
            this.parent = parent;
        }
		
		public void startElement(String uri, String localName, String qName, Attributes attributes){			
			if (qName.equals("value")) {
                String name = attributes.getValue("name");
                String type = attributes.getValue("type");
                boolean hasChildren =  Boolean.parseBoolean(attributes.getValue("hasChildren"));
                String value = null;
				try {
					value = URLDecoder.decode( attributes.getValue("value"), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				locals.add(new AddonDevVariable(target, stackFrameDepth, parent, name, type, value, hasChildren));
			}
		}
	}
	static public ArrayList<IVariable> variablesFromXML(AddonDevDebugTarget target, String stackFrameID, String payload, String parent) throws CoreException {
        SAXParser parser = null;
		try {
			parser = getSAXParser();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XMLToVariableInfo info = new XMLToVariableInfo(target, stackFrameID, parent);
        try {
        	
			//parser.parse(new ByteArrayInputStream(payload.trim().getBytes("UTF-8")), info);
        	parser.parse(new ByteArrayInputStream(payload.getBytes()), info);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info.locals;
	}
	

	
	
	/**
	 * 
	 * @author 
	 *
	 */
	static class XMLdata extends DefaultHandler {
		//public List<JsError> errors = new ArrayList<JsError>();
		public List<Map<String, String>> mapList;
		
		public XMLdata() {
			mapList = new ArrayList<Map<String,String>>();
        }
		
		public void startElement(String uri, String localName, String qName, Attributes attributes){	
			if(!qName.equals("xml"))
			{
				Map<String, String> map = new HashMap<String, String>();
				
				int attrcnt = attributes.getLength();
				for (int i = 0; i < attrcnt; i++) {
					String key = attributes.getQName(i);
					String value = attributes.getValue(i);
					map.put(key, value);
				}
				mapList.add(map);
			}
		}
	}	
	static public List<Map<String, String>> getDataFromXML(String payload) throws ParserConfigurationException, SAXException, IOException {
        SAXParser parser = getSAXParser();
		XMLdata data = new XMLdata();
		parser.parse(new ByteArrayInputStream(payload.trim().getBytes("UTF-8")), data);
		return data.mapList;
	}
	
	
	//
	public static String getAddonIDFromRDF(InputStream input) throws ParserConfigurationException, SAXException, IOException, TransformerException
	{
		
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = fact.newDocumentBuilder();
		Document doc = builder.parse(input);

        Node n;		        
        String id = null;
        NodeIterator nl = XPathAPI.selectNodeIterator(doc, "/RDF/Description/*");
        while ((n = nl.nextNode()) != null) {
            if(n.getNodeName().equals("em:id"))
            {
            	id = n.getTextContent();
            	break;
            }
        }              
        return id;		
	}
}
