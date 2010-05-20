package gef.example.helloworld.editpolicies;

import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.commands.CreateCommand;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.TreeContainerEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

public class TreeEditPolicy extends TreeContainerEditPolicy {
	protected Command createCreateCommand(ElementModel child) {
		
//		Rectangle rect;
//		if (r == null) {
//			rect = new Rectangle();
//			rect.setSize(new Dimension(-1, -1));
//		} else {
//			rect = r;
//		}
		CreateCommand cmd = new CreateCommand((ContentsModel) getHost().getModel(), child);
		//cmd.setLocation(rect);
//		cmd.setParent((ElementModel) getHost().getModel());
//		cmd.setChild(child);
//		cmd.setLabel(label);
//		if (index >= 0)
//			cmd.setIndex(index);
		return cmd;
	}

	@Override
	protected Command getAddCommand(ChangeBoundsRequest request) {
		// TODO Auto-generated method stub
		CompoundCommand command = new CompoundCommand();
        List editparts = request.getEditParts();
        int index = findIndexOfTreeItemAt(request.getLocation());
	    for (Object object : editparts) {
	    	EditPart child = (EditPart)object;
	    	boolean add = true;
	    	if(!add){
	    		
	    	}else{
	    		if (index == -1) index = 0;
	    		 command.add(createCreateCommand((ElementModel)child.getModel()));
	    	}

	    	  
		}
		return command;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// TODO Auto-generated method stub
		if(getHost().getModel() instanceof ContentsModel){
			ElementModel model = (ElementModel) request.getNewObject();	
			CreateCommand command = new CreateCommand((ContentsModel)getHost().getModel(), model);		
			return command;
		}
		return null;
	}

	@Override
	protected Command getMoveChildrenCommand(ChangeBoundsRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
