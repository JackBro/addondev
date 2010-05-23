package gef.example.helloworld.editpolicies;

import gef.example.helloworld.editparts.RadioEditPart;
import gef.example.helloworld.editparts.TabPanelEditPart;
import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.RadioModel;
import gef.example.helloworld.model.TabPanelModel;

import org.eclipse.gef.EditPart;

public class TabBoxLayoutEditPolicy extends FilterBoxLayoutEditPolicy {

	@Override
	protected boolean isFilter(EditPart editpart) {
		// TODO Auto-generated method stub
		return !(editpart instanceof TabPanelEditPart);
	}

	@Override
	protected boolean isFilter(AbstractElementModel model) {
		// TODO Auto-generated method stub
		return !(model instanceof TabPanelModel);
	}

}
