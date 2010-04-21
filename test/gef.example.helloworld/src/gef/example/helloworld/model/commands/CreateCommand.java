package gef.example.helloworld.model.commands;

import gef.example.helloworld.model.AbstractModel;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.HelloModel;
import gef.example.helloworld.model.VBoxModel;

import org.eclipse.gef.commands.Command;

public class CreateCommand extends Command {
	private ContentsModel contentsModel;
	private ElementModel helloModel;

	
	public CreateCommand(ContentsModel contentsModel, ElementModel helloModel) {
		//super();
		this.contentsModel = contentsModel;
		this.helloModel = helloModel;
		this.helloModel.setParent(contentsModel);
	}

	/* (非 Javadoc)
	* @see org.eclipse.gef.commands.Command#execute()
	*/
	public void execute() {
//		if(vboxModel != null)
//			vboxModel.addChild(helloModel);
//		else
//			contentsModel.addChild(helloModel);
		
		contentsModel.addChild(helloModel);
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

	/* (非 Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		//contentsModel.removeChild(helloModel);
	}
}
