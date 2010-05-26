package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.figure.BorderFigure;
import gef.example.helloworld.figure.AbstractElementFigure;
import gef.example.helloworld.model.AbstractDataModel;
import gef.example.helloworld.model.BoxModel;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.MenuPopupModel;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public abstract class AbstractContentsEditPart extends AbstractEditPartWithListener {

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new BoxLayoutEditPolicy());
	}

//	public void propertyChange(PropertyChangeEvent evt) {
////		if (evt.getPropertyName().equals(ContentsModel.P_ADD_CHILD)
////				|| evt.getPropertyName().equals(ContentsModel.P_REMOVE_CHILD)) {
////			refreshChildren();
////		}
//
//	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
		if(isPartChild(childEditPart)){
			BorderFigure  figure = getRootBorderFigure();
			figure.getBottom().add(child);			
		}
		else if(isTopChild(childEditPart)){
			getTop().add(child, 0);			
		}
		else if(isBottomChild(childEditPart)){
			getBottom().add(child);			
		}
		else if(isMainChild(childEditPart)){
			getMain().add(child, index);			
		}
	}

	@Override
	protected void removeChildVisual(EditPart childEditPart) {
//		if(childEditPart.getModel() instanceof AbstractDataModel){
//			IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
//			getRootBorderFigure().getBottom().remove(child);
//		}else{
//			IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
//			getMain().remove(child);
//		}
		
		IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
		if(isPartChild(childEditPart)){
			BorderFigure  figure = getRootBorderFigure();
			figure.getBottom().remove(child);			
		}
		else if(isTopChild(childEditPart)){
			getTop().remove(child);			
		}
		else if(isBottomChild(childEditPart)){
			getBottom().remove(child);			
		}
		else if(isMainChild(childEditPart)){
			getMain().remove(child);		
		}
	}

	protected BorderFigure getRootBorderFigure(){
		AbstractGraphicalEditPart part = (AbstractGraphicalEditPart)getRoot().getChildren().get(0);
		BorderFigure  figure = (BorderFigure)part.getFigure();
		return figure;
	}
	
	public abstract IFigure getTop();
	public abstract IFigure getMain();
	public abstract IFigure getBottom();
	public IFigure getPart() {
		return getRootBorderFigure().getBottom();
	}
	
	protected boolean isPartChild(EditPart childEditPart){
		if(childEditPart.getModel() instanceof AbstractDataModel){
			return true;
		}
		if(childEditPart.getModel() instanceof MenuPopupModel){
			return true;
		}
		return false;
	}
	protected boolean isMainChild(EditPart childEditPart){
		return true;
	}
	protected boolean isTopChild(EditPart childEditPart){
		return false;
	}
	protected boolean isBottomChild(EditPart childEditPart){
		return false;
	}
}
