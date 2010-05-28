package gef.example.helloworld.model;

import java.util.ArrayList;
import java.util.List;

public class ListItemModel extends AbstractElementModel {

	public static final String ATTR_LABEL = "label";
	public static final String LISTITEM_LISTCELL = "listitem_listcell";
	
	private List<ListCellModel> listcells;
	
	public List<ListCellModel> getListcells() {
		return listcells;
	}

	public String getText(){
		return (String) getPropertyValue(ATTR_LABEL);
	}
	
	public void setText(String text){
		setPropertyValue(ATTR_LABEL, text);
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "listitem";
	}
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		
		AddAttrProperty(ATTR_LABEL, ATTR_LABEL, "item");
		
		listcells = new ArrayList<ListCellModel>();
		AddListProperty(LISTITEM_LISTCELL, LISTITEM_LISTCELL, ListCellModel.class, listcells);
	}
}
