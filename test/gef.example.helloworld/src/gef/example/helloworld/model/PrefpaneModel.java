package gef.example.helloworld.model;

public class PrefpaneModel extends BoxModel {

	public static final String ATTR_LABEL_TEXT = "label";
	public static final String ATTR_preference = "tabs";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "prefpane";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddAttrProperty(ATTR_LABEL_TEXT, ATTR_LABEL_TEXT, "OK");
		PreferencesModel prefs = new PreferencesModel();
		prefs.setParent(getParent());
		//AddListProperty(prefs.getName(), prefs.getName(), prefs);
	}

	public String getTabLabel(){
		return (String)getPropertyValue(ATTR_LABEL_TEXT);
	}
	
}
