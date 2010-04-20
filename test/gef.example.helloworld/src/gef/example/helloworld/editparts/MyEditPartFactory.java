package gef.example.helloworld.editparts;

import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.HelloModel;
import gef.example.helloworld.model.VBoxModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class MyEditPartFactory implements EditPartFactory {

	/* (非 Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;

		// モデルの型を調べて対応するEditPartを作成
//		if (model instanceof ContentsModel)
//			part = new ContentsEditPart();
//		else 
		if(model instanceof VBoxModel)
			part = new VBoxEditPart();
		else if (model instanceof HelloModel)
			part = new HelloEditPart();

		part.setModel(model); // モデルをEditPartに設定する
		return part;
	}

}
