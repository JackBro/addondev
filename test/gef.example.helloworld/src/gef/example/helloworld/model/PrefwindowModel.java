package gef.example.helloworld.model;

public class PrefwindowModel extends BoxModel {

	public static final String ATTR_TABS = "tabs";
	private PrefPanesModel fPrefPanesModel;
	
	public PrefPanesModel getPrefPanesModel() {
		return fPrefPanesModel;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "prefwindow";
	}
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		
		fPrefPanesModel = new PrefPanesModel();
		fPrefPanesModel.setParent(this);
		//prefpanes.setDefault();
		//AddListProperty("prefnanes", "prefnanes", prefpanes);
		AddTabListProperty(fPrefPanesModel.getName(), "prefnanes", 
				this, PrefpaneModel.class, fPrefPanesModel.getChildren());
	}
}
