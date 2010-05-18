package gef.example.helloworld.model;

import java.util.ArrayList;

import org.eclipse.ui.views.properties.PropertyDescriptor;

public class MenuPopupModel extends ElementModel {

	public static final String ATTR_MENUPOPUP = "menupopup";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "menupopup";
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "menupopup";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddListProperty(ATTR_MENUPOPUP, ATTR_MENUPOPUP, this);
	}
	
}
