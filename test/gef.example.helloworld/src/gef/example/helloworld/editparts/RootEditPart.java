package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.MyXYLayoutEditPolicy;
import gef.example.helloworld.editpolicies.RootXYLayoutEditPolicy;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.HBoxModel;
import gef.example.helloworld.model.RootModel;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPolicy;

public class RootEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Layer figure = new Layer();
		//figure.setBackgroundColor(ColorConstants.lightGray);
		//figure.setOpaque(false);

		//figure.setPreferredSize(200, 200);
		//figure.setLayoutManager(new XYLayout());
		ToolbarLayout tl = new ToolbarLayout();
		tl.setStretchMinorAxis(false);
		figure.setLayoutManager(new org.eclipse.draw2d.FlowLayout());
		
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		//installEditPolicy(EditPolicy.LAYOUT_ROLE, new MyXYLayoutEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new RootXYLayoutEditPolicy((ContentsModel)getModel()));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals(ContentsModel.P_ADD_CHILDREN)
				|| evt.getPropertyName().equals(ContentsModel.P_REMOVE_CHILDREN)) {
			// 子モデルの構造が変化したので子EditPartの構造も更新する
			refreshChildren();
		}
	}	
}
