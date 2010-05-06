package gef.example.helloworld.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Insets;

public class BoxFigure extends ElementFigure {

	public BoxFigure() {
		super();
		// TODO Auto-generated constructor stub
		setBorder(new LineBorder(ColorConstants.black,1, Graphics.LINE_DOT));

		Insets padding = new Insets(5, 5, 5, 5);
		MarginBorder marginBorder = new MarginBorder(padding);
		setBorder(marginBorder);
	}

	@Override
	public int getDefaultHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDefaultWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

}
