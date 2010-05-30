package gef.example.helloworld.model;

public class KeyModel extends AbstractDataModel {

	public static final String ATTR_COMMAND = "command";
	public static final String ATTR_DISABLED = "disabled";
	public static final String ATTR_KEY = "key";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "key";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddAttrTextProperty(ATTR_COMMAND, ATTR_COMMAND, "");
		AddAttrBoolProperty(ATTR_DISABLED, ATTR_DISABLED, false);
		
		String[] keys = {"",""};
		AddAttrStringsProperty(ATTR_KEY, ATTR_KEY, keys, "");
	}

}
