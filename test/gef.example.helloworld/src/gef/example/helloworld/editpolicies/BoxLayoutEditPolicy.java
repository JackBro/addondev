package gef.example.helloworld.editpolicies;

import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.commands.AddCommand;
import gef.example.helloworld.model.commands.CreateCommand;
import gef.example.helloworld.model.commands.MoveChildCommand;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class BoxLayoutEditPolicy extends FlowLayoutEditPolicy {

	@Override
	protected Command createAddCommand(EditPart child, EditPart after) {
		// TODO Auto-generated method stub
		ElementModel model = (ElementModel) child.getModel();
		ElementModel afterModel = after==null?null:(ElementModel) after.getModel();
		return new AddCommand((ContentsModel)getHost().getModel(), model, afterModel);
	}

	@Override
	protected boolean isHorizontal() {
		// TODO Auto-generated method stub
		EditPart editpart = getHost();
		if(editpart instanceof GraphicalEditPart){
	        IFigure figure = ((GraphicalEditPart) editpart).getContentPane();  
	        LayoutManager layout = figure.getLayoutManager();  
	        if (layout instanceof FlowLayout)  
	                return ((FlowLayout) figure.getLayoutManager()).isHorizontal();  
	        if (layout instanceof ToolbarLayout)  
	                return ((ToolbarLayout) figure.getLayoutManager()).isHorizontal();  
          
		}
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
		ElementModel model = (ElementModel) request.getNewObject();		
		CreateCommand command = new CreateCommand((ContentsModel)getHost().getModel(), model);		
		return command;
	}

}
