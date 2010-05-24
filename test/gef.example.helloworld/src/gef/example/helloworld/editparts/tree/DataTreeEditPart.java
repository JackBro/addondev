package gef.example.helloworld.editparts.tree;

import gef.example.helloworld.model.AbstractElementModel;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;

public class DataTreeEditPart extends ContentsTreeEditPart {

	private ArrayList EMPTY = new ArrayList();
	
	@Override
	protected List getModelChildren() {
		// TODO Auto-generated method stub
		return EMPTY;
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		// TODO Auto-generated method stub
		super.addChildVisual(childEditPart, index);
	}

	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		// TODO Auto-generated method stub
		super.removeChildVisual(childEditPart);
	}

}
