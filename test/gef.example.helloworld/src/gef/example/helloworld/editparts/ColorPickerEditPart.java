package gef.example.helloworld.editparts;

import gef.example.helloworld.figure.ColorPicerFigure;
import gef.example.helloworld.model.ColorPickerModel;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;

public class ColorPickerEditPart extends AbstractElementEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		return new ColorPicerFigure();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(ColorPickerModel.ATTR_TYPE)){
			boolean isbutton = ((ColorPickerModel)getModel()).isButton();
			((ColorPicerFigure)getFigure()).setIsButton(isbutton);
			((ColorPicerFigure)getFigure()).upDate();
		}
	}

}
