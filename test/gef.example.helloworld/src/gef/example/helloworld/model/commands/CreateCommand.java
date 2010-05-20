package gef.example.helloworld.model.commands;

import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import org.eclipse.gef.commands.Command;

public class CreateCommand extends Command {
	private ContentsModel contentsModel;
	private ElementModel childModel;
	private int index;
	
	public CreateCommand(ContentsModel contentsModel, ElementModel childModel, int index) {
		//super();
		this.contentsModel = contentsModel;
		this.childModel = childModel;
		this.childModel.setParent(contentsModel);
		this.index = index;
	}

	public void execute() {
//		if(vboxModel != null)
//			vboxModel.addChild(helloModel);
//		else
//			contentsModel.addChild(helloModel);
		if(index<0){
			contentsModel.addChild(childModel);
		}else{
			contentsModel.addChild(index, childModel);
		}
		//contentsModel.addChild(childModel);
	}

//	public void setContentsModel(Object model) {
////		if(model instanceof VBoxModel)
////			vboxModel = (VBoxModel) model;
////		else
////			contentsModel = (ContentsModel) model;
//		
//		contentsModel = (ContentsModel) model;
//	}
//
//	public void setHelloModel(Object model) {
//		helloModel = (AbstractModel) model;
//	}


	public void undo() {
		contentsModel.removeChild(childModel);
	}
}
