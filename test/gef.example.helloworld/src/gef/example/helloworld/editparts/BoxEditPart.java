package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.VBoxLayoutEditPolicy;
import gef.example.helloworld.figure.BoxFigure;
import gef.example.helloworld.figure.ElementFigure;
import gef.example.helloworld.model.BoxModel;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.EditPolicy;

public class BoxEditPart extends EditPartWithListener {

	private Figure dummy;
	@Override
	protected IFigure createFigure() {
		ElementFigure figure = new BoxFigure();
		
		ToolbarLayout tl = new ToolbarLayout();
		tl.setSpacing(5);	
		tl.setVertical(isVertical());
		figure.setLayoutManager(tl);
		
		dummy  = new Figure();
		dummy.setPreferredSize(10, 20);		
		figure.add(dummy);
		
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new VBoxLayoutEditPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals(ContentsModel.P_ADD_CHILDREN))
		{
			if(getFigure().getChildren().size() > 0){
				getFigure().getChildren().remove(dummy);
			}
			refreshChildren();
		}else if(evt.getPropertyName().equals(BoxModel.P_REMOVE_CHILDREN)){
			ContentsModel elm = (ContentsModel)getModel();
			if(elm.getChildren().size() == 0){
				getFigure().add(dummy);	
			}			
			refreshChildren();
		}else if(evt.getPropertyName().equals(BoxModel.ATTR_ORIENT)){
			ToolbarLayout tl = (ToolbarLayout) getFigure().getLayoutManager();
			tl.setVertical(isVertical());
			getFigure().validate();
		}else if(evt.getPropertyName().equals(BoxModel.ATTR_FLEX)){
	    	EditPartWithListener ep = (EditPartWithListener)getParent();
	    	ep.resizeWidth();
		}
	}


	protected boolean isVertical(){
		String orient = ((BoxModel)getModel()).getPropertyValue(BoxModel.ATTR_ORIENT).toString();
		return orient.equalsIgnoreCase("vertical");		
	}
}
