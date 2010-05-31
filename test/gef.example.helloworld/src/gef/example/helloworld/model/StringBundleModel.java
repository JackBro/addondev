package gef.example.helloworld.model;

public class StringBundleModel extends AbstractElementModel {

	public static final String ATTR_SRC = "src";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "stringbundle";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddAttrTextProperty(ATTR_SRC, ATTR_SRC, "");
	}

}
