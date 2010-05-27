package gef.example.helloworld.editparts;

import gef.example.helloworld.figure.AbstractElementFigure;
import gef.example.helloworld.figure.BoxFigure;

import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;

public class ListBoxEditPart extends AbstractContentsEditPart {
	
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		AbstractElementFigure fig = new BoxFigure();
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		fig.setLayoutManager(gl);
		fig.setPreferredSize(100, 22);
		
		return fig;
	}
	
	@Override
	public IFigure getBottom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFigure getMain() {
		// TODO Auto-generated method stub
		return getFigure();
	}

	@Override
	public IFigure getTop() {
		// TODO Auto-generated method stub
		return null;
	}
}
