package gef.example.helloworld.editparts;

import gef.example.helloworld.figure.LabelFigure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.SimpleLoweredBorder;

public class TextBoxEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		LabelFigure label = new LabelFigure();
		label.setBackgroundColor(ColorConstants.white);
		label.setOpaque(true);
		label.setBorder(new SimpleLoweredBorder());
		//add(label);
		label.setPreferredSize(100, 20);
		
		return label;
	}

}
