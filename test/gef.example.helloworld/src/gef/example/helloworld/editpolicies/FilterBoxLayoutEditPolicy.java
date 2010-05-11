package gef.example.helloworld.editpolicies;

import gef.example.helloworld.editparts.RadioEditPart;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.RadioModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

public abstract class FilterBoxLayoutEditPolicy extends BoxLayoutEditPolicy {

	@Override
	protected Command createAddCommand(EditPart child, EditPart after) {
		// TODO Auto-generated method stub
		if(isFilter(child)) return null;
		return super.createAddCommand(child, after);
	}

	@Override
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		// TODO Auto-generated method stub
		if(isFilter(child)) return null;
		return super.createMoveChildCommand(child, after);
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// TODO Auto-generated method stub
		ElementModel model = (ElementModel) request.getNewObject();	
		if(isFilter(model)) return null;
		return super.getCreateCommand(request);
	}
	
	abstract protected boolean isFilter(EditPart editpart);
	abstract protected boolean isFilter(ElementModel model);
}
