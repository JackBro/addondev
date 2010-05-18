package gef.example.helloworld.parser;

import org.w3c.dom.Element;

import gef.example.helloworld.model.ElementModel;

public class WindowParser extends AbstractXULParser {

	@Override
	protected ElementModel createModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void parseChildElement(ElementModel model, Element e) {
		// TODO Auto-generated method stub
		super.parseChildElement(model, e);
		
		if("menupopup".equals(e.getNodeName())){
			
		}
		
	}
}
