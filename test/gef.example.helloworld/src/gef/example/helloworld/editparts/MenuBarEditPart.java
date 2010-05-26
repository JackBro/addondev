package gef.example.helloworld.editparts;

import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.MenuBaseModel;
import gef.example.helloworld.model.MenubarModel;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class MenuBarEditPart extends BoxEditPart {

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(MenuBaseModel.CHANGE_MENU)){
			MenubarModel model = (MenubarModel)getModel();
			model.removeAllChild();
			List newlist = (List)evt.getNewValue();
			for (Object object : newlist) {
				model.addChild((AbstractElementModel) object);
			}
		}
	}

}
