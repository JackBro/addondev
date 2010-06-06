package gef.example.helloworld.model;

import gef.example.helloworld.viewers.ElementComboBoxPropertyDescriptor;

public abstract class BoxModel extends ContentsModel {

	public static final String ATTR_ORIENT = "orient";
	
	public boolean isVertical(){
		return getPropertyValue(ATTR_ORIENT).equals("vertical");
	}
	
	@Override
	public void installModelProperty() {
		super.installModelProperty();
		
		ElementComboBoxPropertyDescriptor ecp = new ElementComboBoxPropertyDescriptor(ATTR_ORIENT, ATTR_ORIENT, 
				new String[] { "horizontal","vertical" });
		
		AddProperty(ATTR_ORIENT, ecp, "vertical");
	}
}
