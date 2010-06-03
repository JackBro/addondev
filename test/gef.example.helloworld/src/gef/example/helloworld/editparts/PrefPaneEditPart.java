package gef.example.helloworld.editparts;

import gef.example.helloworld.figure.TabPanelFigure;

import org.eclipse.draw2d.IFigure;

public class PrefPaneEditPart extends BoxEditPart {
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		return new TabPanelFigure();
	}
}
