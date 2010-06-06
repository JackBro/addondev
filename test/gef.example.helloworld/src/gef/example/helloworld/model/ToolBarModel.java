package gef.example.helloworld.model;

public class ToolBarModel extends BoxModel {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "toolbar";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		
		setPropertyValue(BoxModel.ATTR_ORIENT, "horizontal");
	}

}
