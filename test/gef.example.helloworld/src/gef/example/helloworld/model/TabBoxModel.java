package gef.example.helloworld.model;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class TabBoxModel extends BoxModel {
	
	public static final String ATTR_TABS = "tabs";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "tabbox";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
//		AddProperty(ATTR_ORIENT, 
//				new ComboBoxPropertyDescriptor(ATTR_ORIENT, ATTR_ORIENT, new String[] { "horizontal"}),
//				"horizontal");
		AddProperty(ATTR_TABS, new TextPropertyDescriptor(ATTR_TABS, ATTR_TABS), "0");
	}

}
