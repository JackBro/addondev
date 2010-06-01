package gef.example.helloworld.model;

import gef.example.helloworld.HelloworldPlugin;

import java.util.ArrayList;

import org.eclipse.ui.views.properties.PropertyDescriptor;

public class MenuPopupModel extends AbstractDataModel{// MenuBaseModel {
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "menupopup";
	}

	@Override
	public String getImage() {
		// TODO Auto-generated method stub
		return HelloworldPlugin.IMG_DUMMY;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "menupopup";
	}
}
