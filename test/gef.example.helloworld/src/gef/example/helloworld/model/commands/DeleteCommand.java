package gef.example.helloworld.model.commands;

import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.HelloModel;

import org.eclipse.gef.commands.Command;

public class DeleteCommand extends Command {
	private ContentsModel contentsModel;
	private HelloModel helloModel;

	/* (非 Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		// モデルを削除する
		contentsModel.removeChild(helloModel);
	}

	public void setContentsModel(Object model) {
		contentsModel = (ContentsModel) model;
	}

	public void setHelloModel(Object model) {
		helloModel = (HelloModel) model;
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		// もう一度追加しなおす
		contentsModel.addChild(helloModel);
	}

}
