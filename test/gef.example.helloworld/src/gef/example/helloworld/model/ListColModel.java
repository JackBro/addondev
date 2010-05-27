package gef.example.helloworld.model;

public class ListColModel extends AbstractElementModel {
	
	public static final String LIST_HEADER = "listheader";
	
	private ListHeaderModel listheader;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "listcol";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		listheader = new ListHeaderModel();
		AddConstProperty(LIST_HEADER, "header", listheader);
	}

}
