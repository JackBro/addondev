package gef.example.helloworld.figure;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.draw2d.ToolbarLayout;

public class WindowFigure extends ElementFigure {
	
	private Figure center;
	private Figure top;
	private Figure bottom;
	
	public WindowFigure() {
		super();
		// TODO Auto-generated constructor stub
		setBackgroundColor(ColorConstants.lightGray);
		setBorder(new SimpleRaisedBorder());
		setOpaque(true);
		setPreferredSize(200, 200);
		BorderLayout layout=new BorderLayout();
		setLayoutManager(layout);
		
		center = new Figure();
		center.setBackgroundColor(ColorConstants.lightGray);
		center.setOpaque(true);
		//center.setPreferredSize(400,200);
		//figure.setSize(400, 200);
		//FlowLayout fl = new FlowLayout();
		ToolbarLayout tl = new ToolbarLayout();
		tl.setStretchMinorAxis(true);
		tl.setVertical(true);
		tl.setSpacing(5);
		center.setLayoutManager(tl);
		
		top = new Figure();
		top.setBackgroundColor(ColorConstants.gray);
		top.setOpaque(true);
		ToolbarLayout ttl = new ToolbarLayout();
		ttl.setStretchMinorAxis(false);
		ttl.setVertical(false);
		ttl.setSpacing(5);
		top.setLayoutManager(ttl);	
		
		bottom = new Figure();
		bottom.setBackgroundColor(ColorConstants.gray);
		bottom.setOpaque(true);
		ToolbarLayout btl = new ToolbarLayout();
		btl.setStretchMinorAxis(true);
		btl.setVertical(true);
		btl.setSpacing(5);
		bottom.setLayoutManager(btl);			
		
		add(top, BorderLayout.TOP);
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.BOTTOM);
	}

	public Figure getCenter() {
		return center;
	}

	public Figure getTop() {
		return top;
	}

	public Figure getBottom() {
		return bottom;
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
