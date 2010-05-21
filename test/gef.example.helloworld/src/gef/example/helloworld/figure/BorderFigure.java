package gef.example.helloworld.figure;

import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CoordinateListener;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;

public class BorderFigure extends Figure {
	
	private Figure center;
	private Figure bottom;
	
	public BorderFigure() {
		super();
		// TODO Auto-generated constructor stub
		//Figure figure = new Figure();
		setBackgroundColor(ColorConstants.lightGray);
		setBorder(new LineBorder());
		setOpaque(true);
		//figure.setPreferredSize(400, 400);
		BorderLayout layout=new BorderLayout();
		setLayoutManager(layout);
		
		
		center = new Figure();
		center.setBackgroundColor(ColorConstants.white);
		center.setOpaque(true);
		//center.setPreferredSize(400,200);
		//figure.setSize(400, 200);
		//FlowLayout fl = new FlowLayout();
		ToolbarLayout tl = new ToolbarLayout();
		tl.setStretchMinorAxis(false);
		tl.setVertical(true);
		tl.setSpacing(5);
		center.setLayoutManager(tl);
		
		bottom = new Figure();
		bottom.setBackgroundColor(ColorConstants.gray);
		bottom.setOpaque(true);
		FlowLayout fl = new FlowLayout();
		fl.setStretchMinorAxis(false);
		fl.setHorizontal(true);
		//fl.setSpacing(5);
		bottom.setLayoutManager(fl);
		
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.BOTTOM);
	}

	public Figure getCenter() {
		return center;
	}

	public Figure getBottom() {
		return bottom;
	}
}
