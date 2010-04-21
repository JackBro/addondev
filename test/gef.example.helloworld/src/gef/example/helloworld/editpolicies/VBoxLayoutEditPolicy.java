package gef.example.helloworld.editpolicies;

import gef.example.helloworld.model.AbstractModel;
import gef.example.helloworld.model.HelloModel;
import gef.example.helloworld.model.commands.CreateCommand;
import gef.example.helloworld.model.commands.MoveChildCommand;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class VBoxLayoutEditPolicy extends FlowLayoutEditPolicy {

	@Override
	protected Command createAddCommand(EditPart child, EditPart after) {
		// TODO Auto-generated method stub
		//getHost()
		
		return null;
	}

	@Override
	protected boolean isHorizontal() {
		// TODO Auto-generated method stub
        IFigure figure = ((GraphicalEditPart) getHost()).getContentPane();  
        LayoutManager layout = figure.getLayoutManager();  
        if (layout instanceof FlowLayout)  
                return ((FlowLayout) figure.getLayoutManager()).isHorizontal();  
        if (layout instanceof ToolbarLayout)  
                return ((ToolbarLayout) figure.getLayoutManager()).isHorizontal();  
        return false;  
	}

	@Override
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		// TODO Auto-generated method stub
		return new MoveChildCommand(getHost(), child, after);
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// TODO Auto-generated method stub
		CreateCommand command = new CreateCommand();
		// 作成するモデルのサイズと位置(制約)を取得
		//Rectangle constraint = (Rectangle) getConstraintFor(request);
		// 新規作成するモデルの取得
		AbstractModel model = (AbstractModel) request.getNewObject();
		// 制約の設定
		//model.setConstraint(constraint);

		command.setContentsModel(getHost().getModel());
		command.setHelloModel(model);
		return command;
	}

}
