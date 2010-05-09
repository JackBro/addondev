package gef.example.helloworld.parser;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gef.example.helloworld.model.ElementModel;

public abstract class AbstractXULParser {

	protected abstract ElementModel createModel();
	
	public ElementModel parse(Element e){
		ElementModel model = createModel();
		//model.installModelProperty();
		
		parseAttribute(model, e);
		parseChildren(model, e);
		
		return model;
	}
	
	private void parseAttribute(ElementModel model, Element e) {
		// TODO Auto-generated method stub
		NamedNodeMap attrs = e.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++) {
			Attr attr = (Attr) attrs.item(i);
			String id = attr.getName();
			String value = attr.getValue();
			model.setPropertyValue(id, value);
		}
	}
	
	protected void parseChildren(ElementModel model, Element e){
		NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				parseChildElement(model, (Element)node);
			}
		}
	}
	
	protected void parseChildElement(ElementModel model, Element e){
		XULLoader.parseElement(model, e);
	}
	
	protected Map<String, String> getAttribute(Element e){
		
		Map<String, String> map = new HashMap<String, String>();
		
		NamedNodeMap attrs = e.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++) {
			Attr attr = (Attr) attrs.item(i);
			String name = attr.getName();
			String value = attr.getValue();
			map.put(name, value);
		}
		
		return map;
	}
}
