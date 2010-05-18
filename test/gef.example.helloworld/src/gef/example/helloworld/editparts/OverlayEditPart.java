package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

public class OverlayEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Figure figure = new Figure();
		figure.setBackgroundColor(ColorConstants.lightGray);
		figure.setBorder(new SimpleRaisedBorder());
		figure.setOpaque(true);
		figure.setPreferredSize(200, 200);
		BorderLayout layout=new BorderLayout();
		figure.setLayoutManager(layout);

		return figure;
	}
	
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		//super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new BoxLayoutEditPolicy());
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		// TODO Auto-generated method stub
		//super.addChildVisual(childEditPart, index);
		IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
		IFigure overlay = getFigure();
		if(childEditPart instanceof StatusbarEditPart){
			overlay.add(child, BorderLayout.BOTTOM);
			//overlay.add(child, BorderLayout.TOP);
			//child.setVisible(true);
		}
	}

}
