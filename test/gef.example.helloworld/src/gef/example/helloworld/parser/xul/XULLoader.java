package gef.example.helloworld.parser.xul;

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

import jp.aonir.fuzzyxml.FuzzyXMLDocument;
import jp.aonir.fuzzyxml.FuzzyXMLElement;
import jp.aonir.fuzzyxml.FuzzyXMLParser;

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
		//parsers.put("window", new DefaultXULParser(WindowModel.class));
		parsers.put("overlay", new DefaultXULParser(OverlayModel.class));
		//parsers.put("prefwindow", new DefaultXULParser(PrefwindowModel.class));
		parsers.put("window", new WindowParser());
		parsers.put("prefwindow", new PrefwindowParser());
		parsers.put("prefwindow", new PrefwindowParser());
		
		parsers.put("button", new DefaultXULParser(ButtonModel.class));
		parsers.put("colorpicker", new DefaultXULParser(ColorPickerModel.class));
		//parsers.put("grid", new GridParser());
		parsers.put("label", new DefaultXULParser(LabelModel.class));
		parsers.put("vbox", new DefaultXULParser(VBoxModel.class));
		parsers.put("hbox", new DefaultXULParser(HBoxModel.class));
		parsers.put("statusbar", new DefaultXULParser(StatusbarModel.class));
		parsers.put("groupbox", new DefaultXULParser(GroupBoxModel.class));

		//parsers.put("listbox", new ListBoxParser());
		parsers.put("listheader", new DefaultXULParser(ListHeaderModel.class));
		parsers.put("listcol", new DefaultXULParser(ListColModel.class));
		parsers.put("listcell", new DefaultXULParser(ListCellModel.class));
		
		parsers.put("checkbox", new DefaultXULParser(CheckBoxModel.class));
		parsers.put("radiogroup", new DefaultXULParser(RadioGroupModel.class));
		parsers.put("radio", new DefaultXULParser(RadioModel.class));
		
		parsers.put("menupopup", new DefaultXULParser(MenuPopupModel.class));
		parsers.put("menuitem", new DefaultXULParser(MenuItemModel.class));
		//parsers.put("template", new TemplateParser());
		
		parsers.put("preferences", new DefaultXULParser(PreferencesModel.class));
		parsers.put("preference", new DefaultXULParser(PreferenceModel.class));
		
		parsers.put("keyset", new DefaultXULParser(KeySetModel.class));
		parsers.put("key", new DefaultXULParser(KeyModel.class));
		
		parsers.put("script", new DefaultXULParser(ScriptModel.class));
		
	}
	public static AbstractElementModel parseElement(AbstractElementModel root, FuzzyXMLElement e) {
		String elementName = e.getName();
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
			String xul = getContent(in);
			FuzzyXMLElement targetelement = null;
			FuzzyXMLParser parser = new FuzzyXMLParser();
			FuzzyXMLDocument document = parser.parse(xul);
			FuzzyXMLElement element = document.getDocumentElement();
			
			AbstractElementModel child = parseElement(root, (FuzzyXMLElement) element.getChildren()[0]);
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
