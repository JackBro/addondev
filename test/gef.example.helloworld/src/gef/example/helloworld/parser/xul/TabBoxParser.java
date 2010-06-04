package gef.example.helloworld.parser.xul;

import jp.aonir.fuzzyxml.FuzzyXMLElement;
import jp.aonir.fuzzyxml.FuzzyXMLNode;
import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.TabBoxModel;

public class TabBoxParser extends AbstractXULParser {

	@Override
	protected AbstractElementModel createModel() {
		// TODO Auto-generated method stub
		return new TabBoxModel();
	}

	@Override
	protected void parseChildElement(AbstractElementModel model,
			FuzzyXMLElement e) {
		// TODO Auto-generated method stub
		if(e.getName().equals("tabpanels")){
			for (FuzzyXMLNode node : e.getChildren()) {
				if(node instanceof FuzzyXMLElement)
					super.parseChildElement(model, (FuzzyXMLElement) node);
			}
		}else{
			super.parseChildElement(model, e);
		}
	}

}
