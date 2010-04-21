package gef.example.helloworld.model.commands;

import gef.example.helloworld.model.AbstractModel;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;

import org.eclipse.gef.commands.Command;

public class AddCommand extends Command {
	
	protected ContentsModel root;
	protected ContentsModel oldroot;
	protected ElementModel model;
	
	protected int beforeIndex;
	protected int addIndex;	
	
	public AddCommand(ContentsModel root, ElementModel model, ElementModel afterModel) {
		super();
		this.root = root;
		this.model = model;
		
		oldroot = model.getParent();
		beforeIndex = oldroot.getChildIndex(model);

		if (afterModel != null) {
			addIndex = root.getChildIndex(afterModel);
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		//model.setConstraint(rectangle);
		//oldRoot.removeChild(model);
		if (addIndex > 0) {
			root.addChild(addIndex, model);
		} else {
			root.addChild(model);
		}
	}

}
