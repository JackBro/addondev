package gef.example.helloworld.model;

import gef.example.helloworld.viewers.ListBoxItemPropertyDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ListBoxModel extends BoxModel {

	public static final String LISTCOLS = "listbox_listcols";
	public static final String LISTITEMS = "listbox_listitems";
	
	private ListColsModel listcols;
	private List listitems;
	
	
	public void setListcols(ListColsModel listcols) {
		this.listcols = listcols;
		//setPropertyValue(LISTCOLS, listcols);
	}

	public ListColsModel getListcols() {
		return listcols;
	}

	public void setListitems(List listitems) {
		this.listitems = listitems;
		//setPropertyValue(LISTITEMS, listitems, false);
		//AddListProperty(LISTITEMS, "items", this, ListItemModel.class, listitems);
	}
	
	public List getListitems() {
		return listitems;
	}

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
		
		listitems = new ArrayList();
		//AddListProperty(LISTITEMS, "items", this, ListItemModel.class, listitems);
		ListBoxItemPropertyDescriptor prop = new ListBoxItemPropertyDescriptor(LISTITEMS, LISTITEMS, this, ListItemModel.class);
		AddProperty(LISTITEMS, prop, listitems);
	}

}
