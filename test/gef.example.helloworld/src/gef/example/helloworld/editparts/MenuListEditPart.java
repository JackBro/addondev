package gef.example.helloworld.editparts;

import gef.example.helloworld.figure.MenuListFigure;

import org.eclipse.draw2d.IFigure;

public class MenuListEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		IFigure figure = new MenuListFigure();
		return figure;
	}

}
