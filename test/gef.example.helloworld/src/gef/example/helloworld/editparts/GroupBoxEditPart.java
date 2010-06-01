package gef.example.helloworld.editparts;

import gef.example.helloworld.model.AbstractElementModel;

import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.IFigure;

public class GroupBoxEditPart extends BoxEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		AbstractElementModel model = (AbstractElementModel)getModel();
		
		IFigure figure = super.createFigure();
		GroupBoxBorder border = new GroupBoxBorder();
		//border.setLabel(model.getName());
		border.setLabel("");
		figure.setBorder(border);
		return figure;
	}

}
