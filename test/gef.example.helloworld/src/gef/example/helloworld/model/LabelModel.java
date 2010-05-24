package gef.example.helloworld.model;

public class LabelModel extends AbstractElementModel {
	
	public static final String VALUE = "value";

	public String getText() {
		return getPropertyValue(VALUE).toString();
	}
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddAttrProperty(VALUE, VALUE, "Hello World");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "label";
	}
}
