package gef.example.helloworld.editpolicies;

import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.HelloModel;
import gef.example.helloworld.model.commands.ChangeConstraintCommand;
import gef.example.helloworld.model.commands.CreateCommand;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class MyXYLayoutEditPolicy extends XYLayoutEditPolicy {

	/* (非 Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	protected Command createAddCommand(EditPart child, Object constraint) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	protected Command createChangeConstraintCommand(
		EditPart child,
		Object constraint) {
		// コマンドの作成
		ChangeConstraintCommand command = new ChangeConstraintCommand();
		// 編集対象のモデルの設定
		command.setModel(child.getModel());
		command.setConstraint((Rectangle) constraint);
		// コマンドを返す
		return command;

	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		//CreateCommand command = new CreateCommand();
		
		// 作成するモデルのサイズと位置(制約)を取得
		//Rectangle constraint = (Rectangle) getConstraintFor(request);
		// 新規作成するモデルの取得
		ElementModel model = (ElementModel) request.getNewObject();
		// 制約の設定
		//model.setConstraint(constraint);
		
		CreateCommand command = new CreateCommand((ContentsModel)getHost().getModel(), model);
		//command.setContentsModel(getHost().getModel());
		//command.setHelloModel(model);
		return command;
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand(org.eclipse.gef.Request)
	 */
	protected Command getDeleteDependantCommand(Request request) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
