package gef.example.helloworld.model.commands;

import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.AbstractElementModel;
import org.eclipse.gef.commands.Command;

public class DeleteCommand extends Command {
	private ContentsModel contentsModel;
	private AbstractElementModel elementModel;

	/* (非 Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		// モデルを削除する
		contentsModel.removeChild(elementModel);
	}

	public void setContentsModel(Object model) {
		contentsModel = (ContentsModel) model;
	}

	public void setHelloModel(Object model) {
		elementModel = (AbstractElementModel) model;
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		// もう一度追加しなおす
		contentsModel.addChild(elementModel);
	}

}
