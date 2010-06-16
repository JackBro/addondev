package gef.example.helloworld.model.parser;

import java.util.HashMap;
import java.util.Map;

import jp.aonir.fuzzyxml.FuzzyXMLAttribute;
import jp.aonir.fuzzyxml.FuzzyXMLElement;
import jp.aonir.fuzzyxml.FuzzyXMLNode;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gef.example.helloworld.model.AbstractElementModel;

public abstract class AbstractXULParser {

	protected abstract AbstractElementModel createModel();
	
	public AbstractElementModel parse(FuzzyXMLElement e){
		AbstractElementModel model = createModel();
		//model.installModelProperty();
		
		parseAttribute(model, e);
		parseChildren(model, e);
		
		return model;
	}
	
	protected void parseAttribute(AbstractElementModel model, FuzzyXMLElement e) {
		// TODO Auto-generated method stub
		FuzzyXMLAttribute[] attrs = e.getAttributes();
		for (int i = 0; i < attrs.length; i++) {
			FuzzyXMLAttribute attr = attrs[i];
			String id = attr.getName();
			String value = attr.getValue();
			model.setPropertyValue(id, value);
		}
	}
	
	protected void parseChildren(AbstractElementModel model, FuzzyXMLElement e){
		FuzzyXMLNode[] children = e.getChildren();
		for (int i = 0; i < children.length; i++) {
			FuzzyXMLNode node = children[i];
			
			//if (node instanceof Element) {
			//	parseChildElement(model, (Element)node);
			//}
			if (node instanceof FuzzyXMLElement) {
				parseChildElement(model, (FuzzyXMLElement)node);
			}
		}
	}
	
	protected void parseChildElement(AbstractElementModel model, FuzzyXMLElement e){
		XULLoader.parseElement(model, e);
	}
	
	protected Map<String, String> getAttribute(FuzzyXMLElement e){
		
		Map<String, String> map = new HashMap<String, String>();
		
		FuzzyXMLAttribute[] attrs = e.getAttributes();
		for (int i = 0; i < attrs.length; i++) {
			FuzzyXMLAttribute attr = attrs[i];
			String name = attr.getName();
			String value = attr.getValue();
			map.put(name, value);
		}
		
		return map;
	}
}
