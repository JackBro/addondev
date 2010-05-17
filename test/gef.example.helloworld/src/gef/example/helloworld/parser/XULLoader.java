package gef.example.helloworld.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.HBoxModel;
import gef.example.helloworld.model.LabelModel;
import gef.example.helloworld.model.PrefwindowModel;
import gef.example.helloworld.model.RootModel;
import gef.example.helloworld.model.VBoxModel;
import gef.example.helloworld.model.WindowModel;
import gef.example.helloworld.model.GridModel;

public class XULLoader {

	private static Map<String, AbstractXULParser> parsers = new HashMap<String, AbstractXULParser>();
	static {
		parsers.put("window", new DefaultXULParser(WindowModel.class));
		parsers.put("prefwindow", new DefaultXULParser(PrefwindowModel.class));
		parsers.put("grid", new GridParser());
		parsers.put("label", new DefaultXULParser(LabelModel.class));
		parsers.put("vbox", new DefaultXULParser(VBoxModel.class));
		parsers.put("hbox", new DefaultXULParser(HBoxModel.class));	
	}
	public static ElementModel parseElement(ElementModel root, Element e) {
		String elementName = e.getTagName();
		AbstractXULParser parser = parsers.get(elementName);
		if(parser !=null){
			ElementModel model = null;
			if(root instanceof ContentsModel){
				model = parser.parse(e);
				((ContentsModel)root).addChild(model);

				return model;
			}else{
				model = parser.parse(e);
				if(root != null){
					
				}
				return model;
			}
		}
		return null;
	}
	
	public static ElementModel loadXUL(InputStream in, ContentsModel root){
	//public static ElementModel loadXUL(IFile file, RootModel root){
		try {
			//String parseStr = getContent(file.getContents());
	        //InputStream is = null;
	        //is = new ByteArrayInputStream(parseStr.getBytes("UTF-8"));
	        
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = dbfactory.newDocumentBuilder();
			Document doc = builder.parse(in);

			ElementModel child = parseElement(root, doc.getDocumentElement());
			return child;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new RootModel();
		}			
	}
	
	public static String getContent(InputStream input) throws IOException
	{			
		String res = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buf = new byte[1024 * 8];
		try {
			while((len = input.read(buf))!=-1){
				out.write(buf,0,len);
			}
			res = out.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try {
				if(input !=null) input.close();
				if(out !=null) out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		return res;
	}
}
