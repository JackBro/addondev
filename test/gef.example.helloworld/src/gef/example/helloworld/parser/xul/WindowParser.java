package gef.example.helloworld.parser.xul;

import jp.aonir.fuzzyxml.FuzzyXMLElement;
import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.ScriptModel;
import gef.example.helloworld.model.WindowModel;

public class WindowParser extends AbstractXULParser {

	@Override
	protected AbstractElementModel createModel() {
		// TODO Auto-generated method stub
		return new WindowModel();
	}

	@Override
	protected void parseChildElement(AbstractElementModel model,
			FuzzyXMLElement e) {
		// TODO Auto-generated method stub
		if(e.getName().equals("script")){
			ScriptModel script = new ScriptModel();
			parseAttribute(script, e);
			((WindowModel)model).getScripts().add(script);
		}else{
			super.parseChildElement(model, e);
		}
	}

}
