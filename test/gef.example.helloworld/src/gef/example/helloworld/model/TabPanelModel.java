package gef.example.helloworld.model;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class TabPanelModel extends BoxModel {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "tabpanel";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddProperty("tabtest", new TextPropertyDescriptor("tabtest", "tabtest"), "OK");
	}

}
