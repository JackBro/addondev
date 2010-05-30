package gef.example.helloworld.model;

public class PreferenceModel extends AbstractDataModel {

	public static final String ATTR_NAME = "name";
	public static final String ATTR_ID = "id";
	public static final String ATTR_TYPE = "type";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "preference";
	}
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		//super.installModelProperty();
		AddAttrTextProperty(ATTR_NAME, ATTR_NAME, "");
		AddAttrTextProperty(ATTR_ID, ATTR_ID, "");
		AddAttrTextProperty(ATTR_TYPE, ATTR_TYPE, "");
	}
}
