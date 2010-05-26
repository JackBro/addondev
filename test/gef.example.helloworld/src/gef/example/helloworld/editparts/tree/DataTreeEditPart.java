package gef.example.helloworld.editparts.tree;

import gef.example.helloworld.editparts.MenuItemEditPart;
import gef.example.helloworld.model.AbstractElementModel;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;

public class DataTreeEditPart extends ContentsTreeEditPart {

	private ArrayList EMPTY = new ArrayList();
	
//	@Override
//	protected List getModelChildren() {
//		// TODO Auto-generated method stub
//		List ll = ();
//		return EMPTY;
//	}


	@Override
	public List getChildren() {
		// TODO Auto-generated method stub
		List ll = super.getChildren();
		List l2 = getModelChildren();
		return super.getChildren();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		super.propertyChange(evt);
		//this.children = getModelChildren();
		//this.children.add(new MenuItemEditPart());
		//refreshChildren();
	}

	@Override
	protected List getModelChildren() {
		// TODO Auto-generated method stub
		return super.getModelChildren();
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
