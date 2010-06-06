package gef.example.helloworld.editparts;

import gef.example.helloworld.figure.TabPanelFigure;
import gef.example.helloworld.model.BoxModel;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;

public class TabPanelEditPart extends BoxEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		BoxModel model = (BoxModel)getModel();
		Figure figure = new TabPanelFigure(model.isVertical());
		return figure;
	}

}
