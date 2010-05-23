package gef.example.helloworld.model.commands;

import gef.example.helloworld.model.AbstractModel;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.LabelModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

public class MoveChildCommand extends Command {

	protected ContentsModel parent;
	protected ContentsModel oldparent;
	protected AbstractElementModel model;
	protected int addIndex = -1;
	
	public MoveChildCommand(EditPart host, EditPart child, EditPart after) {
		parent = (ContentsModel)host.getModel();
		
		model = (AbstractElementModel)child.getModel();
		if(after !=null){
			oldparent = ((AbstractElementModel)after.getModel()).getParent();
			if(oldparent !=null){
				//addIndex = oldparent.getChildIndex(after.getModel());
				addIndex = oldparent.getChildren().indexOf(after.getModel());
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
