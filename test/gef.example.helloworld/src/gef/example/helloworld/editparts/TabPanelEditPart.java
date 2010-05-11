package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.MyComponentEditPolicy;
import gef.example.helloworld.figure.TabPanelFigure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

public class TabPanelEditPart extends BoxEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		return new TabPanelFigure();
	}

//	@Override
//	protected void createEditPolicies() {
//		// TODO Auto-generated method stub
//		installEditPolicy( EditPolicy.COMPONENT_ROLE, new MyComponentEditPolicy());
//	}

}
