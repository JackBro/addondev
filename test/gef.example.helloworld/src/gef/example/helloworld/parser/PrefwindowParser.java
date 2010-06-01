package gef.example.helloworld.parser;

import org.w3c.dom.Element;

import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.PrefwindowModel;

public class PrefwindowParser extends AbstractXULParser {

	@Override
	protected AbstractElementModel createModel() {
		// TODO Auto-generated method stub
		return new PrefwindowModel();
	}

	@Override
	protected void parseChildren(AbstractElementModel model, Element e) {
		// TODO Auto-generated method stub
		super.parseChildren(model, e);
	}

	@Override
	protected void parseChildElement(AbstractElementModel model, Element e) {
		// TODO Auto-generated method stub
		if(e.getNodeName().equals("script")){
			//model
		}else{
			super.parseChildElement(model, e);
		}
	}
}
