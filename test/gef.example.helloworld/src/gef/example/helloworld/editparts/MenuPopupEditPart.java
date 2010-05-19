package gef.example.helloworld.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import gef.example.helloworld.editpolicies.RootXYLayoutEditPolicy;
import gef.example.helloworld.model.*;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

public class MenuPopupEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Figure figure = new Figure();
		figure.setOpaque(true);
		figure.setBackgroundColor(ColorConstants.blue);
		figure.setPreferredSize(30, 30);
		
		return figure;
	}
	
//	@Override
//	public void propertyChange(PropertyChangeEvent evt) {
//		// TODO Auto-generated method stub
//		super.propertyChange(evt);
//		if(evt.getPropertyName().equals(MenuBaseModel.CHANGE_MENU)){
//			ElementModel model = (ElementModel)getModel();
//			MenuPopupModel popup = (MenuPopupModel)model.getPropertyValue(StatusbarModel.ATTR_MENUPOPUP);
//			popup.setChildren((List)evt.getNewValue());
//		}
//	}
}
