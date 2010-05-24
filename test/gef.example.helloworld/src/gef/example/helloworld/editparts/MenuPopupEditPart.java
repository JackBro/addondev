package gef.example.helloworld.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import gef.example.helloworld.editpolicies.RootXYLayoutEditPolicy;
import gef.example.helloworld.model.*;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class MenuPopupEditPart extends DataElementEditPart {

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
//	protected void fireSelectionChanged() {
//		// TODO Auto-generated method stub
//		super.fireSelectionChanged();
//		if(!(getParent() instanceof XULPartEditPart))
//		{
//			EditPart part = (EditPart)getRoot().getChildren().get(0);
//			AbstractGraphicalEditPart xulpart = (AbstractGraphicalEditPart)part.getChildren().get(0);
//			int index = ((AbstractElementModel)xulpart.getModel()).getChildren().indexOf(getModel());
//			if(index != -1){
//				//xulpart.setSelected(index);
//				EditPart e = (EditPart) xulpart.getChildren().get(index);
//				if(getSelected() == SELECTED_NONE){
//					e.setSelected(SELECTED_NONE);
//				}else{
//					e.setSelected(SELECTED_PRIMARY);
//				}
//				//int sel = isS)?SELECTED_NONE:SELECTED_PRIMARY;
//				
//			}
//		}
//	}
	
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
