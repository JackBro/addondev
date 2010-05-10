package gef.example.helloworld.editpolicies;

import gef.example.helloworld.editparts.RadioEditPart;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.RadioModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

public class RadioGroupBoxLayoutEditPolicy extends BoxLayoutEditPolicy {

	@Override
	protected Command createAddCommand(EditPart child, EditPart after) {
		// TODO Auto-generated method stub
		if(!(child instanceof RadioEditPart)) return null;
		return super.createAddCommand(child, after);
	}

	@Override
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		// TODO Auto-generated method stub
		if(!(child instanceof RadioEditPart)) return null;
		return super.createMoveChildCommand(child, after);
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// TODO Auto-generated method stub
		ElementModel model = (ElementModel) request.getNewObject();	
		if(!(model instanceof RadioModel)) return null;
		return super.getCreateCommand(request);
	}

}
