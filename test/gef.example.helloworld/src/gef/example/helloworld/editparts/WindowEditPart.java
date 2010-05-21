package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.figure.BorderFigure;
import gef.example.helloworld.figure.WindowFigure;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.MenuBaseModel;
import gef.example.helloworld.model.StatusbarModel;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

public class WindowEditPart extends ContentsEditPart {

	private WindowFigure window;
	private BorderFigure figure;
	
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
//		Figure figure = new Figure();
//		figure.setBackgroundColor(ColorConstants.lightGray);
//		figure.setOpaque(true);
//		figure.setPreferredSize(400,200);
//		//figure.setSize(400, 200);
//		//FlowLayout fl = new FlowLayout();
//		ToolbarLayout tl = new ToolbarLayout();
//		tl.setStretchMinorAxis(true);
//		tl.setVertical(true);
//		tl.setSpacing(5);
//		figure.setLayoutManager(tl);
		
		window = new WindowFigure();
		
//		figure = new BorderFigure();
//		window = new WindowFigure();
//		figure.getCenter().add(window);
//		Figure figure = new Figure();
//		figure.setBackgroundColor(ColorConstants.lightGray);
//		figure.setBorder(new SimpleRaisedBorder());
//		figure.setOpaque(true);
//		figure.setPreferredSize(400, 400);
//		BorderLayout layout=new BorderLayout();
//		figure.setLayoutManager(layout);
//		
//		
//		center = new Figure();
//		center.setBackgroundColor(ColorConstants.lightGray);
//		center.setOpaque(true);
//		//center.setPreferredSize(400,200);
//		//figure.setSize(400, 200);
//		//FlowLayout fl = new FlowLayout();
//		ToolbarLayout tl = new ToolbarLayout();
//		tl.setStretchMinorAxis(true);
//		tl.setVertical(true);
//		tl.setSpacing(5);
//		center.setLayoutManager(tl);
//		
//		bottom = new Figure();
//		bottom.setBackgroundColor(ColorConstants.gray);
//		bottom.setOpaque(true);
//		ToolbarLayout btl = new ToolbarLayout();
//		btl.setStretchMinorAxis(false);
//		btl.setVertical(false);
//		btl.setSpacing(5);
//		bottom.setLayoutManager(btl);
//		
//		figure.add(center, BorderLayout.CENTER);
//		figure.add(bottom, BorderLayout.BOTTOM);
		
		return window;
	}

//	@Override
//	protected void addChildVisual(EditPart childEditPart, int index) {
//		// TODO Auto-generated method stub
//		IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
//		if(childEditPart.getModel() instanceof MenuBaseModel){
//			bottom.add(child);
//		}else{
//			center.add(child, index);
//		}
//		//super.addChildVisual(childEditPart, index);
//	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
//		if (evt.getPropertyName().equals(ContentsModel.P_ADD_CHILDREN)
//				|| evt.getPropertyName().equals(ContentsModel.P_REMOVE_CHILDREN))
//		{		
//			refreshChildren();
//		}
//		else 
		super.propertyChange(evt);
		
		if(evt.getPropertyName().equals("resize"))
		{
			Rectangle rect = (Rectangle)evt.getNewValue();
			IFigure figure = getFigure();
			//Rectangle rewrect = figure.getBounds()
			//figure.setBounds(rect);
			//figure.setSize(figure.getBounds().width+rect.width, figure.getBounds().height+rect.height);

			//figure.setPreferredSize(new Dimension(figure.getBounds().width, figure.getBounds().height));
			int w = figure.getPreferredSize().width +rect.width;
			int h = figure.getPreferredSize().height+rect.height;
			figure.setPreferredSize(new Dimension(w, h));
			refreshVisuals(); 
			//refreshChildren();
		}
	}

	@Override
	public Figure getBottom() {
		// TODO Auto-generated method stub
		return window.getBottom();
	}

	@Override
	public Figure getMain() {
		// TODO Auto-generated method stub
		return window.getCenter();
	}

	@Override
	public Figure getTop() {
		// TODO Auto-generated method stub
		return window.getTop();
	}

	@Override
	protected boolean isBottomChild(EditPart childEditPart) {
		// TODO Auto-generated method stub
		if(childEditPart.getModel() instanceof StatusbarModel){
			return true;
		}
		return super.isBottomChild(childEditPart);
	}

}
