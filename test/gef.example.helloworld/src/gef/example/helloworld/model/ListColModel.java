package gef.example.helloworld.model;

public class ListColModel extends AbstractElementModel {
	
	
	//public static final String LIST_HEADER = "listheader";
	public static final String SHOW_HEADER = "show_header";
	public static final String HEADER_TEXT = "header_text";
	
	//private ListHeaderModel listheader;

	public boolean isHeader(){
		String h = (String) getPropertyValue(SHOW_HEADER);
		return Boolean.parseBoolean(h);
	}
	public void setIsHeader(boolean isheader){
		setPropertyValue(SHOW_HEADER, String.valueOf(isheader));
	}
	
	public String getHeaderText(){
		return (String) getPropertyValue(HEADER_TEXT);
	}
	
	public void setHeaderText(String text){
		setPropertyValue(HEADER_TEXT, text);
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "listcol";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddBoolProperty(SHOW_HEADER, SHOW_HEADER, false);
		AddTextProperty(HEADER_TEXT, HEADER_TEXT, "");
		AddAttrBoolProperty("test","test", true);
		//listheader = new ListHeaderModel();
		//AddConstProperty(LIST_HEADER, "header", listheader);
	}

}
