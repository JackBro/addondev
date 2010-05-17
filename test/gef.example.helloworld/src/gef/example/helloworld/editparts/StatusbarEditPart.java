package gef.example.helloworld.editparts;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;

public class StatusbarEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Figure figure = new Figure();
		ToolbarLayout tl = new ToolbarLayout();
		tl.setVertical(false);
		figure.setLayoutManager(tl);
		
		return figure;
	}

}
