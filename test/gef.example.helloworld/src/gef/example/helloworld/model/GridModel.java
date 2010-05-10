package gef.example.helloworld.model;

import gef.example.helloworld.viewers.ListPropertyDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class GridModel extends ContentsModel {
	
	public static final String ATTR_COLUMS = "columns";
	//public static final String COLUMS_FLEX = "columnsflex";
	//protected String columnsflex="0,0";
	private static final int defaultcolumn = 2;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "grid";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddProperty(ATTR_COLUMS, new ListPropertyDescriptor(ATTR_COLUMS, ATTR_COLUMS), getDeafult());
		//AddProperty(COLUMS_FLEX, new TextPropertyDescriptor(COLUMS_FLEX, "columnsflex"), columnsflex);
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		StringBuilder buf= new StringBuilder();
		
		buf.append("<grid>\n");
		
		List children = getChildren();
		List<Map<String, String>> list = getColumns();
		int columns = list.size();
		//int rows =  children.size()/columns;
		int rows = children.size()%columns==0?children.size()/columns:children.size()/columns+1;
		
		buf.append("<columns>\n");
		for (Map<String, String> map : list) {
			//buf.append("<column " + map.get("flex") + " >\n");
			buf.append(String.format("<column %s=\"%s\" />\n", "flex", map.get("flex")));
		}
		buf.append("</columns>\n");
		
		buf.append("<rows>\n");
		for (int j = 0; j < rows; j++) {
			buf.append("<row>\n");
			for (int i = 0; i < columns; i++) {
				int index = j*rows+i;
				if(children.size()>index){
					ElementModel model = (ElementModel)children.get(index);
					buf.append(model.toXML());
				}
			}
			buf.append("</row>\n");
		}
		buf.append("</rows>\n");
	
		buf.append("</grid>\n");
		return buf.toString();
		
	}
	
	protected List<Map<String, String>> getDeafult(){
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		for (int i = 0; i < defaultcolumn; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("flex", "0");
			list.add(map);
		}
		
		return list;
	}
	
	protected List<Map<String, String>> getColumns(){
		Object obj = getPropertyValue(ATTR_COLUMS);
		return (List<Map<String, String>>)getPropertyValue(ATTR_COLUMS);
	}
	
	public List<Integer> getColumnFlex(){
		List<Integer> flexlist = new ArrayList<Integer>();

		List<Map<String, String>> list = getColumns();
		for (Map<String, String> map : list) {
			int flex = Integer.parseInt(map.get("flex"));
			flexlist.add(flex);
		}
		
		return flexlist;
	}
}
