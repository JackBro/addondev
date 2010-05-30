package gef.example.helloworld.model;

import gef.example.helloworld.viewers.ListBoxItemPropertyDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ListBoxModel extends AbstractElementModel {

	public static final String LISTCOLS = "listbox_listcols";
	public static final String LISTITEMS = "listbox_listitems";
	
	//public static final String SHOW_HEADER = "listbox_show_header";
	
	private ListHeadModel listhead;
	private ListColsModel listcols;
	private List listitems;
	
	
	public ListHeadModel getListHead() {
		return listhead;
	}

	public void setListHead(ListHeadModel listhead) {
		this.listhead = listhead;
	}

	public void setListcols(ListColsModel listcols) {
		this.listcols = listcols;
		setPropertyValue(LISTCOLS, listcols);
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

	public void upDate(){
		listhead.getChildren().clear();
		for (Object obj : listcols.getChildren()) {
			ListColModel col = (ListColModel)obj;
			if(col.isHeader()){
				ListHeaderModel header = new ListHeaderModel();
				header.setText(col.getHeaderText());
				listhead.addChild(header);
			}
		}
	}
	
	public void upDateCol(){
		for (int i = 0; i < listcols.getChildren().size(); i++) {
			ListColModel col = (ListColModel) listcols.getChildren().get(i);
			col.setIsHeader(false);
		}
		
		for (int i = 0; i < listhead.getChildren().size(); i++) {
			ListHeaderModel header = (ListHeaderModel) listhead.getChildren().get(i);
			ListColModel col = (ListColModel) listcols.getChildren().get(i);
			col.setIsHeader(true);
			col.setHeaderText(header.getText());
		}
	}
	
	@Override
	public String getName() {
		return "listbox";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		
		listhead = new ListHeadModel();
		//AddBoolProperty(SHOW_HEADER, SHOW_HEADER, false);
		
		listcols = new ListColsModel();
		//AddListProperty(LISTCOLS, "columes", this, ListColModel.class, listcols);
		AddListProperty(LISTCOLS, "columes", ListColModel.class, listcols);
		
		listitems = new ArrayList();
		//AddListProperty(LISTITEMS, "items", this, ListItemModel.class, listitems);
		ListBoxItemPropertyDescriptor prop = new ListBoxItemPropertyDescriptor(LISTITEMS, LISTITEMS, this, ListItemModel.class);
		AddProperty(LISTITEMS, prop, listitems);
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		StringBuilder buf= new StringBuilder();
		buf.append("<" + getName() + ">\n");
		
		buf.append(listhead.toXML());
		buf.append(listcols.toXML());
		
		for (Object item : listitems) {
			buf.append(((ListItemModel)item).toXML());
		}
		
		buf.append("</" + getName() + ">\n");
		
		return buf.toString();
	}

}
