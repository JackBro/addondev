package gef.example.helloworld.model;

public class ColorPickerModel extends AbstractElementModel {

	public static final String ATTR_DISABLED = "disabled";
	public static final String ATTR_COLOR = "color";
	public static final String ATTR_ONCHANGE = "onchange";
	public static final String ATTR_TYPE = "type";
	
	static String[] types= new String[]{"","button"};
	
	public boolean isButton(){
		String type = (String) getPropertyValue(ATTR_TYPE);
		return type.equals("button");
	}
	
	public void setIsButton(boolean isbutton){
		setPropertyValue(ATTR_TYPE, String.valueOf(isbutton));
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "colorpicker";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddAttrBoolProperty(ATTR_DISABLED, ATTR_DISABLED, false);
		AddAttrMultiLineTextProperty(ATTR_ONCHANGE, ATTR_ONCHANGE, "");
		AddAttrStringsProperty(ATTR_TYPE, ATTR_TYPE, types, "button");
	}

}
