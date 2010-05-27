package gef.example.helloworld.model;

public class ListHeaderModel extends AbstractElementModel {

	public static final String LABEL = "listheader_label";
	public static final String SHOW_HEADER = "header";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "listheader";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		
		AddBoolProperty(SHOW_HEADER, SHOW_HEADER, false);
		AddAttrProperty(LABEL, LABEL, "");
	}

}
