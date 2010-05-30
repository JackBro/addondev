package gef.example.helloworld.model;

public class MenuItemModel extends AbstractElementModel {

	public static final String ATTR_LABEL = "label";
	public static final String ATTR_ONCOMMAND = "label";
	//public static final String ATTR_LABEL = "label";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "menuitem";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddAttrTextProperty(ATTR_LABEL, ATTR_LABEL, "item");
	}

}
