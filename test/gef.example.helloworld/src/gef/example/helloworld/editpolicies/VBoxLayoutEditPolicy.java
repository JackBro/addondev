package gef.example.helloworld.editpolicies;

import gef.example.helloworld.model.AbstractModel;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.HelloModel;
import gef.example.helloworld.model.commands.AddCommand;
import gef.example.helloworld.model.commands.CreateCommand;
import gef.example.helloworld.model.commands.MoveChildCommand;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class VBoxLayoutEditPolicy extends FlowLayoutEditPolicy {

	@Override
	public Command getCommand(Request request) {
		// TODO Auto-generated method stub
		// System.out.println(request.getType());
		return super.getCommand(request);
	}

	@Override
	protected Command createAddCommand(EditPart child, EditPart after) {
		// TODO Auto-generated method stub
		//getHost()
		ElementModel model = (ElementModel) child.getModel();
		ElementModel afterModel = (ElementModel) after.getModel();
		return new AddCommand((ContentsModel)getHost().getModel(), model, afterModel);
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
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		// TODO Auto-generated method stub
		return new MoveChildCommand(getHost(), child, after);
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// TODO Auto-generated method stub
		ContentsModel cm = (ContentsModel)getHost().getModel();
		ElementModel model = (ElementModel) request.getNewObject();		
		CreateCommand command = new CreateCommand((ContentsModel)getHost().getModel(), model);		
		return command;
	}

}
