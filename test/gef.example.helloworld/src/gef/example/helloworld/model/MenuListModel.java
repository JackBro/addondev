package gef.example.helloworld.model;

public class MenuListModel extends ElementModel {

	public static final String ATTR_MENUPOPUP = "menupopup";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "menulist";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		MenuPopupModel pupup = new MenuPopupModel();
		pupup.getChildren().add(new MenuItemModel());
		AddListProperty(ATTR_MENUPOPUP, ATTR_MENUPOPUP, pupup);
	}

}
