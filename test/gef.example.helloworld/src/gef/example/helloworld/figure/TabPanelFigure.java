package gef.example.helloworld.figure;

import org.eclipse.draw2d.ButtonBorder;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.draw2d.ToolbarLayout;

public class TabPanelFigure extends BoxFigure {

	public TabPanelFigure(Boolean isvertical) {
		super();
		// TODO Auto-generated constructor stub
		setBorder(new LineBorder());
		
		ToolbarLayout tl = new ToolbarLayout();
		tl.setStretchMinorAxis(false);
		tl.setVertical(isvertical);
		setLayoutManager(tl);
	}

	@Override
	public int getDefaultHeight() {
		// TODO Auto-generated method stub
		return super.getDefaultHeight()+20;
	}

	@Override
	public int getDefaultWidth() {
		// TODO Auto-generated method stub
		return super.getDefaultWidth()+50;
	}

}
