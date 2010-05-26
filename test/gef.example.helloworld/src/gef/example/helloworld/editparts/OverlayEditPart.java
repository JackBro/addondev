package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.editpolicies.MyXYLayoutEditPolicy;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

public class OverlayEditPart extends AbstractContentsEditPart {

	private Figure top, center, buttom;
	
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Figure figure = new Figure();
		figure.setBackgroundColor(ColorConstants.lightGray);
		figure.setBorder(new SimpleRaisedBorder());
		figure.setOpaque(true);
		figure.setPreferredSize(200, 200);
		BorderLayout layout=new BorderLayout();
		figure.setLayoutManager(layout);
		
		top = new Figure();
		center = new Figure();
		center.setLayoutManager(new ToolbarLayout());
		buttom = new Figure();
		buttom.setLayoutManager(new ToolbarLayout());
		
		
		figure.add(center, BorderLayout.CENTER);
		figure.add(buttom, BorderLayout.BOTTOM);
		
		return figure;
	}


//	@Override
//	protected void addChildVisual(EditPart childEditPart, int index) {
//		// TODO Auto-generated method stub
//		
//		IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
//		IFigure overlay = getFigure();
//		if(childEditPart instanceof StatusbarEditPart){
//			overlay.add(child, BorderLayout.BOTTOM);
//			//getBottom().add(child);
////			//overlay.add(child, BorderLayout.TOP);
////			//child.setVisible(true);
//		}
//		else{
//			//super.addChildVisual(childEditPart, index);
//			overlay.add(child, BorderLayout.CENTER);
//			
//		}
//	}

	@Override
	public IFigure getBottom() {
		// TODO Auto-generated method stub
		return buttom;
	}

	@Override
	protected boolean isBottomChild(EditPart childEditPart) {
		// TODO Auto-generated method stub
		return (childEditPart instanceof StatusbarEditPart);
	}


	@Override
	public IFigure getMain() {
		// TODO Auto-generated method stub
		return center;
	}

	@Override
	public IFigure getTop() {
		// TODO Auto-generated method stub
		return null;
	}

}
