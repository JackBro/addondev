package gef.example.helloworld.parser.xul;

import jp.aonir.fuzzyxml.FuzzyXMLElement;

import org.w3c.dom.Element;

import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.PrefpaneModel;
import gef.example.helloworld.model.PrefwindowModel;
import gef.example.helloworld.model.ScriptModel;
import gef.example.helloworld.model.WindowModel;

public class PrefwindowParser extends WindowParser {

	@Override
	protected AbstractElementModel createModel() {
		// TODO Auto-generated method stub
		return new PrefwindowModel();
	}

//	@Override
//	protected void parseChildElement(AbstractElementModel model,
//			FuzzyXMLElement e) {
//		// TODO Auto-generated method stub
//		if(e.getName().equals("prefpane")){
//			PrefpaneModel prefpane = new PrefpaneModel();
//			parseAttribute(prefpane, e);
//			parseChildren(prefpane, e);
//			((PrefwindowModel)model).getPrefPanesModel().addChild(prefpane);
//			//((PrefwindowModel)model).firePropertyChange("prefnanes", null, ((PrefwindowModel)model).getPrefPanesModel().getChildren());
//			//((PrefwindowModel)model).addChild(prefpane);
//		}else{
//			super.parseChildElement(model, e);
//		}
//	}
	
	
}
