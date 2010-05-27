package gef.example.helloworld.parser;

import java.util.Map.Entry;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.ModelProperty;
import gef.example.helloworld.model.TemplateModel;

public class TemplateParser extends AbstractXULParser {

	private StringBuilder sb;
	
	@Override
	protected AbstractElementModel createModel() {
		// TODO Auto-generated method stub
		return new TemplateModel();
	}

	@Override
	public AbstractElementModel parse(Element e) {
		TemplateModel model = (TemplateModel)createModel();
		
		parseAttribute(model, e);
		
		sb = new StringBuilder();
		parseChildren(model, e);
		
		model.setText(sb.toString());
		
		return model;
	}

	@Override
	protected void parseChildren(AbstractElementModel model, Element e) {
		// TODO Auto-generated method stub
//		String text = e.getTextContent();
//		String ee = e.toString();
//		Node node = (Node)e;
//		String ns = node.toString();
		//super.parseChildren(model, e);

		NodeList children = e.getChildNodes();
		sb.append(toText(e));
		if(children.getLength() > 0){
			sb.append(">\n");
			for (int i = 0; i < children.getLength(); i++) {
				
				Node node = children.item(i);
				if (node instanceof Element) {
					parseChildren(model, (Element)node);
				}
			}
			sb.append(String.format("</%s>\n", e.getNodeName()));
		}else{
			sb.append("/>\n");
		}
	}
	
	private String toText(Element e){
	
		StringBuilder buf= new StringBuilder();
		String nodename = e.getNodeName();		
		buf.append(String.format("<%s", nodename));
		
		NamedNodeMap attrs = e.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++) {
			Attr attr = (Attr) attrs.item(i);
			String name = attr.getName();
			String value = attr.getValue();
			buf.append(String.format(" %s=\"%s\" ", name, value));
		}
	
		return buf.toString();	
	}

}
