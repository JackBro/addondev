package gef.example.helloworld.editparts.tree;

import gef.example.helloworld.model.AbstractModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

public abstract class MyTreeEditPart extends AbstractTreeEditPart implements
		PropertyChangeListener {

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		super.activate();
		((AbstractModel) getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		((AbstractModel) getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}

}
