package gef.example.helloworld.model;

public class ListHeaderModel extends AbstractElementModel {

	public static final String LABEL = "label";
	//public static final String SHOW_HEADER = "header";
	
	public String getText(){
		return (String) getPropertyValue(LABEL);
	}
	
	public void setText(String text){
		setPropertyValue(LABEL, text);
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "listheader";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		
		//AddAttrBoolProperty(SHOW_HEADER, SHOW_HEADER, false);
		AddAttrTextProperty(LABEL, LABEL, "");
	}

}
