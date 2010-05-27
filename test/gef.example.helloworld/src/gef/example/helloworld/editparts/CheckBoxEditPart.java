package gef.example.helloworld.editparts;

import gef.example.helloworld.figure.CheckBoxFigure;

import org.eclipse.draw2d.CheckBox;
import org.eclipse.draw2d.IFigure;

public class CheckBoxEditPart extends AbstractElementEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		CheckBoxFigure checkbox = new CheckBoxFigure();
		checkbox.setText("check");
		return checkbox;
	}

}
