package gef.example.helloworld.model;

public class MenuBaseModel extends ElementModel {

	public static final String CHANGE_MENU = "change_menu";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddMenuProperty(CHANGE_MENU, "menus", children);
	}

}
