package gef.example.helloworld.model;

public class PrefpaneModel extends BoxModel {

	public static final String ATTR_LABEL_TEXT = "label";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "prefpane";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddTextProperty(ATTR_LABEL_TEXT, ATTR_LABEL_TEXT, "OK");
	}

	public String getTabLabel(){
		return (String)getPropertyValue(ATTR_LABEL_TEXT);
	}
}
