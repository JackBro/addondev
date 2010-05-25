package gef.example.helloworld.model;

public class CommandModel extends AbstractDataModel{
	
	public static final String ATTR_ONCOMMAND = "oncommand";
	
	@Override
	public String getName() {
		return "command";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddAttrProperty(ATTR_ONCOMMAND, ATTR_ONCOMMAND, "");
	}

}
