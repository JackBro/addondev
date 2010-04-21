package gef.example.helloworld.model.commands;

import gef.example.helloworld.model.AbstractModel;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.HelloModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

public class MoveChildCommand extends Command {

	protected ContentsModel parent;
	protected AbstractModel model;
	
	public MoveChildCommand(EditPart host, EditPart child, EditPart after) {
		parent = (ContentsModel)host.getModel();
		model = (AbstractModel)child.getModel();
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		if (parent != null) {
			parent.removeChild(model);
			
			parent.addChild(0, model);
		}
	}

}
