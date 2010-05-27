package gef.example.helloworld.editparts;

import gef.example.helloworld.model.TemplateModel;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;

public class TemplateEditPart extends DataElementEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Figure figure = new Figure();
		figure.setOpaque(true);
		figure.setBackgroundColor(ColorConstants.blue);
		figure.setPreferredSize(30, 30);
		
		return figure;
	}
}
