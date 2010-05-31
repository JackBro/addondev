package gef.example.helloworld.figure;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.swt.graphics.Image;

public class DataFigure extends LabelFigure {

	//private Image image;
	//private String text;
	
	public DataFigure(Image image, String text) {

		super();
		//this.image = image;
		//this.text = text;
		// TODO Auto-generated constructor stub
		label.setIconAlignment(PositionConstants.TOP);
		label.setTextAlignment(PositionConstants.BOTTOM);
		label.setIcon(image);
		label.setText(text);
		
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
	}

	@Override
	public int getDefaultHeight() {
		// TODO Auto-generated method stub
		return label.getSize().height;
	}

}
