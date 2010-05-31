package gef.example.helloworld.model;

public class ScriptModel extends AbstractElementModel {

	public static final String ATTR_TYPE = "type";
	public static final String ATTR_SRC = "src";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "script";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		//super.installModelProperty();
		AddAttrTextProperty(ATTR_TYPE, ATTR_TYPE, "application/x-javascript");
		AddAttrTextProperty(ATTR_SRC, ATTR_SRC, "");
	}
}
