package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.VBoxLayoutEditPolicy;
import gef.example.helloworld.model.HBoxModel;
import gef.example.helloworld.model.VBoxModel;
import gef.example.helloworld.model.WindowModel;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;

public class WindowEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Figure figure = new Figure();
		figure.setBackgroundColor(ColorConstants.lightGray);
		figure.setOpaque(true);
		figure.setPreferredSize(200,200);
		figure.setSize(200, 200);
		FlowLayout fl = new FlowLayout();
		ToolbarLayout tl = new ToolbarLayout();
		tl.setStretchMinorAxis(false);
		tl.setVertical(true);
		tl.setSpacing(5);
		figure.setLayoutManager(tl);
		
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new VBoxLayoutEditPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(WindowModel.P_CHILDREN))
		{		
			refreshChildren();
		}
		else if(evt.getPropertyName().equals("resize"))
		{
			Rectangle rect = (Rectangle)evt.getNewValue();
			IFigure figure = getFigure();
			//Rectangle rewrect = figure.getBounds()
			//figure.setBounds(rect);
			figure.setSize(figure.getBounds().width+rect.width, figure.getBounds().height+rect.height);

			figure.setPreferredSize(new Dimension(figure.getBounds().width, figure.getBounds().height));
			refreshVisuals(); 
		}
	}
	
	protected List getModelChildren() {
		return ((WindowModel) getModel()).getChildren();
	}
}
