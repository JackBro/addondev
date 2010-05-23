package gef.example.helloworld.editpolicies;

import java.util.List;

import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.commands.ChangeConstraintCommand;
import gef.example.helloworld.model.commands.CreateCommand;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

public class RootXYLayoutEditPolicy extends FlowLayoutEditPolicy {

	private ContentsModel parent;
	public RootXYLayoutEditPolicy(ContentsModel parent) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
	}

	@Override
	protected Command createAddCommand(EditPart child, EditPart after) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// TODO Auto-generated method stub
		AbstractElementModel model = (AbstractElementModel) request.getNewObject();		
		EditPart after = getInsertionReference(request);
		int index = getHost().getChildren().indexOf(after);
		CreateCommand command = new CreateCommand((ContentsModel)getHost().getModel(), model, index);		
		return command;
	}
	
	@Override
	protected boolean isHorizontal() {
		// TODO Auto-generated method stub
        IFigure figure = ((GraphicalEditPart) getHost()).getContentPane();  
        LayoutManager layout = figure.getLayoutManager();  
        if (layout instanceof FlowLayout)  
                return ((FlowLayout) figure.getLayoutManager()).isHorizontal();  
        if (layout instanceof ToolbarLayout)  
                return ((ToolbarLayout) figure.getLayoutManager()).isHorizontal();  
        return false;  
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		// TODO Auto-generated method stub
		//return super.createChildEditPolicy(child);
		 return new ResizableEditPolicy();
	}

	@Override
	public Command getCommand(Request request) {
		// TODO Auto-generated method stub
		if(REQ_RESIZE_CHILDREN.equals(request.getType()))
		{
			ChangeBoundsRequest creq = (ChangeBoundsRequest)request;
			Dimension dim = creq.getSizeDelta();
			
			ChangeConstraintCommand cccm = new ChangeConstraintCommand();
			cccm.setModel(parent.getChildren().get(0));
			cccm.setConstraint(new Rectangle(new Point(0, 0), dim));
			return cccm;
//			cccm.setConstraint(request.)
//			return new 
		}
		return super.getCommand(request);
		
	}
}
