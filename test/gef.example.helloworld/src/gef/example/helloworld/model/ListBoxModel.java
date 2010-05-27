package gef.example.helloworld.model;

import java.util.List;

public class ListBoxModel extends AbstractElementModel {

	public static final String LISTCOLS = "listcols";
	
	private ListColsModel listcols;
	private List listitems;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "listbox";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		
		listcols = new ListColsModel();
		AddListProperty(LISTCOLS, "columes", this, ListColModel.class, listcols);
	}

}
