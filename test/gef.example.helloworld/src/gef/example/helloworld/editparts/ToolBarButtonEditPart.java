package gef.example.helloworld.editparts;

import org.eclipse.draw2d.IFigure;

public class ToolBarButtonEditPart extends ButtonEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		IFigure button = super.createFigure();
		button.setBorder(null);
		
		return button;
	}

}
