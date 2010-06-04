package gef.example.helloworld.parser.xul;

import java.util.Map.Entry;

import jp.aonir.fuzzyxml.FuzzyXMLAttribute;
import jp.aonir.fuzzyxml.FuzzyXMLElement;
import jp.aonir.fuzzyxml.FuzzyXMLNode;


import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.TemplateModel;

public class TemplateParser extends AbstractXULParser {

	private StringBuilder sb;
	
	@Override
	protected AbstractElementModel createModel() {
		// TODO Auto-generated method stub
		return new TemplateModel();
	}

	@Override
	public AbstractElementModel parse(FuzzyXMLElement e) {
		TemplateModel model = (TemplateModel)createModel();
		
		parseAttribute(model, e);
		
		sb = new StringBuilder();
		parseChildren(model, e);
		
		model.setText(sb.toString());
		
		return model;
	}

	@Override
	protected void parseChildren(AbstractElementModel model, FuzzyXMLElement e) {
		// TODO Auto-generated method stub
		FuzzyXMLNode[] children = e.getChildren();
		sb.append(toText(e));
		if(children.length > 0){
			sb.append(">\n");
			for (int i = 0; i < children.length; i++) {
				
				FuzzyXMLNode node = children[i];
				if (node instanceof FuzzyXMLElement) {
					parseChildren(model, (FuzzyXMLElement)node);
				}
			}
			sb.append(String.format("</%s>\n", e.getName()));
		}else{
			sb.append("/>\n");
		}
	}
	
	private String toText(FuzzyXMLElement e){
	
		StringBuilder buf= new StringBuilder();
		String nodename = e.getName();		
		buf.append(String.format("<%s", nodename));
		
		FuzzyXMLAttribute[] attrs = e.getAttributes();
		for (int i = 0; i < attrs.length; i++) {
			FuzzyXMLAttribute attr = attrs[i];
			String name = attr.getName();
			String value = attr.getValue();
			buf.append(String.format(" %s=\"%s\" ", name, value));
		}
	
		return buf.toString();	
	}

}
