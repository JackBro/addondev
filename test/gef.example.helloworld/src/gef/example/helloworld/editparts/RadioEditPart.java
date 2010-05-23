package gef.example.helloworld.editparts;

import gef.example.helloworld.figure.RadioFigure;

import org.eclipse.draw2d.IFigure;

public class RadioEditPart extends AbstractElementEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		RadioFigure fig = new RadioFigure();
		
		return fig;
	}

}
