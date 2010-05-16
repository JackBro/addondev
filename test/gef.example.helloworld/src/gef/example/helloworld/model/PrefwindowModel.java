package gef.example.helloworld.model;

public class PrefwindowModel extends BoxModel {

	public static final String ATTR_TABS = "tabs";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "prefwindow";
	}
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		
		PrefPanesModel prefpanes = new PrefPanesModel();
		prefpanes.setParent(this);
		//prefpanes.setDefault();
		AddListProperty("prefnanes", "prefnanes", prefpanes);
	}
}
