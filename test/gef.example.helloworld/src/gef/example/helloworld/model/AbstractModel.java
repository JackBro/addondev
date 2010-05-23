package gef.example.helloworld.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

abstract public class AbstractModel implements IPropertySource  {

	// リスナのリスト
	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	
	// リスナの追加
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	// モデルの変更を通知
	public void firePropertyChange(
		String propName,
		Object oldValue,
		Object newValue) {

		listeners.firePropertyChange(propName, oldValue, newValue);
	}

	// リスナの削除
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return this;
	}

//	@Override
//	public IPropertyDescriptor[] getPropertyDescriptors() {
//		// TODO Auto-generated method stub
//		return new IPropertyDescriptor[0];
//	}
//
//	@Override
//	public Object getPropertyValue(Object id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean isPropertySet(Object id) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void resetPropertyValue(Object id) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void setPropertyValue(Object id, Object value) {
//		// TODO Auto-generated method stub
//		
//	}

}
