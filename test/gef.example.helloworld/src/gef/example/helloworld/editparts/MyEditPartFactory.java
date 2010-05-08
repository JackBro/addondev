package gef.example.helloworld.editparts;

import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.GridModel;
import gef.example.helloworld.model.HBoxModel;
import gef.example.helloworld.model.LabelModel;
import gef.example.helloworld.model.RootModel;
import gef.example.helloworld.model.VBoxModel;
import gef.example.helloworld.model.WindowModel;

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
			part = new BoxEditPart();
		else if(model instanceof HBoxModel)
			part = new BoxEditPart();
		else if(model instanceof LabelModel)
			part = new LabelEditPart();
		else if(model instanceof GridModel)
			part = new GridEditPart();		
		else if(model instanceof WindowModel)
			part = new WindowEditPart();		
		else if(model instanceof RootModel)
			part = new RootEditPart();

		//((ElementModel)model).installModelProperty();
		part.setModel(model); // モデルをEditPartに設定する
		return part;
	}

}
