package gef.example.helloworld.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;

public class MenubarModel extends BoxModel {

	//private List menus;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "menubar";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddAttrProperty(ATTR_ORIENT, ATTR_ORIENT, "horizontal");
		//menus = new ArrayList();
		AddMenuProperty(MenuBaseModel.CHANGE_MENU, "menu", this, children);
	}

}
