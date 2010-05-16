package gef.example.helloworld.editparts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;

public class MenuPopupEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Figure figure = new Figure();
		figure.setBackgroundColor(ColorConstants.blue);
		figure.setPreferredSize(30, 30);
		
		return figure;
	}

}
