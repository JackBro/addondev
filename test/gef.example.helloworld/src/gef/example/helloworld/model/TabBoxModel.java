package gef.example.helloworld.model;

import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class TabBoxModel extends BoxModel {
	
	public static final String ATTR_TABS = "tabs";
	public static final String TABPANELS = "tabpanels";
	private TabPanelsModel fTabPanelsModel;

	public TabPanelsModel getTabPanelsModel() {
		return fTabPanelsModel;
	}

	@Override
	public String getName() {
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
		//fTabPanelsModel = new TabPanelsModel();
		//fTabPanelsModel.setParent(this);
		AddListProperty(TABPANELS, TABPANELS, this, TabPanelModel.class, getChildren());
		//AddTabListProperty(fTabPanelsModel.getName(), fTabPanelsModel.getName(), 
		//		this, TabPanelModel.class, fTabPanelsModel.getChildren());
	}

	@Override
	public String toXML() {	
		StringBuilder buf= new StringBuilder();
		buf.append("<tabbox>\n");
		
		buf.append("<tabs>\n");
		TabPanelsModel model = fTabPanelsModel;//(TabPanelsModel)getPropertyValue(ATTR_TABS);
		for (Object tabpanel : model.getChildren()) {
			buf.append("<tab");
			buf.append(((AbstractElementModel)tabpanel).getAttributes());
			buf.append("/>\n");
		}
		buf.append("</tabs>\n");
		
		buf.append("<tabpanels>\n");
		for (Object tabpanel : model.getChildren()) {
			buf.append(((AbstractElementModel)tabpanel).toXML());
			buf.append("\n");
		}
		buf.append("</tabpanels>\n");
		
		buf.append("</tabbox>\n");
		return buf.toString(); 
	}

}
