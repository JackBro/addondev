package gef.example.helloworld.model.commands;

import gef.example.helloworld.model.AbstractModel;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.HelloModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

public class MoveChildCommand extends Command {

	protected ContentsModel parent;
	protected ContentsModel oldparent;
	protected ElementModel model;
	protected int addIndex = -1;
	
	public MoveChildCommand(EditPart host, EditPart child, EditPart after) {
		parent = (ContentsModel)host.getModel();
		
		model = (ElementModel)child.getModel();
		if(after !=null){
			oldparent = ((ElementModel)after.getModel()).getParent();
			if(oldparent !=null){
				addIndex = oldparent.getChildIndex(after.getModel());
			}
		}
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		if (oldparent != null) {
			oldparent.removeChild(model);
			
			if (addIndex >= 0) {
				parent.addChild(addIndex, model);
			} else {
				parent.addChild(model);
			}
		}
	}

}
