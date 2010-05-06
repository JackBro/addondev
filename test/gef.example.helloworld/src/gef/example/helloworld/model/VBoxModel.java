package gef.example.helloworld.model;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;

public class VBoxModel extends BoxModel {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "vbox";
	}
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddProperty(ATTR_ORIENT, 
				new ComboBoxPropertyDescriptor(ATTR_ORIENT, ATTR_ORIENT, new String[] { "vertical"}),
				"vertical");
	}

}
