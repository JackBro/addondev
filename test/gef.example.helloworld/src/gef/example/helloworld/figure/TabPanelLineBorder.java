package gef.example.helloworld.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;

public class TabPanelLineBorder extends LineBorder {

	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		tempRect.setBounds(getPaintRectangle(figure, insets));
		if (getWidth() % 2 == 1) {
			tempRect.width--;
			tempRect.height--;
		}
		tempRect.shrink(getWidth() / 2, getWidth() / 2);
		graphics.setLineWidth(getWidth());
		graphics.setLineStyle(getStyle());
		if (getColor() != null)
			graphics.setForegroundColor(getColor());
		
		int x1 = tempRect.getTopLeft().x;
		int y1 = tempRect.getTopLeft().y;
		int x2 = tempRect.getTopRight().x;
		int y2 = tempRect.getTopRight().y;
		int x3 = tempRect.getBottomRight().x;
		int y3 = tempRect.getBottomRight().y;		
		
		graphics.drawLine(x1, y1, x2, y2);
		graphics.drawLine(x2, y2, x3, y3);
		
	}

}
