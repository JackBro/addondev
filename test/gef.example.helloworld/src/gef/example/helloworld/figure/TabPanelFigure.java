package gef.example.helloworld.figure;

import org.eclipse.draw2d.ToolbarLayout;

public class TabPanelFigure extends BoxFigure {

	public TabPanelFigure() {
		super();
		// TODO Auto-generated constructor stub
		ToolbarLayout tl = new ToolbarLayout();
		tl.setStretchMinorAxis(false);
		tl.setVertical(false);
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
