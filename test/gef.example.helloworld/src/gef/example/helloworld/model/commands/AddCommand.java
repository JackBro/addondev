package gef.example.helloworld.model.commands;

import gef.example.helloworld.model.AbstractModel;
import gef.example.helloworld.model.ContentsModel;

import org.eclipse.gef.commands.Command;

public class AddCommand extends Command {
	
	protected ContentsModel root;
	protected AbstractModel model;
	
	public AddCommand(ContentsModel root, AbstractModel model) {
		super();
		this.root = root;
		this.model = model;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		//super.execute();
		root.addChild(model);
	}

}
