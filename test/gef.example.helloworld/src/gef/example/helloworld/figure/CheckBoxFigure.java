package gef.example.helloworld.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.SimpleLoweredBorder;
import org.eclipse.swt.graphics.Image;

public class CheckBoxFigure extends LabelFigure {

	static final Image CHECKBOX = LabelFigure.createImage("checkbox.png");
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
//		Figure box = new Figure();
//		box.setBorder(new SimpleLoweredBorder());
//		box.setBackgroundColor(ColorConstants.white);
//		box.setPreferredSize(20, 20);
//		add(box, 0);
		setImage(CHECKBOX);
		setText("checkbox");
	}

	@Override
	public int getDefaultWidth() {
		// TODO Auto-generated method stub
		return super.getDefaultWidth() + CHECKBOX.getImageData().width;
	}

}
