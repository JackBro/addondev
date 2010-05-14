package gef.example.helloworld.model;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class TabBoxModel extends BoxModel {
	
	public static final String ATTR_TABS = "tabs";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "tabbox";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
//		AddProperty(ATTR_ORIENT, 
//				new ComboBoxPropertyDescriptor(ATTR_ORIENT, ATTR_ORIENT, new String[] { "horizontal"}),
//				"horizontal");
		//AddTextProperty(ATTR_TABS, ATTR_TABS, "0");
		TabPanelsModel model = new TabPanelsModel();
		model.setParent(this);
		AddListProperty(ATTR_TABS, ATTR_TABS, model);
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		//return super.toXML();
		
		
		StringBuilder buf= new StringBuilder();
		buf.append("<tabbox>\n");
		
		buf.append("<tabs>\n");
		TabPanelsModel model = (TabPanelsModel)getPropertyValue(ATTR_TABS);
		for (Object tabpanel : model.getChildren()) {
			buf.append("<tab");
			buf.append(((ElementModel)tabpanel).getAttributes());
			buf.append("/>\n");
		}
		buf.append("</tabs>\n");
		
		buf.append("<tabpanels>\n");
		for (Object tabpanel : model.getChildren()) {
			buf.append(((ElementModel)tabpanel).toXML());
			buf.append("\n");
		}
		buf.append("</tabpanels>\n");
		
		buf.append("</tabbox>\n");
		return buf.toString(); 
	}

}
