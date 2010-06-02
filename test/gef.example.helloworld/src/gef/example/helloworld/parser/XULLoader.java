package gef.example.helloworld.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;

import org.eclipse.core.resources.IFile;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.stylesheets.DocumentStyle;
import org.w3c.dom.stylesheets.StyleSheetList;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import gef.example.helloworld.model.*;

public class XULLoader {

	private static Map<String, AbstractXULParser> parsers = new HashMap<String, AbstractXULParser>();
	static {
		parsers.put("window", new DefaultXULParser(WindowModel.class));
		parsers.put("overlay", new DefaultXULParser(OverlayModel.class));
		parsers.put("prefwindow", new DefaultXULParser(PrefwindowModel.class));
		
		parsers.put("button", new DefaultXULParser(ButtonModel.class));
		parsers.put("colorpicker", new DefaultXULParser(ColorPickerModel.class));
		parsers.put("grid", new GridParser());
		parsers.put("label", new DefaultXULParser(LabelModel.class));
		parsers.put("vbox", new DefaultXULParser(VBoxModel.class));
		parsers.put("hbox", new DefaultXULParser(HBoxModel.class));
		parsers.put("statusbar", new DefaultXULParser(StatusbarModel.class));
		parsers.put("menuitem", new DefaultXULParser(MenuItemModel.class));

		parsers.put("listbox", new ListBoxParser());
		parsers.put("listheader", new DefaultXULParser(ListHeaderModel.class));
		parsers.put("listcol", new DefaultXULParser(ListColModel.class));
		parsers.put("listcell", new DefaultXULParser(ListCellModel.class));
		
		parsers.put("checkbox", new DefaultXULParser(CheckBoxModel.class));
		parsers.put("radiogroup", new DefaultXULParser(RadioGroupModel.class));
		parsers.put("radio", new DefaultXULParser(RadioModel.class));
		
		parsers.put("menupopup", new DefaultXULParser(MenuPopupModel.class));
		parsers.put("template", new TemplateParser());
		
		parsers.put("preferences", new DefaultXULParser(PreferencesModel.class));
		parsers.put("preference", new DefaultXULParser(PreferenceModel.class));
		
		parsers.put("keyset", new DefaultXULParser(KeySetModel.class));
		parsers.put("key", new DefaultXULParser(KeyModel.class));
		
	}
	public static AbstractElementModel parseElement(AbstractElementModel root, Element e) {
		String elementName = e.getTagName();
		AbstractXULParser parser = parsers.get(elementName);
		if(parser == null){
			//parser = new AnonymousParser(elementName);
		}
		if(parser !=null){
			AbstractElementModel model = null;
			if(root instanceof ContentsModel){
				model = parser.parse(e);
				((ContentsModel)root).addChild(model);

				return model;
			}else{
				model = parser.parse(e);
				if(root != null){
					root.addChild(model);
				}
				return model;
			}
		}
		return null;
	}
	
	public static AbstractElementModel loadXUL(InputStream in, ContentsModel root){
	//public static ElementModel loadXUL(IFile file, RootModel root){
		try {
			//String parseStr = getContent(file.getContents());
	        //InputStream is = null;
	        //is = new ByteArrayInputStream(parseStr.getBytes("UTF-8"));
	        
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			//dbfactory.setValidating(true);
			dbfactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder;
			builder = dbfactory.newDocumentBuilder();
			
			builder.setEntityResolver(new EntityResolver() {
				
				@Override
				public InputSource resolveEntity(String publicId, String systemId)
						throws SAXException, IOException {
					// TODO Auto-generated method stub
			           if(systemId.equals("chrome://bug489729/locale/bug489729.dtd")){
			        	   
			               InputStream reader = new FileInputStream("D:/data/src/PDE/xpi/bug489729-1.3-fx/chrome/bug489729/locale/ja/bug489729.dtd");
			               return new InputSource(reader);
			           }

					return null;
				}
			});
			builder.setErrorHandler(new ErrorHandler() {
				
				@Override
				public void warning(SAXParseException exception) throws SAXException {
					// TODO Auto-generated method stub
					int i=0;
					i++;					
				}
				
				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					// TODO Auto-generated method stub
					int i=0;
					i++;
				}
				
				@Override
				public void error(SAXParseException exception) throws SAXException {
					// TODO Auto-generated method stub
					int i=0;
					i++;					
				}
			});
			Document doc = builder.parse(in);
			String aa = document2String(doc);
			//DOMImplementation hh = doc.getImplementation();
			//StyleSheetList h = ((DocumentStyle) doc.getDocumentElement().getOwnerDocument()).getStyleSheets();
			//String aa = doc.getDocumentElement().toString();
			//DocumentStyle ds = (DocumentStyle)hh;
			//StyleSheetList h = ds.getStyleSheets();
			AbstractElementModel child = parseElement(root, doc.getDocumentElement());
			return child;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new RootModel();
		}			
	}
	
	public static String document2String(Document doc) {
		String string = null;
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory factory = TransformerFactory.newInstance();

		Transformer former;
		try {
		former = factory.newTransformer();
		former.transform(new DOMSource(doc.getDocumentElement()), result);
		string = result.getWriter().toString();
		} catch (TransformerConfigurationException e) {
		//_logger.warn(e);
		} catch (TransformerException e) {
		//_logger.warn(e);
		}
		return string;
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
