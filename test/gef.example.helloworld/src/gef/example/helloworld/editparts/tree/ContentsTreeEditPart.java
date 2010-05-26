package gef.example.helloworld.editparts.tree;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.editpolicies.TreeEditPolicy;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.AbstractElementModel;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.EditPolicy;

public class ContentsTreeEditPart extends MyTreeEditPart {

	@Override
	protected void fireActivated() {
		// TODO Auto-generated method stub
		super.fireActivated();
	}

//	@Override
//	protected void fireSelectionChanged() {
//		// TODO Auto-generated method stub
//		List list = getViewer().getSelectedEditParts();
//		Object oj = getRoot();
//		super.fireSelectionChanged();
//	}

	@Override
	protected List getModelChildren() {
		// TODO Auto-generated method stub
		return ((AbstractElementModel)getModel()).getChildren();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getPropertyName().equals(ContentsModel.P_ADD_CHILD))
		      refreshChildren();
		if(evt.getPropertyName().equals(ContentsModel.P_REMOVE_CHILD))
		      refreshChildren();
		//if(evt.getPropertyName().equals("change_menu"))
		//      refreshChildren();
	}

	@Override
	protected String getText() {
		// TODO Auto-generated method stub
		return super.getText();
	}

	@Override
	protected void refreshVisuals() {
		// TODO Auto-generated method stub
		//super.refreshVisuals();
		AbstractElementModel model = (AbstractElementModel) getModel();
		setWidgetText(model.getName());
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		//super.createEditPolicies();
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new TreeEditPolicy());
	}

}
