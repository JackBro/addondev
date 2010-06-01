package gef.example.helloworld.editparts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;

public class SeparatorEditPart extends AbstractElementEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Figure figure = new Figure();
		Figure line = new Figure();
		line.setPreferredSize(10, 1);
		line.setBackgroundColor(ColorConstants.darkGray);
		line.setOpaque(true);
		
		ToolbarLayout tl = new ToolbarLayout();
		tl.setVertical(true);
		tl.setStretchMinorAxis(true);
		figure.setLayoutManager(tl);
		figure.add(line);
		figure.setPreferredSize(10, 3);
		return figure;
	}

}
