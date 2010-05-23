package gef.example.helloworld.model;

public class PreferenceModel extends AbstractElementModel {

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
		AddTextProperty(ATTR_ID, ATTR_ID, null);
		AddTextProperty(ATTR_TYPE, ATTR_TYPE, null);
	}
}
