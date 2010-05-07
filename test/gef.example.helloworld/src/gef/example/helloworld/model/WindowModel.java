package gef.example.helloworld.model;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class WindowModel extends ContentsModel {
	
	public static final String ATTR_XMLNS = "xmlns"; 
	public static final String ATTR_TITLE = "title";
	//public static final String XMLS = ;
	
	public WindowModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "window";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddProperty(ATTR_XMLNS, new TextPropertyDescriptor(ATTR_XMLNS, ATTR_XMLNS), "http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul");
		AddProperty(ATTR_TITLE, new TextPropertyDescriptor(ATTR_TITLE, ATTR_TITLE), getName());
	}	
}
