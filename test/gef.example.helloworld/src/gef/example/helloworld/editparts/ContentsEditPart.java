package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.figure.BorderFigure;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.MenuBaseModel;
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

	//int cnt = 0;
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
		
		
//		if(childEditPart.getModel() instanceof MenuBaseModel){
//			IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
//			//AbstractGraphicalEditPart part = (AbstractGraphicalEditPart)getRoot().getChildren().get(0);
//			BorderFigure  figure = getRootBorderFigure();
//			figure.getBottom().add(child);
//			//AbstractGraphicalEditPart xulpart = (AbstractGraphicalEditPart)part.getChildren().get(0);			
//			//xulpart.getFigure().add(((AbstractGraphicalEditPart)childEditPart).getFigure());
//			
//			
//			
////			IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
////			getPart().add(child);
////			if(getPart().getChildren().size()>3){
////				getPart().setPreferredSize(getPart().getPreferredSize().width, getPart().getPreferredSize().height*2);
////			}
//			//xulpart.getFigure().add(child);
//			//((ContentsModel)xulpart.getModel()).addChild((ElementModel) childEditPart.getModel());
//			//((ContentsModel)xulpart.getModel()).addChild(new MenuPopupModel());
//			//((AbstractGraphicalEditPart)childEditPart).getFigure().erase();
//			//((AbstractGraphicalEditPart)childEditPart).getFigure().setVisible(false);
//			//((AbstractGraphicalEditPart)childEditPart).getFigure().setEnabled(false);
//			//((AbstractGraphicalEditPart)childEditPart).getFigure().setOpaque(false);
//			//((AbstractGraphicalEditPart)childEditPart).getFigure().setPreferredSize(new Dimension(0,0));
//			//cnt++;
//			//super.addChildVisual(childEditPart, index);
//		}
//		else{
//			IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
//			getMain().add(child, index);
//			//super.addChildVisual(childEditPart, index);
//			//super.addChildVisual(childEditPart, index-cnt);
//		}
//		//super.addChildVisual(childEditPart, index);
	}

	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		// TODO Auto-generated method stub
		if(childEditPart.getModel() instanceof MenuBaseModel){
//			cnt--;
//			EditPart part = (EditPart)getRoot().getChildren().get(0);
//			AbstractGraphicalEditPart xulpart = (AbstractGraphicalEditPart)part.getChildren().get(0);			
//			xulpart.getFigure().remove(((AbstractGraphicalEditPart)childEditPart).getFigure());	
			IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
			getRootBorderFigure().getBottom().remove(child);
		}else{
			IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
			getMain().remove(child);
			//super.removeChildVisual(childEditPart);
		}
	}

	protected BorderFigure getRootBorderFigure(){
		AbstractGraphicalEditPart part = (AbstractGraphicalEditPart)getRoot().getChildren().get(0);
		BorderFigure  figure = (BorderFigure)part.getFigure();
		return figure;
	}
	
	public abstract Figure getTop();
	public abstract Figure getMain();
	public abstract Figure getBottom();
	public Figure getPart() {
		// TODO Auto-generated method stub
		return getRootBorderFigure().getBottom();
	}
	
	protected boolean isPartChild(EditPart childEditPart){
		if(childEditPart.getModel() instanceof MenuBaseModel){
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
