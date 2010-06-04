package gef.example.helloworld.model;

public class RadioGroupModel extends BoxModel {

	@Override
	public String getName() {
		return "radiogroup";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		
		setPropertyValue(BoxModel.ATTR_ORIENT, "horizontal");
	}

}
