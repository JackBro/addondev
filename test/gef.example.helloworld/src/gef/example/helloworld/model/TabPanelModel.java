package gef.example.helloworld.model;

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
		AddAttrTextProperty(ATTR_LABEL_TEXT, ATTR_LABEL_TEXT, "tab");
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "tabpanel";
	}
}
