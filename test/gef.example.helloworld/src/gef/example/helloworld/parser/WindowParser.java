package gef.example.helloworld.parser;

import org.w3c.dom.Element;

import gef.example.helloworld.model.AbstractElementModel;

public class WindowParser extends AbstractXULParser {

	@Override
	protected AbstractElementModel createModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void parseChildElement(AbstractElementModel model, Element e) {
		// TODO Auto-generated method stub
		super.parseChildElement(model, e);
		
		if("menupopup".equals(e.getNodeName())){
			
		}
		
	}
}
