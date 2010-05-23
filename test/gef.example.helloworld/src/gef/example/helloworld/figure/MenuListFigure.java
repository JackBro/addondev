package gef.example.helloworld.figure;

import org.eclipse.draw2d.ArrowButton;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.SimpleLoweredBorder;
import org.eclipse.draw2d.geometry.Dimension;

public class MenuListFigure extends AbstractElementFigure {

	private ArrowButton figure;
	public MenuListFigure() {
		super();
		//setOpaque(true);
		//setBackgroundColor(ColorConstants.white);
		//setBorder(new SimpleLoweredBorder());
		setLayoutManager(new BorderLayout());
		// TODO Auto-generated constructor stub
		figure = new ArrowButton(PositionConstants.SOUTH);
		figure.setPreferredSize(16, 16);
		figure.setOpaque(true);
		setPreferredSize(100, 16);
		
		LabelFigure label = new LabelFigure();
		label.setBorder(new SimpleLoweredBorder());
		add(label, BorderLayout.CENTER);
		add(figure, BorderLayout.RIGHT);
		
	}

	@Override
	public int getDefaultHeight() {
		// TODO Auto-generated method stub
		return 16;
	}

	@Override
	public int getDefaultWidth() {
		// TODO Auto-generated method stub
		return 100;
	}

}
