package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.editpolicies.MyXYLayoutEditPolicy;
import gef.example.helloworld.editpolicies.RootXYLayoutEditPolicy;
import gef.example.helloworld.figure.BorderFigure;
import gef.example.helloworld.model.ContentsModel;
import java.beans.PropertyChangeEvent;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

public class RootEditPart extends AbstractEditPartWithListener {

	private BorderFigure figure;
	
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
//		Layer figure = new Layer();		
//		Figure bFigure = new Figure();
//		bFigure.setBackgroundColor(ColorConstants.darkGray);
//		bFigure.setPreferredSize(100, 50);
//		ToolbarLayout tl = new ToolbarLayout();
//		tl.setStretchMinorAxis(true);
//		tl.setVertical(true);
//		figure.setLayoutManager(tl);

		figure = new BorderFigure();
		figure.getBottom().setPreferredSize(100, 80);		
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		//installEditPolicy(EditPolicy.LAYOUT_ROLE, new MyXYLayoutEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new RootXYLayoutEditPolicy((ContentsModel)getModel()));
		//installEditPolicy(EditPolicy.LAYOUT_ROLE, new BoxLayoutEditPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals(ContentsModel.P_ADD_CHILD)
				|| evt.getPropertyName().equals(ContentsModel.P_REMOVE_CHILD)) {
			// 子モデルの構造が変化したので子EditPartの構造も更新する
			refreshChildren();
		}
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		// TODO Auto-generated method stub
		IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
		figure.getCenter().add(child, index);
		//super.addChildVisual(childEditPart, index);
	}	
}
