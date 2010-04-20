package gef.example.helloworld.editparts;

import gef.example.helloworld.model.AbstractModel;

import java.beans.PropertyChangeListener;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

abstract public class EditPartWithListener
	extends AbstractGraphicalEditPart
	implements PropertyChangeListener {

	public void activate() {
		super.activate();
		// 自身をリスナとしてモデルに登録
		 ((AbstractModel) getModel()).addPropertyChangeListener(this);
	}

	public void deactivate() {
		super.deactivate();
		// モデルから削除
		 ((AbstractModel) getModel()).removePropertyChangeListener(this);
	}
}
