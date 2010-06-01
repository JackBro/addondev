package gef.example.helloworld.model;

public class SeparatorModel extends AbstractElementModel {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "separator";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		setPropertyValue(ATTR_CLASS, "groove");
	}

}
