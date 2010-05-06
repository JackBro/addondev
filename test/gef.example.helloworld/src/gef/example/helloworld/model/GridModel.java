package gef.example.helloworld.model;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class GridModel extends ContentsModel {
	
	public static final String COLUMS_FLEX = "columnsflex";
	protected String columnsflex="0,0";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "grid";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddProperty(COLUMS_FLEX, new TextPropertyDescriptor(COLUMS_FLEX, "columnsflex"), columnsflex);
	}
}
