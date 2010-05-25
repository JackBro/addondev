package gef.example.helloworld.model;

public class PreferencesModel extends AbstractDataModel {
	
	public static final String CHANGE_PREFERENCE = "change_preference";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "preferences";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		//super.installModelProperty();
		AddListProperty(CHANGE_PREFERENCE, "preference", PreferenceModel.class, children);
	}

}
