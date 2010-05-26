package gef.example.helloworld.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class WindowModel extends BoxModel {
	
	public static final String ATTR_XMLNS = "xmlns"; 
	public static final String ATTR_TITLE = "title";
	public static final String SCRIPTS = "scripts";
	//public static final String XMLS = ;
	
	private List scripts;
	
	public WindowModel() {
		super();
		// TODO Auto-generated constructor stub
		//scripts = new ArrayList();
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
		AddAttrProperty(ATTR_XMLNS, ATTR_XMLNS, "http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul");
		AddAttrProperty(ATTR_TITLE, ATTR_TITLE, getName());
		
		if(scripts == null) scripts = new ArrayList();
		AddListProperty(SCRIPTS, SCRIPTS, ScriptModel.class, scripts);
	}	
}
