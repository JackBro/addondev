package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.editpolicies.RadioGroupBoxLayoutEditPolicy;

import org.eclipse.gef.EditPolicy;

public class RadioGroupEditPart extends BoxEditPart {

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		//super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new RadioGroupBoxLayoutEditPolicy());
	}

}
