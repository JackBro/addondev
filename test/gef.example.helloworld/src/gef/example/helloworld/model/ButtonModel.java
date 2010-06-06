package gef.example.helloworld.model;

import org.eclipse.draw2d.IFigure;

import gef.example.helloworld.editparts.AbstractElementEditPart;
import gef.example.helloworld.viewers.KeyModifiersPropertyDescriptor;

public class ButtonModel extends AbstractElementModel {

	public static final String ATTR_LABEL = "label";
	public static final String ATTR_TYPE = "type";
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "button";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		//AddAttrBoolProperty("test","test", true);
		//AddProperty("test", new KeyModifiersPropertyDescriptor("test", "keytest"), "");
		String[] types = new String[]{"","checkbox", "menu", "menu-button", "radio", "repeat"};
		AddAttrStringsProperty(ATTR_TYPE, ATTR_TYPE, types, "");
	}

}
