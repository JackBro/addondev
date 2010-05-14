package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.MyComponentEditPolicy;
import gef.example.helloworld.figure.RadioFigure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

public class RadioEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		RadioFigure fig = new RadioFigure();
		
		return fig;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new MyComponentEditPolicy());
	}

}
