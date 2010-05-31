package gef.example.helloworld.model;

import gef.example.helloworld.HelloworldPlugin;

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

	@Override
	public String getImage() {
		// TODO Auto-generated method stub
		return HelloworldPlugin.IMG_DUMMY;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "preferences";
	}

}
