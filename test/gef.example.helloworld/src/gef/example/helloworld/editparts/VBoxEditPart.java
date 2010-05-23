package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.MyComponentEditPolicy;
import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.figure.BoxFigure;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.LabelModel;
import gef.example.helloworld.model.VBoxModel;

import java.beans.PropertyChangeEvent;
import java.util.List;


import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;

public class VBoxEditPart extends BoxEditPart {
//	private Figure dummy;
//	@Override
//	protected IFigure createFigure() {
//		// TODO Auto-generated method stub	
////		ElementModel model = (ElementModel)getModel();
////		
////		BoxFigure figure = new BoxFigure();
////		//Layer figure = new Layer();
////		LineBorder lb = new LineBorder();
////		lb.setColor(ColorConstants.red);
////		lb.setWidth(3);
////		figure.setBorder(new LineBorder(ColorConstants.lightGray));
////		
////		Insets padding = new Insets(5, 5, 5, 5);
////		MarginBorder marginBorder = new MarginBorder(padding);
////
////		ToolbarLayout tl = new ToolbarLayout();
////		tl.setVertical(true);
////		tl.setStretchMinorAxis(true);
////		
////		figure.setLayoutManager(tl);
////		
////		if(model.getChildren().size() == 0){
////			dummy  = new Figure();
////			dummy.setPreferredSize(10, 20);	
////			figure.add(dummy);
////		}
//		IFigure figure = getFigure();
//		ToolbarLayout tl = (ToolbarLayout)figure.getLayoutManager();
//		tl.setVertical(true);
//		return figure;
//	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new BoxLayoutEditPolicy());
	}

//	@Override
//	public void propertyChange(PropertyChangeEvent evt) {
//		// TODO Auto-generated method stub
//		if (evt.getPropertyName().equals(ContentsModel.P_ADD_CHILD))
//		{
//			ContentsModel elm = (ContentsModel)getModel();
//			if(getFigure().getChildren().size() > 0){
//				getFigure().getChildren().remove(dummy);
//			}
//			refreshChildren();
//			
//		}else if(evt.getPropertyName().equals(VBoxModel.P_REMOVE_CHILD)){
//						
//			ContentsModel elm = (ContentsModel)getModel();
//			if(elm.getChildren().size() == 0){
//				getFigure().add(dummy);
//			}	
//			refreshChildren();
//		}
//	}
}
