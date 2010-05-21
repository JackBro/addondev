package gef.example.helloworld.model;

public class StatusbarModel extends ContentsModel {
	public static final String ATTR_MENUPOPUP = "menupopup";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "statusbar";
	}
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		//MenuPopupModel popup = new MenuPopupModel();
		//popup.setParent(this);
		//popup.getChildren().add(new MenuItemModel());
		//AddListProperty(ATTR_MENUPOPUP, ATTR_MENUPOPUP, popup);
		//AddProperty(ATTR_MENUPOPUP, new PropertyDescriptor(ATTR_MENUPOPUP, ATTR_MENUPOPUP), popup);
	}
}
