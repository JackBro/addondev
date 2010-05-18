package gef.example.helloworld.model;

public class MenuPopupBoxModel extends ContentsModel {
	public static final String ATTR_MENUPOPUP = "menupopup";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		MenuPopupModel popup = new MenuPopupModel();
		popup.setParent(this);
		AddListProperty(ATTR_MENUPOPUP, ATTR_MENUPOPUP, popup);
	}
}
