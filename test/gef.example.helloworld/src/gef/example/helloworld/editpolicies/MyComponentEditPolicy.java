package gef.example.helloworld.editpolicies;

import gef.example.helloworld.model.commands.DeleteCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class MyComponentEditPolicy extends ComponentEditPolicy {
	// オーバーライド
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		DeleteCommand command = new DeleteCommand();
		command.setContentsModel(getHost().getParent().getModel());
		command.setHelloModel(getHost().getModel());
		return command;
	}
}
