package gef.example.helloworld.editparts;

import gef.example.helloworld.model.*;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class MyEditPartFactory implements EditPartFactory {

	/* (非 Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;
		
		if(model instanceof TextBoxModel)
			part = new TextBoxEditPart();	
		if(model instanceof VBoxModel)
			part = new BoxEditPart();
		else if(model instanceof HBoxModel)
			part = new BoxEditPart();
		if(model instanceof GroupBoxModel)
			part = new GroupBoxEditPart();
		else if(model instanceof LabelModel)
			part = new LabelEditPart();
		else if(model instanceof GridModel)
			part = new GridEditPart();		
		else if(model instanceof WindowModel)
			part = new WindowEditPart();		
		else if(model instanceof RootModel)
			part = new RootEditPart();
		else if(model instanceof RadioModel)
			part = new RadioEditPart();
		else if(model instanceof RadioGroupModel)
			part = new RadioGroupEditPart();
		else if(model instanceof TabBoxModel)
			part = new TabBoxEditPart();
		else if(model instanceof TabPanelModel)
			part = new TabPanelEditPart();
		else if(model instanceof MenuListModel)
			part = new MenuListEditPart();
		else if(model instanceof PrefwindowModel)
			part = new PrefwindowEditPart();
		else if(model instanceof PrefpaneModel)
			part = new TabPanelEditPart();
		
		part.setModel(model); // モデルをEditPartに設定する
		return part;
	}

}
