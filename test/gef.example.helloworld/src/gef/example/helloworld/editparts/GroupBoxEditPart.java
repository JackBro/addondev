package gef.example.helloworld.editparts;

import gef.example.helloworld.model.ElementModel;

import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.IFigure;

public class GroupBoxEditPart extends BoxEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		ElementModel model = (ElementModel)getModel();
		
		IFigure figure = super.createFigure();
		GroupBoxBorder border = new GroupBoxBorder();
		border.setLabel(model.getName());
		figure.setBorder(border);
		return figure;
	}

}
