package gef.example.helloworld.model.commands;

import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.LabelModel;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

public class ChangeConstraintCommand extends Command {
	private AbstractElementModel model; // このコマンドによって変更されるモデル
	private Rectangle constraint; // 変更する制約
	private Rectangle oldConstraint; // 以前の制約

	/* (非 Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		// モデルの制約を変更する
		model.setConstraint(constraint);
	}

	public void setConstraint(Rectangle rect) {
		constraint = rect;
	}

	public void setModel(Object model) {
		this.model = (AbstractElementModel) model;
		// 変更前の情報を記録
		//oldConstraint = helloModel.getConstraint();
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		//helloModel.setConstraint(oldConstraint);
	}

}
