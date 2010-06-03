package gef.example.helloworld.model;

public class PrefwindowModel extends WindowModel {

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
		//AddTabListProperty(fPrefPanesModel.getName(), "prefnanes", 
		//		this, PrefpaneModel.class, fPrefPanesModel.getChildren());
		
		AddListProperty("prefpanes", "prefpanes", 
				this, PrefpaneModel.class, getChildren());
	}
}
