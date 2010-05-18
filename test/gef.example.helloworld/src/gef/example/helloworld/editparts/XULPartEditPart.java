package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.RootXYLayoutEditPolicy;
import gef.example.helloworld.model.*;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

public class XULPartEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Figure figure = new Figure();
		figure.setOpaque(true);
		figure.setBackgroundColor(ColorConstants.lightGray);
		figure.setPreferredSize(10, 50);
		
		FlowLayout fl = new FlowLayout();
		fl.setHorizontal(true);
		fl.setStretchMinorAxis(false);
		
		figure.setLayoutManager(fl);
		
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new RootXYLayoutEditPolicy((ContentsModel)getModel()));
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		// TODO Auto-generated method stub
		ElementModel model = (ElementModel)getElementModel().getMain().getChildren().get(0); 
		model.getChildren().add(childEditPart.getModel());
		super.addChildVisual(childEditPart, index);
	}

	
	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		// TODO Auto-generated method stub
		//getElementModel().getMain().getChildren().remove(childEditPart.getModel());
		ElementModel model = (ElementModel)getElementModel().getMain().getChildren().get(0);
		model.getChildren().remove(childEditPart.getModel());
		super.removeChildVisual(childEditPart);
		
	}

	private XULPartModel getElementModel(){
		return (XULPartModel)getModel();
	}
}
