package gef.example.helloworld.model.commands;

import gef.example.helloworld.model.AbstractModel;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.AbstractElementModel;

import org.eclipse.gef.commands.Command;

public class AddCommand extends Command {
	
	protected ContentsModel root;
	protected ContentsModel oldroot;
	protected AbstractElementModel model;
	
	protected int beforeIndex;
	protected int addIndex;	
	
	public AddCommand(ContentsModel root, AbstractElementModel model, AbstractElementModel afterModel) {
		super();
		this.root = root;
		this.model = model;
		
		oldroot = model.getParent();
		
		//beforeIndex = oldroot.getChildIndex(model);
		beforeIndex = oldroot.getChildren().indexOf(model);

		if (afterModel != null) {
			//addIndex = root.getChildIndex(afterModel);
			addIndex = root.getChildren().indexOf(afterModel);
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		//model.setConstraint(rectangle);
		oldroot.removeChild(model);
		if (addIndex > 0) {
			root.addChild(addIndex, model);
		} else {
			root.addChild(model);
		}
	}

}
