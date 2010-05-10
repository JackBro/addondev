package gef.example.helloworld.figure;

import org.eclipse.draw2d.CheckBox;
import org.eclipse.swt.graphics.Image;

public class RadioFigure extends LabelFigure {

	static final Image RAIDO = LabelFigure.createImage("radio.png");

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		setImage(RAIDO);
		setText("radio");
	}

	@Override
	public int getDefaultWidth() {
		// TODO Auto-generated method stub
		return super.getDefaultWidth() + RAIDO.getImageData().width;
	}

}
