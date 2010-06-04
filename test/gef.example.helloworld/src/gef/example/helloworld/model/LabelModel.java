package gef.example.helloworld.model;

public class LabelModel extends AbstractElementModel implements ILabelModel {
	
	public static final String VALUE = "value";

	public String getText() {
		return getPropertyValue(VALUE).toString();
	}
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddAttrTextProperty(VALUE, VALUE, "label");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "label";
	}
}
