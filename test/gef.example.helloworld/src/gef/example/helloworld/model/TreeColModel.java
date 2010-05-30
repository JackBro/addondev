package gef.example.helloworld.model;

public class TreeColModel extends AbstractElementModel {

	public static final String ATTR_LABEL = "label";
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "treecol";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddAttrTextProperty(ATTR_LABEL, ATTR_LABEL, "col");
	}
}
