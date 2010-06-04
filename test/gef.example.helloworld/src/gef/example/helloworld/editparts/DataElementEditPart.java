package gef.example.helloworld.editparts;

import gef.example.helloworld.HelloworldPlugin;
import gef.example.helloworld.figure.DataFigure;
import gef.example.helloworld.model.AbstractDataModel;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Image;

public class DataElementEditPart extends AbstractEditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		AbstractDataModel model = (AbstractDataModel)getModel();
		String strimage = model.getImage();
		Image image = HelloworldPlugin.getDefault().getImageRegistry().get(strimage);
		String text = model.getLabel();
		DataFigure figure = new DataFigure(image, text);
		return figure;
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		// TODO Auto-generated method stub
		//super.addChildVisual(childEditPart, index);
	}

	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		// TODO Auto-generated method stub
		//super.removeChildVisual(childEditPart);
	}
}
