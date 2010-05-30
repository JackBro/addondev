package gef.example.helloworld.model;

import java.util.ArrayList;
import java.util.List;

public class ListItemModel extends AbstractElementModel {

	public static final String ATTR_LABEL = "label";
	public static final String ATTR_VALUE = "value";
	public static final String LISTITEM_LISTCELL = "listitem_listcell";
	
	//private List<ListCellModel> listcells;
	
	public List<ListCellModel> getListcells() {
		return (List<ListCellModel>)getChildren();
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
		
		AddAttrTextProperty(ATTR_LABEL, ATTR_LABEL, "item");
		AddAttrTextProperty(ATTR_VALUE, ATTR_VALUE, "");
		
		//listcells = new ArrayList<ListCellModel>();
		//AddListProperty(LISTITEM_LISTCELL, LISTITEM_LISTCELL, ListCellModel.class, getChildren());
	}
}
