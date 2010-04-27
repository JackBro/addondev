package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.MyXYLayoutEditPolicy;
import gef.example.helloworld.editpolicies.VBoxLayoutEditPolicy;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.GridModel;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class GridEditPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Label label = new Label();
		label.setText("VBox");
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		Figure fig = new FreeformLayer();
		fig.setBorder(new LineBorder(ColorConstants.black,1, Graphics.LINE_DOT));
		fig.setLayoutManager(gl);
		fig.add(label);
		return fig;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new VBoxLayoutEditPolicy());
	
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals(ContentsModel.P_CHILDREN)) {
			refreshChildren();
		} else if (evt.getPropertyName().equals(ElementModel.ATTR_FLEX)) {
	    	EditPartWithListener ep = (EditPartWithListener)getParent();
	    	//ep.resizeChildren();
	    	
	    }
	}
	
	protected List getModelChildren() {
		return ((GridModel) getModel()).getChildren();
	}
	
	public void resizeColumns(){
		
	}
}
