package gef.example.helloworld.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.model.*;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SimpleLoweredBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPolicy;

public class StatusbarEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Figure figure = new Figure();
		figure.setPreferredSize(10, 22);
		figure.setBorder(new SimpleLoweredBorder());
		ToolbarLayout tl = new ToolbarLayout();
		tl.setVertical(false);
		figure.setLayoutManager(tl);
		
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		//super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new BoxLayoutEditPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(StatusbarModel.ATTR_MENUPOPUP)){
			ElementModel model = (ElementModel)getModel();
			MenuPopupModel popup = (MenuPopupModel)model.getPropertyValue(StatusbarModel.ATTR_MENUPOPUP);
			popup.setChildren((List)evt.getNewValue());
			
		}
	}
	
}
