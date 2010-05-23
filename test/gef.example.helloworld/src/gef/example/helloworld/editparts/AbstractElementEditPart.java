package gef.example.helloworld.editparts;

import gef.example.helloworld.model.AbstractElementModel;

import java.beans.PropertyChangeEvent;

public abstract class AbstractElementEditPart extends AbstractEditPartWithListener {

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(AbstractElementModel.ATTR_FLEX)){
	    	AbstractEditPartWithListener ep = (AbstractEditPartWithListener)getParent();
	    	ep.resize();
		}
		super.propertyChange(evt);
		
	}

}
