package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.RootXYLayoutEditPolicy;
import gef.example.helloworld.model.ContentsModel;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

public class XULPartEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Figure figure = new Figure();
		figure.setOpaque(true);
		figure.setBackgroundColor(ColorConstants.lightGray);
		figure.setPreferredSize(10, 50);
		
		//GridData
		
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new RootXYLayoutEditPolicy((ContentsModel)getModel()));
	}

}
