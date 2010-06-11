package gef.example.helloworld.model;

public class PopupModel extends AbstractDataModel {

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		//super.installModelProperty();
		AddAttrTextProperty(ATTR_ID, ATTR_ID, "");
	}

	@Override
	public String getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "popup";
	}

}
