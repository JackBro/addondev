package gef.example.helloworld.editparts.tree;

import gef.example.helloworld.model.AbstractDataModel;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.MenuBaseModel;
import gef.example.helloworld.model.MenuItemModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class TreeEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
	    EditPart part = null;
	    
	    if(model instanceof MenuItemModel)
	    	part = new DataTreeEditPart();
	    else if(model instanceof MenuBaseModel)
	    	part = new ContentsTreeEditPart();
	    else if (model instanceof ContentsModel)
		  part = new ContentsTreeEditPart();
	    else if (model instanceof AbstractElementModel)
	      part = new ContentsTreeEditPart();

	    if (part != null)
	      part.setModel(model);

	    return part;
	}

}
