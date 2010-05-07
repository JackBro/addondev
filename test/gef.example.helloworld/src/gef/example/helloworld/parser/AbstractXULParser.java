package gef.example.helloworld.parser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gef.example.helloworld.model.ElementModel;

public abstract class AbstractXULParser {

	protected abstract ElementModel createModel();
	
	public ElementModel parse(Element e){
		ElementModel model = createModel();
		
		//parseAttribute(model, );
		parseChildren(model, e);
		
		return model;
	}
	
	private void parseAttribute(ElementModel model, Element e) {
		// TODO Auto-generated method stub
		
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
}
