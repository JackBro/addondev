package gef.example.helloworld.model;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class TabPanelModel extends BoxModel {

	public static final String ATTR_LABEL_TEXT = "label";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "tabpanel";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddTextProperty(ATTR_LABEL_TEXT, ATTR_LABEL_TEXT, "OK");
	}
	
	@Override
	public String toXML() {
		StringBuilder buf= new StringBuilder();
		buf.append("<tabpanel>");
		for (Object obj : getChildren()) {
			buf.append(((AbstractElementModel)obj).toXML());
			buf.append("\n");
		}
		buf.append("</tabpanel>");
		return buf.toString(); 
	}
	
	public String getTabLabel(){
		return (String)getPropertyValue(ATTR_LABEL_TEXT);
	}
}
