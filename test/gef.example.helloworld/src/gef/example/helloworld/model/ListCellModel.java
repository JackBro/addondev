package gef.example.helloworld.model;

public class ListCellModel extends AbstractElementModel {

	public static final String LABEL = "label";
	
	public String getText(){
		return (String) getPropertyValue(LABEL);
	}

	public void setText(String value) {
		// TODO Auto-generated method stub
		setPropertyValue(LABEL, value);
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

		AddAttrTextProperty(LABEL, LABEL, "label");
	}

}
