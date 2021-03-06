package gef.example.helloworld.model;

import gef.example.helloworld.HelloworldPlugin;

public class KeySetModel extends AbstractDataModel {

	public static final String KEY = "key";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "keyset";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		
		AddListProperty(KEY, KEY, KeyModel.class, getChildren());
	}

	@Override
	public String getImage() {
		// TODO Auto-generated method stub
		return HelloworldPlugin.IMG_DUMMY;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "keyset";
	}

}
