package jp.addondev.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jp.addondev.debug.core.model.AddonDebugTarget;
import jp.addondev.debug.core.model.JSStackFrame;
import jp.addondev.debug.core.model.JSThread;
import jp.addondev.debug.core.model.JSVariable;
import jp.addondev.debug.core.model.JSError;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
		public JSThread thread;
		public AddonDebugTarget target;
		//public List<JSStackFrame> stacks = new ArrayList<JSStackFrame>();
		public List<JSStackFrame> stacks;
		//public List<JSVariable> locals = new ArrayList<JSVariable>();
		
		public XMLToStackFrameInfo(AddonDebugTarget target) {
            this.target = target;
            stacks = new ArrayList<JSStackFrame>();
        }

		public void startElement(String uri, String localName, String qName, Attributes attributes){			
			if (qName.equals("stackframe")) {
				String depth = attributes.getValue("depth");
                String filename = attributes.getValue("filename");
				String functionname = attributes.getValue("functionname");
                String line = attributes.getValue("line");
                try {
					thread = (JSThread) target.getThreads()[0];
				} catch (DebugException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                try {
                    if (filename != null){
                    	//file:/D:/data/src/PDE/eclipsePDE/runtime-EclipseApplication/webtest/WebContent/index.html
                        //name = URLDecoder.decode(name, "UTF-8");
                    	//filename = ResourcePathUtil.getLocalFormUrl(URLDecoder.decode(filename, "UTF-8"));
                        //name = name.substring(6);
                    	//filename = target.URLDecode(filename);
                    	//URLEncoder.encode(arg0)
                    	//if(target.)
                    	String filepath = URLDecoder.decode(filename, "UTF-8");
                    	File file = new File(filepath);
                    	if(file.exists())
                    	{
	                    	filename = URLDecoder.decode(filename, "UTF-8");
	                    	IPath ipath = target.getAddonDevUtil().getPath(filepath);
	                    	filename = ipath.toOSString();
	                    	
	                    	JSStackFrame frame = new JSStackFrame(thread, target, depth, filename, functionname, line);
	                    	
	                    	stacks.add(frame);
                    	}
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
		}
	}
	static public JSStackFrame[] StackFramesFromXML(AddonDebugTarget target, String payload) throws CoreException {
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

		//JSStackFrame stackframe = info.stacks.get(0);
		//IVariable[] vals = (IVariable[]) info.locals.toArray(new JSVariable[0]);
		//stackframe.setVariables(vals);
		return (JSStackFrame[]) info.stacks.toArray(new JSStackFrame[0]);
	}
	
	
	
	/**
	 * 
	 * @author 
	 *
	 */
	static class XMLToVariableInfo extends DefaultHandler {
		public AddonDebugTarget target;
		public List<JSVariable> locals = new ArrayList<JSVariable>();
		private String stackFrameDepth;
		private String parent;
		
		public XMLToVariableInfo(AddonDebugTarget target, String stackFrameDepth, String parent) {
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
				locals.add(new JSVariable(target, stackFrameDepth, parent, name, type, value, hasChildren));
			}
//			else if(qName.equals("var"))
//			{
//                String name = attributes.getValue("name");
//                String type = attributes.getValue("type");
//                boolean hasChildren =  Boolean.parseBoolean(attributes.getValue("hasChildren"));
//                String value = null;
//				try {
//					value = URLDecoder.decode( attributes.getValue("value"), "UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}	
//				locals.add(new JSVariable(target, stackFrameDepth, parent, name, type, value, hasChildren));				
//			}
		}
	}
	static public IVariable[] VariablesFromXML(AddonDebugTarget target, String stackFrameID, String payload, String parent) throws CoreException {
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
		//return (JSVariable[]) info.locals.toArray(new JSVariable[0]);
		return info.locals.toArray(new JSVariable[0]);
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
}
