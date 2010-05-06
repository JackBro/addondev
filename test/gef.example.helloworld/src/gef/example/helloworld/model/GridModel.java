package gef.example.helloworld.model;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyColumnLabelProvider;

public class GridModel extends ContentsModel {
	
	public static final String COLUMS_FLEX = "columnsflex";
	protected String columnsflex="0,1";
	
	public GridModel() {
		super();
		// TODO Auto-generated constructor stub
		AddProperty(COLUMS_FLEX, new TextPropertyDescriptor(COLUMS_FLEX, "columnsflex"), columnsflex);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "grid";
	}
}
