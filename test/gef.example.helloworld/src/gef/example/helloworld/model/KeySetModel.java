package gef.example.helloworld.model;

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

}
