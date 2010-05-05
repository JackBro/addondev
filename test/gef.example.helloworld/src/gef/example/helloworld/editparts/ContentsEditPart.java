package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.MyXYLayoutEditPolicy;
import gef.example.helloworld.editpolicies.VBoxLayoutEditPolicy;
import gef.example.helloworld.model.ContentsModel;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPolicy;

public class ContentsEditPart extends EditPartWithListener {

	/* (非 Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Layer figure = new Layer();
		//figure.setLayoutManager(new XYLayout());
		
//		GridLayout gl = new GridLayout();
//		gl.numColumns = 1;
//		gl.horizontalSpacing = 5;
//		figure.setLayoutManager(gl);
		
		ToolbarLayout tl = new ToolbarLayout();
		//tl.setMatchWidth(true);
		tl.setStretchMinorAxis(true);
//		tl.setVertical(false);
//		tl.setStretchMinorAxis(false);
//		figure.setLayoutManager(tl);
		FlowLayout fl = new FlowLayout();
		fl.setStretchMinorAxis(true);
		//fl.setHorizontal(false);
		figure.setLayoutManager(fl);
		return figure;
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		//installEditPolicy(EditPolicy.LAYOUT_ROLE, new MyXYLayoutEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new VBoxLayoutEditPolicy());
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return ((ContentsModel) getModel()).getChildren();
	}

	/* (非 Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ContentsModel.P_ADD_CHILDREN)
				|| evt.getPropertyName().equals(ContentsModel.P_REMOVE_CHILDREN)) {
			// 子モデルの構造が変化したので子EditPartの構造も更新する
			refreshChildren();
		}
	}

}
