package gef.example.helloworld.model;

public class ListCellModel extends AbstractElementModel {

	public static final String LABEL = "listcell_label";
	
	public String getText(){
		return (String) getPropertyValue(LABEL);
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "listcell";
	}
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();

		AddAttrProperty(LABEL, LABEL, "");
	}
}
