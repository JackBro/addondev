package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.MenuBaseModel;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public abstract class ContentsEditPart extends EditPartWithListener {

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new BoxLayoutEditPolicy());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ContentsModel.P_ADD_CHILDREN)
				|| evt.getPropertyName().equals(ContentsModel.P_REMOVE_CHILDREN)) {
			refreshChildren();
		}
	}

	@Override
	protected void addChild(EditPart child, int index) {
		// TODO Auto-generated method stub

		super.addChild(child, index);
		//super.addChild(child, index-cnt);
	}

	int cnt = 0;
	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		// TODO Auto-generated method stub
//		if(childEditPart.getModel() instanceof MenuBaseModel){
//			EditPart part = (EditPart)getRoot().getChildren().get(0);
//			AbstractGraphicalEditPart xulpart = (AbstractGraphicalEditPart)part.getChildren().get(0);
//			//xulpart.getFigure().add(((AbstractGraphicalEditPart)childEditPart).getFigure());
//			if(((ContentsModel)xulpart.getModel()).getChildIndex(childEditPart.getModel()) == -1)
//				((ContentsModel)xulpart.getModel()).addChild((ElementModel) childEditPart.getModel());			
//		}
		if(childEditPart.getModel() instanceof MenuBaseModel){
			EditPart part = (EditPart)getRoot().getChildren().get(0);
			AbstractGraphicalEditPart xulpart = (AbstractGraphicalEditPart)part.getChildren().get(0);
			xulpart.getFigure().add(((AbstractGraphicalEditPart)childEditPart).getFigure());
			//((ContentsModel)xulpart.getModel()).addChild((ElementModel) childEditPart.getModel());
			((AbstractGraphicalEditPart)childEditPart).getFigure().erase();
			//((AbstractGraphicalEditPart)childEditPart).getFigure().setVisible(false);
			//((AbstractGraphicalEditPart)childEditPart).getFigure().setEnabled(false);
			//((AbstractGraphicalEditPart)childEditPart).getFigure().setOpaque(false);
			//((AbstractGraphicalEditPart)childEditPart).getFigure().setPreferredSize(new Dimension(0,0));
			cnt++;
			super.addChildVisual(childEditPart, index);
		}
		else{
			super.addChildVisual(childEditPart, index);
			//super.addChildVisual(childEditPart, index-cnt);
		}
		//super.addChildVisual(childEditPart, index);
	}
}
